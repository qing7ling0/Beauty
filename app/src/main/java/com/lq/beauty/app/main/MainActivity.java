package com.lq.beauty.app.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lq.beauty.R;
import com.lq.beauty.app.base.BaseBeautyActivity;
import com.lq.beauty.app.camera.render.CameraRender;
import com.lq.beauty.app.main.widget.RecordButton;
import com.lq.beauty.app.videoList.VideoListActivity;
import com.lq.beauty.base.cache.CacheManager;
import com.lq.beauty.base.opengl.WGLSurfaceView;
import com.lq.beauty.base.widget.BaseRecyclerView;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.iconics.view.IconicsButton;

import butterknife.*;

public class MainActivity extends BaseBeautyActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @BindView(R.id.CameraSurfaceView)
    protected WGLSurfaceView mWGLSurfaceView;

    @BindView(R.id.main)
    protected CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.mainEffectTypeName)
    protected TextView mEffectTypeName;

    @BindView(R.id.mainBtnStore)
    protected IconicsButton mBtnStore;

    @BindView(R.id.mainBtnRecord)
    protected RecordButton mBtnRecord;

    @OnClick(R.id.mainBtnRecord) void onRecordClicked() {
        mBtnRecord.animateCheckedState();
        isRecording = !isRecording;
        cameraRender.changeRecordingState(isRecording);
    }

    @OnClick(R.id.mainBtnHistory) void onHistoryClicked() {
        Intent intent = new Intent(MainActivity.this, VideoListActivity.class);
        MainActivity.this.startActivity(intent);
    }

    protected boolean isRecording;
    protected CameraRender cameraRender;
    protected MainMenu mainMenu;

    @Override
    protected int getContentView() { return R.layout.act_main; }

    @Override
    protected void onLayoutInflater() {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mToolbar.setTitle("Hello");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.side_nav_bar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mCoordinatorLayout.setTranslationX((float) (drawerView.getWidth()*Math.pow(slideOffset,1.2d)));
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();

        if (PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CAMERA }, 1);
        }
        cameraRender = new CameraRender();
        mWGLSurfaceView.setWRenderer(cameraRender);

        mainMenu = new MainMenu(this, (BaseRecyclerView) findViewById(R.id.brvMenuList));
        mainMenu.initWidget();
    }

    @Override
    protected void initData() {
        isRecording = false;
        CacheManager.getInstance().initCacheDir();
        mainMenu.initData();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
