package com.lq.beauty.app.videoList;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.lq.beauty.R;
import com.lq.beauty.app.base.BaseBeautyActivity;
import com.lq.beauty.app.videoList.adapter.VideoListAdapter;
import com.lq.beauty.app.videoList.data.VideoListItemData;
import com.lq.beauty.base.utils.FileUtil;
import com.lq.beauty.base.utils.StringUtils;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class VideoListActivity extends BaseBeautyActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rvVideoList)
    protected RecyclerView rvVideoList;

    private VideoListAdapter videoListAdapter;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected int getContentView() { return R.layout.act_video_list; }

    @Override
    protected void onLayoutInflater() {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
    }

    @Override
    protected void initWidget() {
        videoListAdapter = new VideoListAdapter(this);
        rvVideoList.setLayoutManager(new GridLayoutManager(this, 3));
        rvVideoList.setAdapter(videoListAdapter);
    }

    @Override
    protected void initData() {
        disposables.add(videoListObservable()
                .map(new Function<String, VideoListItemData>() {
                    @Override
                    public VideoListItemData apply(String string) throws Exception {
                        VideoListItemData data = new VideoListItemData();
                        data.setVideoPath(string);

                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        try {
                            mmr.setDataSource(string);
                            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
//                            String bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

                            int dur = StringUtils.toInt(duration, 0);
                            if (dur > 3600000)
                                data.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date(dur)));
                            else
                                data.setTime(new SimpleDateFormat("mm:ss").format(new Date(dur)));

                            data.setName(title);
                            Bitmap bitmap = mmr.getFrameAtTime();
                            data.setBitmap(bitmap);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            data.setTime("00:00");
                            data.setName("");
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                            data.setTime("00:00");
                            data.setName("");
                        }
                        mmr.release();
                        SystemClock.sleep(100);
                        return data;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VideoListItemData>() {
                    @Override
                    public void accept(VideoListItemData videoListItemData) throws Exception {
                        videoListAdapter.addItem(0,videoListItemData);
                    }
                }));
    }

    Observable<String> videoListObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override public ObservableSource<? extends String> call() throws Exception {
                // Do some long running operation
                List<String> list = new ArrayList<String>();
                FileUtil.getFileList(list, new File(FileUtil.getSDRoot()), new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return TextUtils.equals("mp4", FileUtil.getFileFormat(pathname.getAbsolutePath()).toLowerCase());
                    }
                });
                return Observable.fromArray(list.toArray(new String[list.size()]));
            }
        });
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
//    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        return true;
    }
}
