package zentech.myapplication.Entities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import zentech.myapplication.ClientSide.InstructionsStepOneTabFragment;
import zentech.myapplication.ClientSide.InstructionsStepThreeTabFragment;
import zentech.myapplication.ClientSide.InstructionsStepTwoTabFragment;

public class InstructionsTabsPagerAdapter extends FragmentPagerAdapter
{
    private static final int NUM_OF_TABS = 3;
    private final FragmentManager fragMan;
    private ArrayList<Fragment> fragments =new ArrayList<Fragment>();

    public InstructionsTabsPagerAdapter(FragmentManager fm)
    {
        super(fm);
        this.fragMan = fm;
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
                InstructionsStepOneTabFragment fragment = new InstructionsStepOneTabFragment();
                fragments.add(fragment);
                return fragment;
            }
            case 1:
            {
                InstructionsStepTwoTabFragment fragment = new InstructionsStepTwoTabFragment();
                fragments.add(fragment);
                return fragment;
            }
            case 2:
            {
                InstructionsStepThreeTabFragment fragment = new InstructionsStepThreeTabFragment();
                fragments.add(fragment);
                return fragment;
            }
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

    public interface InstructionsStepOneTabFragmentListener {
        void onNextPageSelected();
        void onStepThreeSelected();
    }
}

