package ru.goodibunakov.testforzennex2018;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import ru.goodibunakov.testforzennex2018.fragments.FourFragment;
import ru.goodibunakov.testforzennex2018.fragments.OneFragment;
import ru.goodibunakov.testforzennex2018.fragments.ThreeFragment;
import ru.goodibunakov.testforzennex2018.fragments.TwoFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUI();
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
        adapter.addFragment(new OneFragment(), "People");
        adapter.addFragment(new TwoFragment(), "Group");
        adapter.addFragment(new ThreeFragment(), "Calls");
        adapter.addFragment(new FourFragment(), "4");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.format_list_bulleted);
        tabLayout.getTabAt(1).setIcon(R.drawable.image);
        tabLayout.getTabAt(2).setIcon(R.drawable.web);
        tabLayout.getTabAt(3).setIcon(R.drawable.google_maps);
    }
}