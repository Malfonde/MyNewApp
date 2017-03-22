package zentech.myapplication.ClientSide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import zentech.myapplication.DAL.Model;
import zentech.myapplication.Entities.Boat;
import zentech.myapplication.Entities.LaunchBoatTabsPagerAdapter;
import zentech.myapplication.R;

public class LaunchBoatStepOneFragment extends Fragment
{

    private ImageButton dateTimeOk;
    private TimePicker timePicker;
    private String launchTime;
    private Boat selectedBoat;
    private String UserID;
    private String launchDate = Calendar.MONTH + "\\" + Calendar.DAY_OF_MONTH + "\\" + Calendar.YEAR;
    private OrderGetBoatTask mAuthTask = null;
    private View progressBar;
    private LaunchBoatTabsPagerAdapter.LaunchBoatTabsFragmentListener mListener;
    private String getBoatOrderID;
    private EditText moreServices;
    private CheckBox iceRequire;
   // private CheckBox fuelRequire;
    private Button moreServicesBtn;
    private LinearLayout fuelLayout;
    private EditText fuelAmount;
    private CheckBox fullFuel;
    private View wrappingLayout;
    private View scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_launch_boat_step_one, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        UserID = bundle.getString(BoatManagementActivity.USER_ID_KEY);
       // Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
       // ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        Object obj = bundle.getParcelable(BoatManagementActivity.BOAT_KEY);

        if (obj != null) {
            selectedBoat = (Boat) obj;
        }
        getBoatOrderID = bundle.getString(LaunchBoatTabsPagerAdapter.GET_BOAT_ORDER_ID);

        progressBar = view.findViewById(R.id.launch_boat_progress);
        dateTimeOk = (ImageButton) view.findViewById(R.id.dialog_date_time_ok_image_button);

        timePicker = (TimePicker) view.findViewById(R.id.dialog_date_time_timePicker);
        timePicker.setCurrentHour(Calendar.HOUR_OF_DAY);
        timePicker.setCurrentMinute(Calendar.MINUTE);
        timePicker.setOnTimeChangedListener( new TimePicker.OnTimeChangedListener()
        {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                launchTime = String.valueOf(hourOfDay).toString() + ":" + String.valueOf(minute).toString();
            }
        });

        dateTimeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                AttemptToSendOrder();

            }
        });


        scrollView = view.findViewById(R.id.launch_boat_wrapping_scrollview);
        wrappingLayout = view.findViewById(R.id.step_one_wrapping_layout);

        moreServicesBtn = (Button) view.findViewById(R.id.more_services_button);
        moreServicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                switch(moreServices.getVisibility())
                {
                    case View.VISIBLE :
                    {
                        moreServices.setVisibility(View.GONE);
                        moreServicesBtn.setText("Need more services?");
                        break;
                    }
                    case View.GONE:
                    {
                        moreServices.setVisibility(View.VISIBLE);
                        moreServicesBtn.setText("Press to cancel more services");
                        break;
                    }
                }
            }
        });

        moreServices = (EditText) view.findViewById(R.id.launch_boat_more_services);
        iceRequire = (CheckBox) view.findViewById(R.id.isIce);
       // fuelRequire = (CheckBox) view.findViewById(R.id.fuel_checkbox);
        fuelLayout = (LinearLayout) view.findViewById(R.id.fuelLayout);
        fuelAmount = (EditText) view.findViewById(R.id.fuel_amount);
        fullFuel = (CheckBox) view.findViewById(R.id.full_fuel_checkbox);
        /*fuelRequire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    fuelLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    fuelLayout.setVisibility(View.GONE);
                }
            }
        });*/
    }

    private void AttemptToSendOrder()
    {
        boolean isValid = true;
        View focusView = null;
        if (mAuthTask != null)
        {
            return;
        }

        double fuel = 0;

       /* if(fuelRequire.isChecked())
        {
            if(!fullFuel.isChecked())
            {
                if (fuelAmount.getText().toString() == null || fuelAmount.getText().toString().isEmpty())
                {
                    fuelAmount.setError("enter amount of galons or uncheck the fuel service");
                    focusView = fuelAmount;
                    isValid = false;
                }
                else
                {
                    try {
                        fuel = Double.parseDouble(fuelAmount.getText().toString());
                    }
                    catch (Exception e)
                    {

                        isValid = false;
                    }
                }
            }
        }*/

        String additionalServices = "";

        if(moreServices.getVisibility() == View.VISIBLE)
        {
            additionalServices = moreServices.getText().toString();
        }

        if(isValid)
        {
            showProgress(true);
            mAuthTask = new OrderGetBoatTask(selectedBoat.getObjectId(), selectedBoat.getMarineID(), fuel, iceRequire.isChecked(), additionalServices);
            mAuthTask.execute((Void) null);
        }
        else
        {
            focusView.requestFocus();
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof LaunchBoatTabsPagerAdapter.LaunchBoatTabsFragmentListener)
        {
            mListener = (LaunchBoatTabsPagerAdapter.LaunchBoatTabsFragmentListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LaunchBoatTabsPagerAdapter.LaunchBoatTabsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

            scrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            scrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scrollView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            scrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class OrderGetBoatTask extends AsyncTask<Void, Void, String>
    {
        private final double fuel;
        ArrayList<String> services = new ArrayList<>();
        private boolean iceRequested;
        private String selectedBoatID;
        private String selectedBoatMarineID;

        OrderGetBoatTask(String boatID, String marineID, double fuel, boolean ice, String moreServices)
        {
            selectedBoatID = boatID;
            selectedBoatMarineID = marineID;
            this.fuel = fuel;
            iceRequested = ice;

            //TODO: fix this pls.... its so ugly i cant look at it
            // pls pay attention that moreservices CAN be "", this is intentionally so services will be written to the db as an array.. and not as string..
            services.add(moreServices);
            //ugliness ends here..
        }

        @Override
        protected String doInBackground(Void... params)
        {
            if(getBoatOrderID != null && !getBoatOrderID.isEmpty())
            {
                String orderID = Model.instance().UpdateOrderGetBoatFromMarina(getBoatOrderID,launchDate, launchTime, selectedBoatID, UserID, selectedBoatMarineID, this.fuel, iceRequested, services);
                return orderID;
            }
            else
            {
                String orderID = Model.instance().OrderGetBoatFromMarina(launchDate, launchTime, selectedBoatID, UserID, selectedBoatMarineID, this.fuel,iceRequested, services);
                return orderID;
            }
        }

        @Override
        protected void onPostExecute(final String orderID)
        {
            mAuthTask = null;
            showProgress(false);

            if (orderID != null && !orderID.isEmpty())
            {
                //TODO: go to status page
                //Intent boatManagement = new Intent(LoginActivity.this, BoatManagementActivity.class);
                //startActivity(boatManagement);
                mListener.onNextPageSelected(orderID);
            }
            else {
               //TODO: alert the user to try again
                Snackbar.make(scrollView, "Try sending again, the request failed.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
