package ru.goodibunakov.testforzennex2018.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import ru.goodibunakov.testforzennex2018.R;
import ru.goodibunakov.testforzennex2018.adapters.TabAdapter;
import ru.goodibunakov.testforzennex2018.fragments.MapFragment;
import ru.goodibunakov.testforzennex2018.fragments.OneFragment;
import ru.goodibunakov.testforzennex2018.fragments.JsonFragment;
import ru.goodibunakov.testforzennex2018.fragments.PhotoFragment;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ERROR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //проверка доступности play services
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS){
            Dialog errorDialog = apiAvailability.getErrorDialog(this, errorCode, REQUEST_ERROR,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //выйти если сервис не доступен
                            finish();
                        }
                    });
            errorDialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getResources().getText(R.string.cant_back), Toast.LENGTH_SHORT).show();
    }

    private void setUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3));

        final ViewPager viewPager = findViewById(R.id.pager);

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), getResources().getString(R.string.tab_title1));
        adapter.addFragment(new PhotoFragment(), getResources().getString(R.string.tab_title2));
        adapter.addFragment(new JsonFragment(), getResources().getString(R.string.tab_title3));
        adapter.addFragment(new MapFragment(), getResources().getString(R.string.tab_title4));
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.format_list_bulleted);
        tabLayout.getTabAt(1).setIcon(R.drawable.image);
        tabLayout.getTabAt(2).setIcon(R.drawable.web);
        tabLayout.getTabAt(3).setIcon(R.drawable.google_maps);
    }
}