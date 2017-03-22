package zentech.myapplication.DAL;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import zentech.myapplication.Entities.Boat;
import zentech.myapplication.Entities.Dock;
import zentech.myapplication.Entities.GetBoatOrder;
import zentech.myapplication.Entities.Marina;
import zentech.myapplication.Entities.ReturnBoatOrder;
import zentech.myapplication.Entities.Service;
import zentech.myapplication.Entities.User;
import zentech.myapplication.Entities.Worker;
import zentech.myapplication.ServerSideHandlers.ServerRequest;


/**
 * Created by izik on 12/14/2016.
 */

public class Model
{
    private static  Model instance = new Model();
    private User currentUser;
    private ArrayList<String> marineIds = new ArrayList<>();
    public static String SharedFolder = "https://192.168.20.1/Marines/";
    private static String serverIP = "http://192.168.20.1:8080";
    public static String MarineProfileImageName = "/1.jpg";

    private Model()
    {
    }

    public User getCurrentUser()
    {
        if (currentUser != null)
        {
        return currentUser;}
        else
        {
            // no way we dont have user details from login or register, we need to alert that the user needs to  reopen the application.
            // and we should close it by force.
            return null;
        }
    }

    //Always use this singltone, dont do new Model in your code. ever!.
    public static Model instance() {
        return instance;
    }

    //region Public Methods

