package zentech.myapplication.Entities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

import zentech.myapplication.ClientSide.BoatManagementActivity;
import zentech.myapplication.ClientSide.LaunchBoatStepOneFragment;

/**
 * Created by ad05n on 3/3/2017.
 */
public class LaunchBoatTabsPagerAdapter extends FragmentPagerAdapter
{
    private static final int NUM_OF_TABS = 1;
    private final FragmentManager fragMan;
    private final Boat selectedBoat;
    private final String orderingUserID;
    private ArrayList<Fragment> fragments =new ArrayList<Fragment>();
    private String getBoatOrderID;
    public static String GET_BOAT_ORDER_ID = "getBoatOrderID";

    public LaunchBoatTabsPagerAdapter(FragmentManager supportFragmentManager, Boat selectedBoat, String orderingUserID)
    {
        super(supportFragmentManager);
        fragMan = supportFragmentManager;
        this.selectedBoat = selectedBoat;
        this.orderingUserID = orderingUserID;
    }

    public void setOrderID(String objID)
    {
        getBoatOrderID = objID;
    }

    public void ClearAll()
    {
        for (int i = 0; i < fragments.size(); i++) {
            fragMan.beginTransaction().remove(fragments.get(i)).commit();
        }

        fragments.clear();
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
            {
                LaunchBoatStepOneFragment fragment = new LaunchBoatStepOneFragment();
                Bundle b = new Bundle();
                b.putParcelable(BoatManagementActivity.BOAT_KEY, selectedBoat);
                b.putString(BoatManagementActivity.USER_ID_KEY, orderingUserID);
                fragment.setArguments(b);
                fragments.add(fragment);
                return fragment;
            }
            /*case 1:
            {
                LaunchBoatStepTwoFragment fragment = new LaunchBoatStepTwoFragment();
                Bundle b = new Bundle();
                b.putParcelable(BoatManagementActivity.BOAT_KEY, selectedBoat);
                b.putString(BoatManagementActivity.USER_ID_KEY, orderingUserID);
                b.putString(GET_BOAT_ORDER_ID,getBoatOrderID);
                fragment.setArguments(b);
                fragments.add(fragment);
                return fragment;
            }*/
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    @Override
    public int getCount()
    {
        return NUM_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
            {
                return "Step One";
            }
            case 1:
            {
                return "Step Two";
            }
            case 2:
            {
                return "Step Three";
            }
            default:
                return null;
        }
    }

    public interface LaunchBoatTabsFragmentListener {
        void onNextPageSelected(String getOrderID);
        void onFinalStepSelected();
    }


}
