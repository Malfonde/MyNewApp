package zentech.myapplication.ClientSide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import zentech.myapplication.DAL.Model;
import zentech.myapplication.Entities.User;
import zentech.myapplication.R;

import static android.Manifest.permission.READ_CONTACTS;

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private UserRegisterTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CallbackManager callbackManager;
    private EditText mZipCodeView;
    private EditText mNameView;
    private DatePicker mDOBView;
    private String mDob;
    private ImageButton mConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        callbackManager = CallbackManager.Factory.create();
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        populateAutoComplete();

        mConfirm = (ImageButton) findViewById(R.id.register_confirmation_button);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptRegister();
            }
        });

        mPasswordView = (EditText) findViewById(R.id.register_password);
        mZipCodeView = (EditText) findViewById(R.id.register_zipCode);
        mNameView = (EditText) findViewById(R.id.register_name);
        mDOBView = (DatePicker) findViewById(R.id.register_dob);
        mDOBView.init(1990, 1, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDob = getDateFromDatePicker(mDOBView);
            }
        });

       // Calendar c = Calendar.getInstance();
        Date date = new Date();
        System.out.println("Date is "+date);
        mDOBView.setMaxDate(date.getTime());

        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        //region Facebook
        //facebookSDKInitialize();
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.register_facebook_button);
        //TODO:publishactions?
        loginButton.setReadPermissions("email,public_profile");
        getLoginDetails(loginButton);
        LoginWithPreviousFacebookConnection();

        //endregion
    }

    protected void getLoginDetails(LoginButton login_button){
        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result)
            {
                getUserInfo(login_result);
            }

            @Override
            public void onCancel() {
                // code for cancellation
            }

            @Override
            public void onError(FacebookException exception) {
                //  code to handle error
            }
        });
    }

    private void AttemptRegister()
    {
        //already trying to verify
        if (mAuthTask != null)
        {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mZipCodeView.setError(null);
        mNameView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String zipCode = mZipCodeView.getText().toString();
        String name = mNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if(TextUtils.isEmpty(mDob))
        {
            //TODO: implement error messasge for datepicker
            focusView = mDOBView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if(!isZipCodeValid(zipCode)){
            mZipCodeView.setError("This zip-code is invalid");
            focusView = mZipCodeView;
            cancel = true;
        }

        if(TextUtils.isEmpty(name)){
            mNameView.setError("The name is empty");
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserRegisterTask(email, password, zipCode, name, mDob);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isZipCodeValid(String zipCode) {
        return true;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void LoginWithPreviousFacebookConnection()
    {
        if(FacebookAccessTokenValid())
        {
            //connected to FB already, then just get user details from server
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response)
                        {
                            String email = "";
                            String name = "";
                            String id="";

                            try {
                                email = object.getString("email");
                                id  = object.getString("id");
                                name =  object.getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(Model.instance().loginUserThroughFacebook(id,email,name))
                            {
                                finish();
                                Intent boatManagement = new Intent(RegisterActivity.this, BoatManagementActivity.class);
                                startActivity(boatManagement);

                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    private boolean FacebookAccessTokenValid()
    {
        if(AccessToken.getCurrentAccessToken() != null)
        {
            if(AccessToken.getCurrentAccessToken().isExpired())
            {
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), RegisterActivity.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(RegisterActivity.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private static String getDateFromDatePicker(DatePicker datePicker)
    {
        String day = String.valueOf(datePicker.getDayOfMonth());
        String month = String.valueOf(datePicker.getMonth());
        String year =  String.valueOf(datePicker.getYear());

        return month + "\\" + day + "\\" + year;
    }

    /*
  To get the facebook user's own profile information via  creating a new request.
  When the request is completed, a callback is called to handle the success condition.
*/
    protected void getUserInfo(LoginResult login_result){

        GraphRequest data_request = GraphRequest.newMeRequest(
                login_result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response)
                    {

                        String email = "";
                        String name = "";
                        String id="";

                        try {
                            email = json_object.getString("email");
                            id  = json_object.getString("id");
                            name =  json_object.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(Model.instance().loginUserThroughFacebook(id,email,name))
                        {
                            finish();
                            Intent addABoat = new Intent(RegisterActivity.this, AddBoatActivity.class);
                            startActivity(addABoat);
                        }
                    }
                });

        //    Bundle permission_param = new Bundle();
        //  permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        //  data_request.setParameters(permission_param);

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        data_request.setParameters(parameters);
        data_request.executeAsync();
    }

    //Forward the login results to the callbackManager
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("data",data.toString());
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, User>
    {

        private final String mEmail;
        private final String mPassword;
        private final String mName;
        private final String mZipCode;
        private final String mDob;

        UserRegisterTask(String email, String password, String zipCode, String name, String dob) {
            mEmail = email;
            mPassword = password;
            mName = name;
            mZipCode = zipCode;
            mDob = dob;
        }

        @Override
        protected User doInBackground(Void... params)
        {
            User userFromServer = null;
            userFromServer = Model.instance().RegisterUser(mEmail,mPassword,mName,mZipCode,mDob);

            return userFromServer;
        }

        @Override
        protected void onPostExecute(final User user)
        {
            mAuthTask = null;
            showProgress(false);

            if (user != null)
            {
                finish();
                Intent addABoat = new Intent(RegisterActivity.this, AddBoatActivity.class);
                startActivity(addABoat);
            } else {
                mEmailView.setError(getString(R.string.error_incorrect_loginUser));
                mPasswordView.setError(getString(R.string.error_incorrect_loginUser));
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
