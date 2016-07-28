package com.jyzn.iw.activity;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jyzn.iw.R;
import com.jyzn.iw.adapter.VehicleStatusAdapter;
import com.jyzn.iw.animator.SlideInOutLeftItemAnimator;
import com.jyzn.iw.entity.Coordinate;
import com.jyzn.iw.entity.VehicleStatus;
import com.jyzn.iw.view.FollowFingerView;
import com.jyzn.iw.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private long lastPressTime = 0;
    private SwipeRefreshLayout refreshLayout;
    private final List<VehicleStatus> vStatus = new ArrayList<>();
    private RecyclerView rView;
    private VehicleStatusAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final FollowFingerView fingerView = (FollowFingerView) findViewById(R.id.finger_view);
        fingerView.setOnPathCreatedListener(new FollowFingerView.OnPathCreatedListener() {

            @Override
            public void onPathCreated(List<PointF> points) {
                Log.i(DrawerActivity.this.getClass().getSimpleName(), "point size: " + points.size());
                fingerView.redrawPath();
            }
        });

        refreshLayout  = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA);
        refreshLayout.setOnRefreshListener(this);

        rView = (RecyclerView) findViewById(R.id.vehicle_list);
        rView.setLayoutManager(new LinearLayoutManager(this));
        SlideInOutLeftItemAnimator animator = new SlideInOutLeftItemAnimator(rView);
        animator.setAddDuration(1000);
        rView.setItemAnimator(animator);
        adapter = new VehicleStatusAdapter(vStatus);
        rView.setAdapter(adapter);
    }

    private VehicleStatus.WorkStatus generateWorkStatus() {
        int r = (int)(Math.random() * 6);
        switch (r) {
            case 0:
                return VehicleStatus.WorkStatus.IDLE;
            case 1:
                return VehicleStatus.WorkStatus.BREAK_DOWN;
            case 2:
                return VehicleStatus.WorkStatus.CHARGING;
            case 3:
                return VehicleStatus.WorkStatus.TAKING_SHELVES;
            case 4:
                return VehicleStatus.WorkStatus.TO_PICKING_TABLE;
            case 5:
                return VehicleStatus.WorkStatus.LINE_UP;
            case 6:
                return VehicleStatus.WorkStatus.BACK_SHELVES;
        }

        return VehicleStatus.WorkStatus.IDLE;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - lastPressTime <= 5000) {
                finish();
            } else {
                lastPressTime = System.currentTimeMillis();
                Toast toast = Toast.makeText(DrawerActivity.this, getString(R.string.exit_toast), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        Log.i(getClass().getSimpleName(), "onRefresh");

        if (vStatus.size() > 0) {
            adapter.removeItem();
            refreshLayout.setRefreshing(false);
        }
        else {
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 20; i ++) {
                        VehicleStatus status = new VehicleStatus();
                        status.ip = (int)(Math.random() * 255) + "." + (int)(Math.random() * 255) + "." +
                                (int)(Math.random() * 255) + "." +  (int)(Math.random() * 255);
                        status.workStatus = generateWorkStatus();
                        status.powerLeft = (int) (100 * Math.random());
                        status.coordinate = new Coordinate((int)(1000 * Math.random()), (int)(1000 * Math.random()), 1);
//                        vStatus.add(status);
                        adapter.addItem(status);
                    }
                    refreshLayout.setRefreshing(false);
                }
            }, 5000);
        }
    }
}
