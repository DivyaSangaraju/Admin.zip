package com.tripnetra.tnadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tripnetra.tnadmin.Admin_Management.Admin_mgnt;
import com.tripnetra.tnadmin.Analytics.select_reports;
import com.tripnetra.tnadmin.Notifications.select_notifctn;
import com.tripnetra.tnadmin.bookings.BookingMainFragment;
import com.tripnetra.tnadmin.bookings.Todo_list;
import com.tripnetra.tnadmin.bookings.marriage_requests;
import com.tripnetra.tnadmin.inventory.Car_Main_Invtry_Act;
import com.tripnetra.tnadmin.inventory.Hotel_Main_Invtry_Act;
import com.tripnetra.tnadmin.inventory.Tour_Main_Invtry_Act;
import com.tripnetra.tnadmin.logs.Cancel_Fragment;
import com.tripnetra.tnadmin.logs.LinkFragment;
import com.tripnetra.tnadmin.logs.LogDetailsFragment;
import com.tripnetra.tnadmin.payments.PaymentRepMainFragment;
import com.tripnetra.tnadmin.rest.VolleyRequester;
import com.tripnetra.tnadmin.utils.Change_ip_frag;
import com.tripnetra.tnadmin.utils.G46567;
import com.tripnetra.tnadmin.utils.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DashBoardActivity extends AppCompatActivity {

    Session session;
    NavigationView navigationView;
    DrawerLayout drawerLayt;
    Toolbar toolbar;
    CustomLoading cloading;
    static int navItemIndex = 0,Titleindex=0;
    static String CURRENT_TAG = "Inventory";
    String LoginID,UserType,UserName,UserId,desid,FcmToken="";
    String[] activityTitles;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        session = new Session(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LoginID = session.getULogid();
        String Userid = session.getUId();
        UserType = session.getUType();
        UserName = session.getUName();
        UserId  = session.getUId();
        desid=session.getdesigid();
        Toast.makeText(this, desid, Toast.LENGTH_SHORT).show();
        cloading = new CustomLoading(this);
        cloading.setCancelable(false);
        Objects.requireNonNull(cloading.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( DashBoardActivity.this, instanceIdResult -> {
            FcmToken = instanceIdResult.getToken();
            Log.e("newToken",FcmToken);

            //Toast.makeText(this, FcmToken, Toast.LENGTH_SHORT).show();

            inserttoken();
        });



        if (!UserName.equals("")) {

            G46567 g665 = (G46567) getApplicationContext();
            g665.setUserId(Userid);
            g665.setUserName(UserName);
            g665.setUserType(UserType);

            drawerLayt = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);


            navigationView.setItemIconTintList(null);
            View navHeader = navigationView.getHeaderView(0);

            activityTitles = new String[17];
            activityTitles[0] = "Inventory";
            activityTitles[1] = "Car Inventory";
            activityTitles[2] = "Payment Reports";
            activityTitles[3] = "Hotel Bookings";
            activityTitles[4] = "Car Bookings";
            activityTitles[5] = "Tour Bookings";
            activityTitles[6] = "Darshan Bookings";
            activityTitles[8] = "Log Details";
            activityTitles[7] = "Cancellations";

            activityTitles[9] = "Link";
            activityTitles[10] = "Bussiness Report";
            activityTitles[11] = "Tour Inventory";
            activityTitles[12] = "Admin List";
            activityTitles[13] = "Assign Notification";
            activityTitles[14] = "Marriage Requests";
            activityTitles[15] = "To Do List";
            activityTitles[16] = "Ip List";


        //    Log.i("FcmToken",FcmToken);

            ((TextView) navHeader.findViewById(R.id.nameTv)).setText(UserName);
            int ii = 0;
            if (LoginID.contains("_")) {
                ii = LoginID.indexOf("_") + 1;
            } else if (LoginID.contains(" ")) {
                ii = LoginID.indexOf(" ") + 1;
            }
            if (ii != 0) {
                ((TextView) navHeader.findViewById(R.id.nameshortTv)).setText(String.valueOf(LoginID.charAt(0)) + String.valueOf(LoginID.charAt(ii)));
            } else {
                ((TextView) navHeader.findViewById(R.id.nameshortTv)).setText(String.valueOf(LoginID.charAt(0)));
            }

            setUpNavigationView();

            if (savedInstanceState == null) {
                navItemIndex = 0;
                Titleindex = 0;
                CURRENT_TAG = activityTitles[0];
                loadHomeFragment();
            }


            else{
                //force_logout();
                Toast.makeText(DashBoardActivity.this, "Something Went Wrong Try Again", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void setUpNavigationView() {

        if(!(UserType.equals("Admin") ||(desid.equals("4")))) {
            navigationView.getMenu().findItem(R.id.pricereps).setVisible(false);//Hide Payment Reports
            navigationView.getMenu().findItem(R.id.logdata).setVisible(false);//Hide LogData Reports
            navigationView.getMenu().findItem(R.id.cancels).setVisible(false);//Hide Cancellations
            navigationView.getMenu().findItem(R.id.analytics).setVisible(false); //Hide Business Reports
            navigationView.getMenu().findItem(R.id.adminlist).setVisible(false); //Hide Admin Reports
            navigationView.getMenu().findItem(R.id.iplist).setVisible(false); //Hide IP Reports
           // navigationView.getMenu().findItem(R.id.marriage).setVisible(false); //Hide Marriage Reports
        }


        if(!(UserId.equals("41"))){
            navigationView.getMenu().findItem(R.id.marriage).setVisible(false);//visible to only sir/mam--SuperAdmins
            navigationView.getMenu().findItem(R.id.todolist).setVisible(false);///visible to only sir/mam--SuperAdmins
        }


        navigationView.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.nav_inventory:
                    navItemIndex = 0;
                    Titleindex=0;
                    CURRENT_TAG = activityTitles[0];
                    break;
                case R.id.Carinv:
                    navItemIndex = 1;
                    Titleindex=1;
                    CURRENT_TAG = activityTitles[1];
                    break;
                case R.id.pricereps:
                    navItemIndex = 2;
                    Titleindex=2;
                    CURRENT_TAG = activityTitles[2];
                    break;

                case R.id.hotelbookings:
                    navItemIndex = 3;
                    Titleindex=3;
                    CURRENT_TAG = activityTitles[3];
                    break;
                case R.id.carbookings:
                    navItemIndex = 4;
                    Titleindex=4;
                    CURRENT_TAG = activityTitles[4];
                    break;
                case R.id.tourbookings:
                    navItemIndex = 5;
                    Titleindex=5;
                    CURRENT_TAG = activityTitles[5];
                    break;
                case R.id.darbookings:
                    navItemIndex = 6;
                    Titleindex=6;
                    CURRENT_TAG = activityTitles[6];
                    break;
                case R.id.cancels:
                    navItemIndex = 7;
                    Titleindex=7;
                    CURRENT_TAG = activityTitles[7];
                    break;
                case R.id.logdata:
                    navItemIndex = 8;
                    Titleindex=8;
                    CURRENT_TAG = activityTitles[8];
                    break;

                case R.id.link:
                    navItemIndex = 9;
                    Titleindex=9;
                    CURRENT_TAG = activityTitles[9];
                    break;

                case R.id.analytics:
                    navItemIndex = 10;
                    Titleindex=10;
                    CURRENT_TAG = activityTitles[10];
                    break;

                case R.id.Tourinv:
                    navItemIndex = 11;
                    Titleindex=11;
                    CURRENT_TAG = activityTitles[11];
                    break;

                case R.id.adminlist:
                    navItemIndex = 12;
                    Titleindex=12;
                    CURRENT_TAG = activityTitles[12];
                    break;

              /*  case R.id.notification:
                navItemIndex = 13;
                Titleindex=13;
                CURRENT_TAG = activityTitles[13];
                break;*/

                case R.id.marriage:
                navItemIndex = 14;
                Titleindex=14;
                CURRENT_TAG = activityTitles[14];
                break;

                case R.id.todolist:
                navItemIndex = 15;
                Titleindex=15;
                CURRENT_TAG = activityTitles[15];
                break;

                case R.id.iplist:
                    navItemIndex = 16;
                    Titleindex=16;
                    CURRENT_TAG = activityTitles[16];
                    break;

                default:
                    navItemIndex = 0;
                    Titleindex=0;
                    CURRENT_TAG = activityTitles[0];
                    break;
            }

            menuItem.setChecked(true);

            loadHomeFragment();

            return true;
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayt, toolbar, R.string.app_name, R.string.dummy_content) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayt.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void loadHomeFragment() {


        assert getSupportActionBar()!=null;
        getSupportActionBar().setTitle(activityTitles[Titleindex]);

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) { drawerLayt.closeDrawers();return; }

        new Handler().post(() -> {
            Fragment fragment = getHomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        });

        drawerLayt.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                return new Hotel_Main_Invtry_Act();
            case 1:
                return new Car_Main_Invtry_Act();
            case 2:
                return new PaymentRepMainFragment();
            case 3: case 4: case 5: case 6:
                return new BookingMainFragment();
            case 7:
                return new Cancel_Fragment();
            case 8:
                return new LogDetailsFragment();
            case 9:
                return new LinkFragment();
            case 10:
                return new select_reports();
            case 11:
                return new Tour_Main_Invtry_Act();

            case 12:
                return new Admin_mgnt();

            case 13:
                return new select_notifctn();

            case 14:
                return new marriage_requests();

            case 15:
                return new Todo_list();

            case 16:
                return new Change_ip_frag();
            default:
                return new Hotel_Main_Invtry_Act();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { getMenuInflater().inflate(R.menu.main, menu);return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        new AlertDialog.Builder(DashBoardActivity.this).setMessage("Do you Want to Logout")
                .setPositiveButton("Yes", (dialog, id) -> clearSharedprefs())
                .setNegativeButton("No", (dialog, id) -> {})
                .setCancelable(true).create().show();
    }

    private void force_logout() {
        new AlertDialog.Builder(DashBoardActivity.this).setMessage("Something Wrong\n\nPlease Login Again")
                .setPositiveButton("Ok", (dialog, id) -> clearSharedprefs())
                .setCancelable(false).create().show();
    }

    public void clearSharedprefs(){
        session.setLoggedin(false);
        session.ClearAll();

        Intent intent = new Intent(DashBoardActivity.this, LoginMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit)
        {finish(); }
        else {
            Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 3 * 1000);
        }
    }

    public void inserttoken(){

        cloading.show();

        Map<String, String> params = new HashMap<>();
        params.put("fcm_token", FcmToken);
        params.put("id", UserId);

       //Log.i("ps", String.valueOf(params));
        Log.i("rs", String.valueOf(params));
        new VolleyRequester(this).ParamsRequest(1, "https://tripnetra.com/androidphpfiles/test/adminpanel/ins.php", cloading, params, false, response -> {
            cloading.dismiss();
           // Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

        });
    }

}