package com.dracoo.jobreport.feature;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.feature.action.ActionFragment;
import com.dracoo.jobreport.feature.connection.ConnectionFragment;
import com.dracoo.jobreport.feature.dashboard.DashboardFragment;
import com.dracoo.jobreport.feature.documentation.DocumentationFragment;
import com.dracoo.jobreport.feature.env.EnvironmentFragment;
import com.dracoo.jobreport.feature.machine.MachineFragment;
import com.dracoo.jobreport.feature.problem.ProblemFragment;
import com.dracoo.jobreport.feature.signature.SignatureFragment;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.google.android.material.navigation.NavigationView;

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
    public static final String EXTRA_CALLER_ACTIVITY = "job.extra_caller_flag";

    public static final int EXTRA_FLAG_DASH = 11;
    public static final int EXTRA_FLAG_PROBLEM = 12;
    public static final int EXTRA_FLAG_LIGHTNING = 13;
    public static final int EXTRA_FLAG_MACHINE = 14;
    public static final int EXTRA_FLAG_CONN = 15;
    public static final int EXTRA_FLAG_DOC = 16;
    public static final int EXTRA_FLAG_ACTION = 17;
    public static final int EXTRA_FLAG_SIGNATURE = 18;

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
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        try{
            String prefUn = preference.getTechName();
            String prefPhone = preference.getPhone();

            lblName.setText(prefUn.trim());
            lblPhone.setText("Phone " +prefPhone.trim());
        }catch (Exception e){ Log.d("suram",""+e.toString()); }

        setupToolbarNavDrawer();
        loadFragment();
    }

    @Override
    public void onStart(){
        super.onStart();
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
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
        String typeMenu;
        try{
            typeMenu = getIntent().getStringExtra(ConfigApps.EXTRA_CALLER_VIEW);
//            messageUtils.toastMessage("menu type " +typeMenu, ConfigApps.T_INFO);
        }catch (Exception e){
            typeMenu = ConfigApps.NEW_TYPE;
        }
        Bundle bundle = new Bundle();
        bundle.putString(ConfigApps.EXTRA_CALLER_VIEW, typeMenu);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (flagMenu) {
            case EXTRA_FLAG_DASH:
                if (getSupportActionBar() != null) { getSupportActionBar().setSubtitle("Dashboard"); }
                transaction.replace(R.id.frame_nav_container, new DashboardFragment());
                break;
            case EXTRA_FLAG_PROBLEM:
                if (getSupportActionBar() != null) { getSupportActionBar().setSubtitle("Problem Desc"); }
                ProblemFragment problemFragment = new ProblemFragment();
                transaction.replace(R.id.frame_nav_container, problemFragment);
                problemFragment.setArguments(bundle);
                break;
            case EXTRA_FLAG_LIGHTNING:
                if (getSupportActionBar() != null) { getSupportActionBar().setSubtitle("Electrical Environtment"); }
                EnvironmentFragment environmentFragment = new EnvironmentFragment();
                transaction.replace(R.id.frame_nav_container, environmentFragment);
                environmentFragment.setArguments(bundle);
                break;
            case EXTRA_FLAG_MACHINE:
                if (getSupportActionBar() != null) { getSupportActionBar().setSubtitle("Machine"); }
                MachineFragment machineFragment = new MachineFragment();
                transaction.replace(R.id.frame_nav_container, machineFragment);
                machineFragment.setArguments(bundle);
                break;
            case EXTRA_FLAG_CONN:
                if (getSupportActionBar() != null) { getSupportActionBar().setSubtitle("Connection"); }
                ConnectionFragment connectionFragment = new ConnectionFragment();
                transaction.replace(R.id.frame_nav_container, connectionFragment);
                connectionFragment.setArguments(bundle);
                break;
            case EXTRA_FLAG_DOC :
                if (getSupportActionBar() != null) { getSupportActionBar().setSubtitle("Documentation"); }
                transaction.replace(R.id.frame_nav_container, new DocumentationFragment());
                break;
            case EXTRA_FLAG_ACTION :
                if (getSupportActionBar() != null) { getSupportActionBar().setSubtitle("Action"); }
                transaction.replace(R.id.frame_nav_container, new ActionFragment());
                break;
            case EXTRA_FLAG_SIGNATURE :
                if (getSupportActionBar() != null) { getSupportActionBar().setSubtitle("Signature"); }
                transaction.replace(R.id.frame_nav_container, new SignatureFragment());
                break;
            default:
                break;
        }
        transaction.commit();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
                fragment = new EnvironmentFragment();
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
            case R.id.nav_signature :
                fragment = new SignatureFragment();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle("Signature");
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
