package com.lq.beauty.app.view.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
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
import com.lq.beauty.app.RxBus.RxBus;
import com.lq.beauty.app.RxBus.RxBusEvent;
import com.lq.beauty.app.SettingManager;
import com.lq.beauty.app.base.BaseBeautyActivity;
import com.lq.beauty.app.camera.render.CameraRender;
import com.lq.beauty.app.view.main.widget.CameraRatioTransformView;
import com.lq.beauty.app.view.main.widget.RecordButton;
import com.lq.beauty.app.view.videoList.VideoListActivity;
import com.lq.beauty.base.cache.CacheManager;
import com.lq.beauty.base.opengl.WGLSurfaceView;
import com.lq.beauty.base.utils.UIHelper;
import com.lq.beauty.base.utils.ViewTransform;
import com.lq.beauty.base.widget.BaseRecyclerView;
import com.mikepenz.iconics.view.IconicsButton;

import java.util.Set;

import butterknife.*;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseBeautyActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

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

    @BindView(R.id.CameraRatioTransformView)
    protected CameraRatioTransformView mCameraRatioTransformView;

    @OnClick(R.id.mainBtnRecord) void onRecordClicked() {
        mBtnRecord.start();
        isRecording = !isRecording;
//        cameraRender.changeRecordingState(isRecording);
    }

    @OnClick(R.id.mainBtnHistory) void onHistoryClicked() {
        VideoListActivity.show(MainActivity.this);
    }

    protected boolean isRecording;
    protected CameraRender cameraRender;
    protected MainMenu mainMenu;

    @Override
    protected int getContentView() { return R.layout.act_main; }

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
                mCameraRatioTransformView.setCameraRatio(SettingManager.getInstance().getCameraRatio());
                ViewTransform.setViewHeight(mWGLSurfaceView, mCameraRatioTransformView.getHeight());
                ViewTransform.setViewWidth(mWGLSurfaceView, mCameraRatioTransformView.getWidth());
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

        RxBus.getInstance().toObservable(RxBusEvent.class)
                .subscribe(new Consumer<RxBusEvent>() {
                    @Override
                    public void accept(RxBusEvent evt) throws Exception {
                        switch (evt.getId()) {
                            case 1:
                                break;
                            case 2:
                            {
//                                cameraRender.setCurrentCameraRatio(SettingManager.getInstance().getCameraRatio());
                            }
                                break;
                            case 3:
                                break;
                        }
                    }
                });


        RxBus.getInstance().toObservable(CameraRatioTransformView.RxBusEventChangeCameraRatio.class)
                .subscribe(new Consumer<CameraRatioTransformView.RxBusEventChangeCameraRatio>() {
                    @Override
                    public void accept(CameraRatioTransformView.RxBusEventChangeCameraRatio evt) throws Exception {
                        ViewTransform.setViewHeight(
                                mWGLSurfaceView,
                                SettingManager.getVideoCameraRatioHeight(
                                        SettingManager.getInstance().getCameraRatio(),
                                        mCameraRatioTransformView.getHeight()));

                        ViewTransform.setViewWidth(
                                mWGLSurfaceView,
                                SettingManager.getVideoCameraRatioWidth(
                                        SettingManager.getInstance().getCameraRatio(),
                                        mCameraRatioTransformView.getWidth()));
                    }
                });
    }

    @Override
    protected void initData() {
        isRecording = false;
        CacheManager.getInstance().initCacheDir();
        mainMenu.initData();
        mCameraRatioTransformView.setCameraRatio(SettingManager.getInstance().getCameraRatio());
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
