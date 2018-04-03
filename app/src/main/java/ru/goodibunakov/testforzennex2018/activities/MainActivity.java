package ru.goodibunakov.testforzennex2018.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Locale;

import ru.goodibunakov.testforzennex2018.R;
import ru.goodibunakov.testforzennex2018.adapters.TabAdapter;
import ru.goodibunakov.testforzennex2018.fragments.MapFragment;
import ru.goodibunakov.testforzennex2018.fragments.ListFragment;
import ru.goodibunakov.testforzennex2018.fragments.JsonFragment;
import ru.goodibunakov.testforzennex2018.fragments.PhotoFragment;
import ru.goodibunakov.testforzennex2018.utils.PreferenceHelper;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ERROR = 0;
    PreferenceHelper preferenceHelper;
    public static final String LANG = "ru";
    Configuration config;
    private Locale locale = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString(LANG, "");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
        {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

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
    public void onBackPressed() {
        Toast.makeText(this, getResources().getText(R.string.cant_back), Toast.LENGTH_SHORT).show();
    }

    //создание главного меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //MenuItem menuItem = menu.findItem(R.id.lang);
        //menuItem.setChecked(preferenceHelper.getBoolean(PreferenceHelper.LANG));
        return super.onCreateOptionsMenu(menu);
    }

    //обработка нажатия пункта главного меню
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.lang){
            item.setChecked(!item.isChecked());
            preferenceHelper.putBoolean(PreferenceHelper.LANG, item.isChecked());
            config.locale = Locale.ENGLISH;
            preferenceHelper.putBoolean(PreferenceHelper.LANG, true);
        } else  config.locale = Locale.forLanguageTag(LANG);
        preferenceHelper.putBoolean(PreferenceHelper.LANG, false);
        getResources().updateConfiguration(config, null);
        return super.onOptionsItemSelected(item);
    }

    private void setUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        final ViewPager viewPager = findViewById(R.id.pager);

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new ListFragment(), getResources().getString(R.string.tab_title1));
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