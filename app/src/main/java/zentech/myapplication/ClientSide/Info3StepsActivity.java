package zentech.myapplication.ClientSide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import zentech.myapplication.Entities.InstructionsTabsPagerAdapter;
import zentech.myapplication.R;

public class Info3StepsActivity extends AppCompatActivity implements InstructionsTabsPagerAdapter.InstructionsStepOneTabFragmentListener
{
    private InstructionsTabsPagerAdapter instructionsTabsPagerAdapter;
    private final String TITLE = "Instructions";
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_3_steps);
        setTitle(TITLE);
        pager =  (ViewPager) findViewById(R.id.info_steps_layout).findViewById(R.id.instructs_VP);
        instructionsTabsPagerAdapter = new InstructionsTabsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(instructionsTabsPagerAdapter);
    }

    @Override
    public void onNextPageSelected()
    {
        pager.setCurrentItem(pager.getCurrentItem() +1);
    }

    @Override
    public void onStepThreeSelected()
    {
        finish();
        Intent login = new Intent(Info3StepsActivity.this, LoginActivity.class);
        startActivity(login);
    }
}
