package com.dracoo.jobreport.feature;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    TextView lblName, lblPhone;
    boolean doubleBackToExitPressedOnce = false;

    private Preference preference;
    private MessageUtils messageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        View header = navigationView.getHeaderView(0);
        setupToolbarNavDrawer();

        lblName = (TextView) header.findViewById(R.id.lbl_header_name);
        lblPhone = (TextView) header.findViewById(R.id.lbl_header_phone);
    }

    @Override
    protected void onStart(){
        super.onStart();
        preference = new Preference(MenuActivity.this);
        messageUtils = new MessageUtils(MenuActivity.this);
        try{
            String prefUn = preference.getUn();
            String prefPhone = preference.getPhone();

            if (!prefUn.equals("") || !prefPhone.equals("")){
                lblName.setText(prefUn.trim());
                lblPhone.setText(prefPhone.trim());
            }
        }catch (Exception e){}
    }

    private void setupToolbarNavDrawer(){
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Job Report");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        final Menu menu = navigationView.getMenu();

        menu.add(Menu.NONE, R.id.nav_home, Menu.NONE, ConfigApps.HOME);
        menu.findItem(R.id.nav_home).setIcon(R.drawable.nav_home);
        menu.add(Menu.NONE, R.id.nav_problem, Menu.NONE, ConfigApps.PROBLEM_DESC);
        menu.findItem(R.id.nav_problem).setIcon(R.drawable.nav_information);
        menu.add(Menu.NONE, R.id.nav_lightning, Menu.NONE, ConfigApps.LIGHTNING);
        menu.findItem(R.id.nav_lightning).setIcon(R.drawable.ic_lightning);
        menu.add(Menu.NONE, R.id.nav_machine, Menu.NONE, ConfigApps.MACHINE);
        menu.findItem(R.id.nav_machine).setIcon(R.drawable.ic_setting);
        menu.add(Menu.NONE, R.id.nav_connection, Menu.NONE, ConfigApps.CONNECTION);
        menu.findItem(R.id.nav_connection).setIcon(R.drawable.ic_tower);
        menu.add(Menu.NONE, R.id.nav_doc, Menu.NONE, ConfigApps.DOC);
        menu.findItem(R.id.nav_doc).setIcon(R.drawable.ic_doc);
        menu.add(Menu.NONE, R.id.nav_logout, Menu.NONE, ConfigApps.LOGOUT);
        menu.findItem(R.id.nav_logout).setIcon(R.drawable.ic_logout);

        navigationView.setItemIconTintList(null);

    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                super.onBackPressed();

                return;
            }
            this.doubleBackToExitPressedOnce = true;
            messageUtils.toastMessage("Tekan 'Back' sekali lagi untuk keluar", ConfigApps.T_INFO);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
//            // Handle the camera action
        } else if (id == R.id.nav_problem) {
//
        }
// else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
