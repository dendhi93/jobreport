package com.dracoo.jobreport.feature;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.feature.action.ActionFragment;
import com.dracoo.jobreport.feature.connection.ConnectionFragment;
import com.dracoo.jobreport.feature.dashboard.DashboardFragment;
import com.dracoo.jobreport.feature.documentation.DocumentationFragment;
import com.dracoo.jobreport.feature.env.EnvirontmentFragment;
import com.dracoo.jobreport.feature.machine.MachineFragment;
import com.dracoo.jobreport.feature.problem.ProblemFragment;
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
    public static int navItemIndex = 0;

    public static final String EXTRA_CALLER_ACTIVITY = "job.extra_caller_flag";

    public static final int EXTRA_FLAG_DASH = 11;
    public static final int EXTRA_FLAG_PROBLEM = 12;
    public static final int EXTRA_FLAG_LIGHTNING = 13;
    public static final int EXTRA_FLAG_MACHINE_ = 14;
    public static final int EXTRA_FLAG_DOC = 15;
    public static final int EXTRA_FLAG_ACTION = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        View header = navigationView.getHeaderView(0);
        setupToolbarNavDrawer();

        lblName = header.findViewById(R.id.lbl_header_name);
        lblPhone = header.findViewById(R.id.lbl_header_phone);
        preference = new Preference(MenuActivity.this);
        messageUtils = new MessageUtils(MenuActivity.this);
        try{
            String prefUn = preference.getTechName();
            String prefPhone = preference.getPhone();

            lblName.setText(prefUn.trim());
            lblPhone.setText("Phone " +prefPhone.trim());

        }catch (Exception e){
            Log.d("suram",""+e.toString());
        }

        setupToolbarNavDrawer();
        loadFragment();

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

    private void loadFragment(){
        int flagMenu = getIntent().getIntExtra(EXTRA_CALLER_ACTIVITY, 0);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (flagMenu) {
            case EXTRA_FLAG_DASH:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Dashboard");

                }
                transaction.replace(R.id.frame_nav_container, new DashboardFragment());
                break;
            case EXTRA_FLAG_PROBLEM:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Problem Desc");

                }
                transaction.replace(R.id.frame_nav_container, new ProblemFragment());
                break;
            case EXTRA_FLAG_LIGHTNING:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Electrical Environtment");

                }
                transaction.replace(R.id.frame_nav_container, new EnvirontmentFragment());
                break;
            case EXTRA_FLAG_MACHINE_:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Machine");

                }
                transaction.replace(R.id.frame_nav_container, new MachineFragment());
                break;
            case EXTRA_FLAG_DOC :
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Documentation");

                }
                transaction.replace(R.id.frame_nav_container, new DocumentationFragment());
                break;
            case EXTRA_FLAG_ACTION :
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Action");

                }
                transaction.replace(R.id.frame_nav_container, new ActionFragment());
                break;
            default:
                break;
        }
        transaction.commit();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home :
                fragment = new DashboardFragment();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Dashboard");
                }
                break;
            case R.id.nav_problem :
                fragment = new ProblemFragment();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Problem Desc");
                }
                break;
            case R.id.nav_lightning :
                fragment = new EnvirontmentFragment();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Electrical Environtment");
                }
                break;
            case R.id.nav_machine :
                fragment = new MachineFragment();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Machine");
                }
                break;
            case R.id.nav_connection :
                fragment = new ConnectionFragment();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Connection");
                }
                break;
            case R.id.nav_doc :
                fragment = new DocumentationFragment();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Documentation");
                }
                break;
            case R.id.nav_action :
                fragment = new ActionFragment();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Action");
                }
                break;
            case R.id.nav_logout :
                new AlertDialog.Builder(MenuActivity.this, R.style.AlertDialog)
                        .setTitle("Warning")
                        .setMessage("Mohon selesaikan pekerjaan yang masih tertunda, apakah Anda yakin ingin keluar ?")
                        .setIcon(R.drawable.ic_logo)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;

        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_nav_container, fragment).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
