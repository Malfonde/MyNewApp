package zentech.myapplication.ClientSide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;

import zentech.myapplication.DAL.Model;
import zentech.myapplication.Entities.Boat;
import zentech.myapplication.R;

public class BoatManagementActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String USER_ID_KEY = "orederingUserID";
    private ImageButton launchBoatImageButton;
    private ArrayList<Boat> boats;
    private String userID;
    private android.support.v7.widget.Toolbar appBarLayout;
    private Boat selectedBoat;
    public static String BOAT_KEY = "selectedBoat";
    private View mProgressView;
    private View mBoatManagementFromView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat_management);

        Toolbar toolbar = (Toolbar) findViewById(R.id.boatManagementToolBar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mProgressView = findViewById(R.id.boat_management_progress);
        mBoatManagementFromView = findViewById(R .id.app_bar_boat_management_ContentLayout);
        userID = Model.instance().getCurrentUser().getObjectId();

        showProgress(true);
        boats = Model.instance().getUserBoatsByID(userID);

        if(boats == null)
        {
            //user has no boats!?@#!?@#?!@ boats = Model.instance().getUserBoatsByID(userID);
        }

        showProgress(false);

        selectedBoat = boats.get(0);

        getSupportActionBar().setTitle(selectedBoat.getName());

        launchBoatImageButton = (ImageButton) findViewById(R.id.launchImageButton);

        if(selectedBoat.getMarineID() == null || selectedBoat.getMarineID().isEmpty())
        {
            //user is not belong to a marine, but he has a boat..
            launchBoatImageButton.setBackgroundResource(R.drawable.find_marine);
            launchBoatImageButton.setTag(R.drawable.find_marine);
        }
        else
        {
            if (selectedBoat.isDocked() || selectedBoat.isRacked()) {
                // set the launch image to be MarineSearch
                launchBoatImageButton.setBackgroundResource(R.drawable.launch_boat_image);
                launchBoatImageButton.setTag(R.drawable.launch_boat_image);

            } else {
                launchBoatImageButton.setBackgroundResource(R.drawable.return_boat_to_marine);
                launchBoatImageButton.setTag(R.drawable.return_boat_to_marine);
            }
        }

        launchBoatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //open the dialog : date and time picker.
                switch ((int)launchBoatImageButton.getTag())
                {
                    case R.drawable.find_marine:
                    {
                        Intent marinesSearch = new Intent(BoatManagementActivity.this, MarineSearchItemListActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelable(BOAT_KEY,selectedBoat);
                        b.putString(USER_ID_KEY,userID);
                        marinesSearch.putExtras(b);
                        startActivity(marinesSearch);
                        break;
                    }
                    case R.drawable.launch_boat_image:
                    {
                        Intent marinesSearch = new Intent(BoatManagementActivity.this, LaunchBoatTabsActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelable(BOAT_KEY,selectedBoat);
                        b.putString(USER_ID_KEY,userID);
                        marinesSearch.putExtras(b);
                        startActivity(marinesSearch);
                        break;
                    }
                    case R.drawable.return_boat_to_marine:
                    {
                        /*Intent marinesSearch = new Intent(BoatManagementActivity.this, ReturnBoatActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelable(BOAT_KEY,selectedBoat);
                        b.putString(USER_ID_KEY,userID);
                        marinesSearch.putExtras(b);
                        startActivity(marinesSearch);
                        break;*/
                    }
                    default:
                    {
                        //shouldnt reach here tbh..
                    }
                }

                    //handle searching marine redirect


            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.boat_management_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mBoatManagementFromView.setVisibility(show ? View.GONE : View.VISIBLE);
            mBoatManagementFromView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBoatManagementFromView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mBoatManagementFromView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
