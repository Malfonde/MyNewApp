package zentech.myapplication.ServerSideHandlers;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;


/**
 * Created by izik on 12/14/2016.
 */
public class ServerRequest
{

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public ServerRequest()
    {
    }

    // Make sure the URL is https!! (we encode it)
    private JSONObject getJSONFromUrl(String urlSite, JSONObject jsonObject)
    {
        HttpURLConnection conn = null;

        try
        {
            URL url = new URL(urlSite);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("LaunchMyBoat", jsonObject.toString());

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            is = (InputStream) conn.getContent();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            json = sb.toString();
            Log.e("JSON", json);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;
    }

    public JSONObject getJSON(String url, JSONObject jsonObject) {

        ContentValues param = new ContentValues();
        param.put(url,jsonObject.toString());
        Request myTask = new Request();
        JSONObject jobj = null;
        try{
            jobj = AsyncTaskCompat.executeParallel( myTask, param ).get();
            //jobj = myTask.execute(param).get();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return jobj;
    }

    private class Request extends AsyncTask<ContentValues, String, JSONObject>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("AsyncTask", "onPreExecute");
        }

        @Override
        protected JSONObject doInBackground(ContentValues... args)
        {
            Log.e("AsyncTask", "doInBackground");
            //android.os.Debug.waitForDebugger();

            JSONObject json = null;

            ServerRequest request = new ServerRequest();

            Set<Map.Entry<String, Object>> keyValue = args[0].valueSet();
            Iterator itr = keyValue.iterator();

            while(itr.hasNext())
            {
                Map.Entry me = (Map.Entry)itr.next();
                String key = me.getKey().toString();
                JSONObject value = null;

                try
                {
                    value = new JSONObject(me.getValue().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                json = request.getJSONFromUrl(key, value);
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Log.e("AsyncTask", "onPostExecute");
            super.onPostExecute(json);
        }

    }
}