    public User LoginUser(String email, String password)
    {
        JSONObject jsonUserDetails = new JSONObject();

        try {
            jsonUserDetails.put("email", email);
            jsonUserDetails.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON("http://192.168.20.1:8080/loginUser", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        //if user is found
        User userToReturn = ConvertJSONToUser(json);

        currentUser = userToReturn;
        return userToReturn;
    }

    public User RegisterUser(String email, String password, String name, String zipCode, String dob)
    {
        JSONObject jsonUserDetails = new JSONObject();

        try {
            JSONArray boats = new JSONArray();

            jsonUserDetails.put("email", email);
            jsonUserDetails.put("password", password);
            jsonUserDetails.put("name", name);
            jsonUserDetails.put("zipCode", zipCode);
            jsonUserDetails.put("dob", dob);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON("http://192.168.20.1:8080/registerUserByEmail", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        //if user is found
        User userToReturn = ConvertJSONToUser(json);

        currentUser = userToReturn;
        return userToReturn;
    }

    public ArrayList<String> getMarineIds() {
        return marineIds;
    }

    private boolean DidServerHadErrors(JSONObject json) {
        try
        {
            String error = json.getString("error");

            //theres error!
            return true;
        }
        catch (Exception e)
        {
            // there is no error and we can return false
            return false;
        }
    }

    private User ConvertJSONToUser(JSONObject json)
    {
        String userObjectID = "";
        ArrayList<Boat> boats = new ArrayList<>();
        String email = "";
        String name = "";
        String facebookEmail ="";
        String userFacebookID = "";
        String zipCode= "";
        String dob="";

        //from facebook details
        try
        {
            JSONObject userJson = (JSONObject) json.get("user");
            userObjectID = userJson.getString("_id");
            userFacebookID = userJson.getString("FacebookID");
            facebookEmail = userJson.getString("FacebookEmail");
            name = userJson.getString("Name");
            email = userJson.getString("Email");
            zipCode = userJson.getString("ZipCode");
            dob = userJson.getString("DateOfBirth");

            JSONArray jar = new JSONArray(userJson.get("BoatsIDs").toString());

            for (int i=0; i<jar.length();i++)
            {

                Boat b = GetBoatByID(jar.get(i).toString());

                if (b!= null) {
                    boats.add(b);
                }
                else
                {
                    Log.e("loadingBoatByID","there was an error while trying to load the boat's info : " + jar.get(i).toString());
                }
            }
        }
        catch (JSONException e)
        {
        }

        return new User(userObjectID,email,name,boats, facebookEmail, userFacebookID, zipCode,dob);
    }

    public Boat GetBoatByID(String id)
    {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP+"/getBoatByID", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        try {

            JSONObject objJson = (JSONObject)json.get("boat");
            Boat b = ConvertJSONToBoat(objJson);
            return b;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private Boat ConvertJSONToBoat(JSONObject jobj)
    {
        String name = "";
        String objectId = "";
        double fuel=0;
        boolean isDocked = false;
        boolean isRacked = false;
        double weight = 0;
        String ownerID;
        String dockID;
        ArrayList<String> permittedUsersIDs = new ArrayList<>();
        String rackID;
        boolean isForSale;
        String lastTreatedTime;
        String lastTreatedDate;
        ArrayList<GetBoatOrder> getBoatOrders = new ArrayList<>();
        ArrayList<ReturnBoatOrder> returnBoatOrders = new ArrayList<>();
        String marineID = "";

        try
        {
            name = jobj.getString("Name");
            objectId = jobj.getString("_id");
            isDocked = Boolean.parseBoolean(jobj.getString("IsDocked"));
            isRacked = Boolean.parseBoolean(jobj.getString("IsRacked"));
            isForSale = Boolean.parseBoolean(jobj.getString("IsForSale"));
            weight = Double.parseDouble(jobj.getString("Weight"));
            ownerID = jobj.getString("OwnerID");
            rackID = jobj.getString("RackID");
            dockID = jobj.getString("DockName");
            marineID = jobj.getString("AtMarine");
            lastTreatedTime = jobj.getString("LastTreatedTime");
            lastTreatedDate = jobj.getString("LastTreatedDate");
            JSONArray jar = new JSONArray(jobj.getString("PermittedUsersIDs"));

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jsonobj = jar.getJSONObject(i);
                String id = jsonobj.getString("_id");
                if(!id.isEmpty()) {
                    permittedUsersIDs.add(jsonobj.getString("_id"));
                }
            }

            jar = new JSONArray(jobj.getString("GetBoatOrdersIDs"));

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jsonobj = jar.getJSONObject(i);
                String objID = jsonobj.getString("_id");
                GetBoatOrder order = GetBoatOrderByID(objID);
                if(order != null) {
                    getBoatOrders.add(order);
                }
            }

            jar = new JSONArray(jobj.getString("ReturnBoatOrdersIDs"));

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jsonobj = jar.getJSONObject(i);
                String objID = jsonobj.getString("_id");
                ReturnBoatOrder order = GetReturnBoatOrderByID(objID);
                returnBoatOrders.add(order);
            }

            Boat boat = new Boat(objectId,name,fuel,isDocked,dockID,rackID,isRacked,
                            weight,ownerID,permittedUsersIDs,isForSale,lastTreatedTime,
                                                                lastTreatedDate, marineID);

            return boat;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ReturnBoatOrder GetReturnBoatOrderByID(String objID) {

        JSONObject jobj = new JSONObject();

        try {
            jobj.put("id", objID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getReturnBoatOrder", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }


        try {

            JSONObject objJson = (JSONObject)json.get("order");
            ReturnBoatOrder order = ConvertJSONToReturnBoatOrder(objJson);

            return order;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ReturnBoatOrder ConvertJSONToReturnBoatOrder(JSONObject json) {

        String _objID = "";
        String boatID = "";
        String time = "";
        String date = "";
        ArrayList<Service> selectedServices = new ArrayList<>();
        String orderingUserID = "";

        //from facebook details
        try
        {

            _objID = json.getString("_id");
            boatID = json.getString("BoatID");
            time = json.getString("Time");
            date = json.getString("Date");
            orderingUserID = json.getString("OrderingUserID");

            JSONArray jar = new JSONArray(json.get("SelectedServices").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                Service srv = ConvertJSONToService(jobj);

                if (srv!= null) {
                    selectedServices.add(srv);
                }
                else
                {
                    Log.e("ConvertJSONToService","there was an error while trying to load the service's info : " + jobj.getString("_id"));
                }
            }

            return new ReturnBoatOrder(_objID,time,date,orderingUserID,selectedServices, boatID);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public GetBoatOrder GetBoatOrderByID(String objID)
    {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("id", objID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON("http://192.168.20.1:8080/getBoatOrderByID", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        try {

            JSONObject objJson = (JSONObject)json.get("order");
            GetBoatOrder order = ConvertJSONToGetBoatOrder(objJson);

            return order;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private GetBoatOrder ConvertJSONToGetBoatOrder(JSONObject json)
    {
        String _objID = "";
        String boatID = "";
        String time = "";
        String date = "";
        ArrayList<Service> moreServices = new ArrayList<>();
        String orderingUserID = "";
        double fuel;
        boolean ice;

        //from facebook details
        try
        {
            _objID = json.getString("_id");
            boatID = json.getString("BoatID");
            time = json.getString("Time");
            date = json.getString("Date");
            orderingUserID = json.getString("OrderingUserID");
            ice = json.getBoolean("Ice");
            fuel = json.getDouble("fuel");

            JSONArray jar = new JSONArray(json.get("Services").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                //TODO: this is temporary... because of the way we insert the personal request..

                String name = jobj.getString("Name");
                String desc = jobj.getString("Description");
                Service srv = new Service(name,desc,"",0,"");


                //if (srv!= null) {
                moreServices.add(srv);
               // }
               /*else
                {
                    Log.e("ConvertJSONToService","there was an error while trying to load the service's info : " + jobj.getString("_id"));
                }*/
            }

            return new GetBoatOrder(_objID,time,date,orderingUserID,moreServices, boatID,fuel,ice);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private Service ConvertJSONToService(JSONObject jobj)
    {
        String name = "";
        String description="";
        String objectID="";
        double price = 0;
        String marineID;

        try
        {
            name = jobj.getString("Name");
            description = jobj.getString("Description");
            objectID = jobj.getString("_id");
            price = jobj.getDouble("Price");
            marineID = jobj.getString("MarineID");

            return new Service(name,description,objectID,price, marineID);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public User GetUserByID(String objectID)
    {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("id", objectID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON("http://192.168.20.1:8080/getUserByID", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        User user = ConvertJSONToUser(json);

        return user;
    }

    public Dock getDockByID(String objectId)
    {
        ServerRequest serverRequest = new ServerRequest();
        JSONObject jobj = new JSONObject();

        try
        {
            jobj.put("objectId", objectId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = serverRequest.getJSON("http://192.168.20.1:8080/getDockByID", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        Dock dockedAt = ConvertJSONtoDock(json);

        return dockedAt;

    }

    private Dock ConvertJSONtoDock(JSONObject jobj)
    {
        String name ="";
        String objectId="";
        ArrayList<String> boatsIDs = new ArrayList<>();

        try
        {
            name = jobj.get("Name").toString();
            JSONArray jar = new JSONArray(jobj.getString("BoatsIDs"));

            for (int i=0; i<jar.length();i++)
            {
                boatsIDs.add(jar.getString(i));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Dock dock = new Dock(objectId,boatsIDs,name);

        return dock;
    }

    private JSONObject ConvertBoatToJSON(Boat b)
    {
        return new JSONObject();
    }

    public boolean loginUserThroughFacebook(String id, String email, String name)
    {
        JSONObject jsonUserDetails = new JSONObject();

        try
        {
            jsonUserDetails.put("facebookID", id);
            jsonUserDetails.put("facebookEmail", email);
            jsonUserDetails.put("name", name);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON("http://192.168.20.1:8080/loginUserWithFacebook", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            Log.e("Model","loginUserWithFacebook error");
            return false;
        }

        //if user is found
        User userToReturn = ConvertJSONToUser(json);

        currentUser = userToReturn;
        return true;
    }

    public void updateUserDetails(String email, String name) {
        //TODO:: update server with user facebook details.
    }

    public  String[] getMarinesPicturesPaths()
    {
        ServerRequest serverRequest = new ServerRequest();
        JSONObject jsonUserDetails = new JSONObject();

        JSONObject json = serverRequest.getJSON(serverIP + "/getMarinesPicturesPaths", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            Log.e("Model","getMarinesPicturesPaths error");
            return new String[0];
        }

        ArrayList<String> Results = new ArrayList();
        try {
            JSONArray jar = json.getJSONArray("marines");
            for (int i = 0; i < jar.length(); i++)
            {
                String imagePath = SharedFolder + jar.getJSONObject(i).getString("_id") + MarineProfileImageName;
                Results.add(imagePath); //adding the pictures folder path

                //adding a marine to the all_marines array if its new.
                if(!marineIds.contains(jar.getJSONObject(i).getString("_id")))
                {
                    marineIds.add(jar.getJSONObject(i).getString("_id"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // converting the arraylist<string> to desired string[]
        String[] results = new String[Results.size()];
        results = Results.toArray(results);

        return results;

    }

    public Marina getMarineById(String id)
    {
        JSONObject jsonUserDetails = new JSONObject();
        try {

            jsonUserDetails.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getMarineById", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        //if user is found
        Marina marinaResult = ConvertJSONToMarina(json);

        return marinaResult;
    }

    private Marina ConvertJSONToMarina(JSONObject json)
    {
        String marinaObjectID = "";
        String email = "";
        String name = "";
        String facebookEmail ="";
        String marinaFacebookID = "";
        ArrayList<Dock> docks = new ArrayList<>();
        ArrayList<Worker> workers = new ArrayList<>();
        ArrayList<Service> returnBoatServices = new ArrayList<>();
        ArrayList<Service> getBoatServices = new ArrayList<>();
        ArrayList<String> rackedBoatsIDs = new ArrayList<>();
        ArrayList<GetBoatOrder> getBaotOrders = new ArrayList<>();

        //from facebook details
        try
        {
            JSONObject marinaJson = (JSONObject) json.get("marine");
            marinaObjectID = marinaJson.getString("_id");
            marinaFacebookID = marinaJson.getString("FacebookID");
            facebookEmail = marinaJson.getString("FacebookEmail");
            name = marinaJson.getString("Name");
            email = marinaJson.getString("Email");

            JSONArray jar = new JSONArray(marinaJson.get("Docks").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                Dock dock =  ConvertJSONtoDock(jobj);
                docks.add(dock);
            }

            jar = new JSONArray(marinaJson.get("GetBoatOrders").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                GetBoatOrder order =  ConvertJSONToGetBoatOrder(jobj);
                getBaotOrders.add(order);
            }

            jar = new JSONArray(marinaJson.get("WorkersIDs").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                String id = jobj.getString("_id");
                Worker worker =  GetWorkerByID(id);
                workers.add(worker);
            }


            jar = new JSONArray(marinaJson.get("ReturnBoatServicesIDs").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                String id = jobj.getString("_id");

                Service srv =  GetServiceByID(id);
                returnBoatServices.add(srv);
            }

            jar = new JSONArray(marinaJson.get("GetBoatServicesIDs").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                String id = jobj.getString("_id");
                Service srv =  GetServiceByID(id);
                getBoatServices.add(srv);
            }

            jar = new JSONArray(marinaJson.get("RackedBoatsIDs").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                String rackedBoatID =  jobj.getString("rackedBoatID");
                rackedBoatsIDs.add(rackedBoatID);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return new Marina(name ,workers ,marinaObjectID, email,facebookEmail, marinaFacebookID,docks,returnBoatServices, getBoatServices, rackedBoatsIDs);
    }

    public Service GetServiceByID(String id)
    {
        JSONObject jsonUserDetails = new JSONObject();
        try {

            jsonUserDetails.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getServiceById", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        //if user is found
        JSONObject jobj = null;
        try {
            jobj = (JSONObject) json.get("service");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Service srvResult = ConvertJSONToService(jobj);

        return srvResult;
    }

    public Worker GetWorkerByID(String id)
    {
        JSONObject jsonUserDetails = new JSONObject();
        try {

            jsonUserDetails.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getWorkerById", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        //if user is found
        Worker workerResult = ConvertJSONToWorker(json);

        return workerResult;
    }

    private Worker ConvertJSONToWorker(JSONObject jobj)
    {
        String name = "";
        String lastName = "";
        String objID = "";
        String iD = "";
        Boolean isAppointed = false;
        double salary = 0;

        try {
            jobj = (JSONObject)jobj.get("worker");
            name =  jobj.getString("Name");
            lastName =  jobj.getString("LastName");
            objID = jobj.getString("_id");
            iD = jobj.getString("ID");
            isAppointed = jobj.getBoolean("IsAppointed");
            salary = jobj.getDouble("Salary");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Worker(name,lastName,objID,iD,isAppointed,salary);
    }

   /* public String getMarinePicturePathById(String id)
    {
        return SharedFolder + id + "/1.jpg";
    }*/

    public ArrayList<Marina> getAllMarines()
    {
        JSONObject jsonMarineDetails = new JSONObject();
        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getAllMarines", jsonMarineDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        ArrayList<Marina> Results = new ArrayList();
        try {
            JSONArray jar = json.getJSONArray("marines");
            for (int i = 0; i < jar.length(); i++) {
                Results.add(ConvertJSONToMarina(jar.getJSONObject(i))); //Getting current json object and converting to marine

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* for (Marina m :  Results)
        {
            if(!allMarines.contains(m))
            {
                allMarines.add(m);
            }
        }*/

        return Results;
    }

    public String OrderGetBoatFromMarina(String selectedDate, String selectedTime, String boatID, String orderingPersonID, String marinaID, double fuel, boolean ice, ArrayList<String> services)
    {
        JSONObject jsonUserDetails = new JSONObject();
        JSONArray srvArray = new JSONArray();

        try
        {
            jsonUserDetails.put("date", selectedDate);
            jsonUserDetails.put("time", selectedTime);
            jsonUserDetails.put("boatID", boatID);
            jsonUserDetails.put("orderingUserID", orderingPersonID);
            jsonUserDetails.put("marineID", marinaID);
            jsonUserDetails.put("ice", ice);
            jsonUserDetails.put("fuel", fuel);

            //ToDo: u need to change this from ArrayList<String> to ArrayList<Service> and do everything in a more detailed way...
            // this workaround is just for Beta, so more services right now ius just an edit text field.
            for (String service : services)
            {
                JSONObject srv = new JSONObject();
                srv.put("Name","PersonalRequest");
                srv.put("Description", service);
                srvArray.put(srv);
            }

            jsonUserDetails.put("moreServices", srvArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/orderGetBoat", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            //TODO: we need to tell the user that his request wasnt sent.... or he should check it by status?
            return "";
        }
        String orderObjectID = "";

        try
        {
            JSONObject orderJson = (JSONObject) json.get("getOrder");
            orderObjectID = orderJson.getString("_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderObjectID;
    }

    public boolean addABoat(String name, double weight, boolean isForSale, String lastTreatedDate, String lastTreatedTime)
    {
        JSONObject jsonBoatDetails = new JSONObject();
        try {

            String orderingPersonID = Model.instance.getCurrentUser().getObjectId();

            jsonBoatDetails.put("name", name);
            jsonBoatDetails.put("date", lastTreatedDate);
            jsonBoatDetails.put("time", lastTreatedTime);
            jsonBoatDetails.put("isForSale", isForSale);
            jsonBoatDetails.put("ownerID", orderingPersonID);
            jsonBoatDetails.put("weight", weight);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/addABoat", jsonBoatDetails);

        if(DidServerHadErrors(json))
        {
            //TODO: we need to tell the user that his request wasnt sent.... or he should check it by status?
            return false;
        }

        return true;
    }

    public ArrayList<Boat> getUserBoatsByID(String userID) {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("id", userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getUserBoatsByID", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        ArrayList<Boat> boats = new ArrayList<>();

        try {
            JSONArray jar = ((JSONObject)((JSONArray)json.get("boats")).get(0)).getJSONArray("BoatsIDs");
            for (int i = 0; i < jar.length(); i++)
            {
                Boat boat = getBoatByID(jar.getString(i)); //Getting current json object and converting to boat
                boats.add(boat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return boats;
    }

    public Boat getBoatByID(String boatID)
    {
        JSONObject jsonUserDetails = new JSONObject();
        try {

            jsonUserDetails.put("id", boatID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getBoatByID", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        try
        {
            JSONObject objJson = (JSONObject)json.get("boat");
            Boat boatResult = ConvertJSONToBoat(objJson);

            return boatResult;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String UpdateOrderGetBoatFromMarina(String getBoatOrderID, String launchDate, String launchTime, String selectedBoatID, String userID,
                                               String selectedBoatMarineID, double fuel, boolean ice, ArrayList<String> services)
    {

        JSONObject jsonUserDetails = new JSONObject();
        JSONArray srvArray = new JSONArray();
        try {
            jsonUserDetails.put("_id", getBoatOrderID);
            jsonUserDetails.put("date", launchDate);
            jsonUserDetails.put("time", launchTime);
            jsonUserDetails.put("boatID", selectedBoatID);
            jsonUserDetails.put("orderingUserID", userID);
            jsonUserDetails.put("marineID", selectedBoatMarineID);
            jsonUserDetails.put("ice", ice);
            jsonUserDetails.put("fuel", fuel);

            //ToDo: u need to change this from ArrayList<String> to ArrayList<Service> and do everything in a more detailed way...
            // this workaround is just for Beta, so more services right now ius just an edit text field.
            for (String service : services) {

                JSONObject srv = new JSONObject();
                srv.put("name","PersonalRequest");
                srv.put("description", service);
                srvArray.put(srv);
            }

            jsonUserDetails.put("moreServices", srvArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/updateOrderGetBoat", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            //TODO: we need to tell the user that his request wasnt sent.... or he should check it by status?
            try
            {
                return json.getString("error");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        String orderObjectID = "";

        try {
            JSONObject orderJson = (JSONObject) json.get("getOrder");
            orderObjectID = orderJson.getString("_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderObjectID;
    }
}
