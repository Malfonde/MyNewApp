package zentech.myapplication.ClientSide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import java.util.Calendar;

import zentech.myapplication.DAL.Model;
import zentech.myapplication.R;


public class AddBoatActivity extends AppCompatActivity {

    private EditText mName;
    private CheckBox mIsForSale;
    private TimePicker mLastTreatedTimePicker;
    private DatePicker mLastTreatedDatePicker;
    private String lastTreatedDate;
    private ImageButton mOkButton;
    private EditText mWeight;
    private String lastTreatedTime;
    private View mFormView;
    private View mProgressView;
    private AddBoatTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_boat);

        mName = (EditText) findViewById(R.id.add_boat_name);
        mIsForSale = (CheckBox) findViewById(R.id.add_boat_is_for_sale);
        mLastTreatedTimePicker = (TimePicker) findViewById(R.id.add_boat_timePicker);
        mLastTreatedDatePicker = (DatePicker) findViewById(R.id.add_boat_datePicker);
        mOkButton = (ImageButton) findViewById(R.id.add_boat_ok_image_button);
        mWeight = (EditText) findViewById(R.id.add_boat_weight);


        mLastTreatedDatePicker.init(1990, 1, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                lastTreatedDate = getDateFromDatePicker(mLastTreatedDatePicker);
            }

            private String getDateFromDatePicker(DatePicker mLastTreatedDatePicker) {
                String day = String.valueOf(mLastTreatedDatePicker.getDayOfMonth());
                String month = String.valueOf(mLastTreatedDatePicker.getMonth());
                String year = String.valueOf(mLastTreatedDatePicker.getYear());

                return month + "\\" + day + "\\" + year;
            }
        });
        mLastTreatedDatePicker.setMaxDate(Calendar.DATE - 1000);

        mLastTreatedTimePicker.setCurrentHour(Calendar.HOUR_OF_DAY);
        mLastTreatedTimePicker.setCurrentMinute(Calendar.MINUTE);
        mLastTreatedTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                lastTreatedTime = hourOfDay + ":" + minute;
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    AttemptAddBoat();
            }
        });

        mFormView = findViewById(R.id.add_boat_wrapping_layout);
        mProgressView = findViewById(R.id.add_boat_progress);
        setTitle("Fill your boat\'s details");

    }

    private void AttemptAddBoat()
    {
        if(mAuthTask != null) {
            return;
        }


        String name = mName.getText().toString();
        String weight = mWeight.getText().toString();
        boolean isForSale = mIsForSale.isChecked();
        boolean isValid = true;
        double w = 0;
        mName.setError(null);
        mWeight.setError(null);
        mIsForSale.setError(null);


        if (name.isEmpty())
        {
            mName.setError(getString(R.string.error_field_required));
            isValid = false;
        }

        if(weight.isEmpty())
        {
            mWeight.setError(getString(R.string.error_field_required));
            isValid = false;
        }
        else
        {
            try
            {
                w = Double.parseDouble(weight);
            }
            catch (Exception e)
            {
                isValid = false;
                mWeight.setError(getString(R.string.error_field_only_numbers));
            }
        }

       if(isValid)
       {
           showProgress(true);
           mAuthTask = new AddBoatTask(name,w,isForSale);
           mAuthTask.execute((Void) null);
       }
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

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class AddBoatTask extends AsyncTask<Void, Void, Boolean>
    {
        private final String name;
        private final double weight;
        private final boolean isForSale;

        AddBoatTask(String name, double weight, boolean isForSale) {
            this.name = name;
            this.weight = weight;
            this.isForSale = isForSale;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Boolean success = Model.instance().addABoat(name, weight, isForSale, lastTreatedDate, lastTreatedTime);
            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask = null;
            showProgress(false);

            if (success)
            {
                finish();
                Intent boatManagement = new Intent(AddBoatActivity.this, BoatManagementActivity.class);
                startActivity(boatManagement);
            } else {
                mName.setError("Couldn't add the boat, try againe.");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

