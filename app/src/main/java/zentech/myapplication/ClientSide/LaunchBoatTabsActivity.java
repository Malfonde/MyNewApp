package zentech.myapplication.ClientSide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import zentech.myapplication.Entities.Boat;
import zentech.myapplication.Entities.LaunchBoatTabsPagerAdapter;
import zentech.myapplication.R;


public class LaunchBoatTabsActivity extends AppCompatActivity implements LaunchBoatTabsPagerAdapter.LaunchBoatTabsFragmentListener
{
    private final String TITLE = "Launch your boat";
    private LaunchBoatTabsPagerAdapter tabsPagerAdapter;
    private ViewPager pager;
    private Boat selectedBoat;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_boat_tabs);
        setTitle(TITLE);
        pager =  (ViewPager) findViewById(R.id.activity_launch_boat_process_tabs_layout).findViewById(R.id.launch_boat_VP);
        Bundle bundle = getIntent().getExtras();
        Object obj = bundle.getParcelable(BoatManagementActivity.BOAT_KEY);

        if (obj != null) {
            selectedBoat = (Boat) obj;
        }
        userID = bundle.getString(BoatManagementActivity.USER_ID_KEY);

        tabsPagerAdapter = new LaunchBoatTabsPagerAdapter(getSupportFragmentManager(),selectedBoat,userID);
        pager.setAdapter(tabsPagerAdapter);

    }

    @Override
    public void onNextPageSelected(String orderID)
    {
        pager.setCurrentItem(pager.getCurrentItem() +1);
        ((LaunchBoatTabsPagerAdapter)pager.getAdapter()).setOrderID(orderID);
    }

    @Override
    public void onFinalStepSelected()
    {
        finish();
        Intent login = new Intent(LaunchBoatTabsActivity.this, LoginActivity.class);
        startActivity(login);
    }
}
