package com.lq.beauty.app.videoList;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lq.beauty.R;
import com.lq.beauty.app.base.BaseBackBeautyActivity;
import com.lq.beauty.app.base.BaseBeautyActivity;
import com.lq.beauty.app.videoList.adapter.VideoListAdapter;
import com.lq.beauty.app.videoList.data.VideoListItemData;
import com.lq.beauty.base.adapter.BaseRecyclerAdapter;
import com.lq.beauty.base.utils.FileUtil;
import com.lq.beauty.base.utils.ImageUtils;
import com.lq.beauty.base.utils.StringUtils;
import com.lq.beauty.base.utils.UIHelper;
import com.lq.beauty.base.widget.BaseRecyclerView;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class VideoListActivity extends BaseBackBeautyActivity {

    @BindView(R.id.rvVideoList)
    protected BaseRecyclerView rvVideoList;

    @BindView(R.id.rvMyVideoList)
    protected BaseRecyclerView rvMyVideoList;

    @BindView(R.id.nsvScrollView)
    protected NestedScrollView nsvScrollView;

    private VideoListAdapter videoListAdapter;
    private VideoListAdapter videoMyListAdapter;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected int getContentView() { return R.layout.act_video_list; }

    @Override
    protected void onLayoutInflater() {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle(R.string.video);
        Fresco.initialize(this);

        videoListAdapter = new VideoListAdapter(this);
        videoListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                onVideoItemClicked(videoListAdapter.getItem(position));
            }
        });
        rvVideoList.setLayoutManager(new GridLayoutManager(this, 3));
        rvVideoList.setAdapter(videoListAdapter);

        videoMyListAdapter = new VideoListAdapter(this);
        videoMyListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                onVideoItemClicked(videoMyListAdapter.getItem(position));
            }
        });
        rvMyVideoList.setLayoutManager(new GridLayoutManager(this, 3));
        rvMyVideoList.setAdapter(videoMyListAdapter);
    }

    @Override
    protected void initData() {
        initVideoListAdapterData();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int height = nsvScrollView.getMeasuredHeight();
        rvVideoList.setMaxHeight(height);
        rvMyVideoList.setMaxHeight(height);
    }

    private void onVideoItemClicked(VideoListItemData item) {

        int i = 1;
        int j = i + 1;
    }

    private void initVideoListAdapterData() {
        disposables.add(videoListObservable()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return TextUtils.equals("mp4", FileUtil.getFileFormat(s));
                    }
                })
                .map(new Function<String, VideoListItemData>() {
                    @Override
                    public VideoListItemData apply(String string) throws Exception {
                        if (!TextUtils.equals("mp4", FileUtil.getFileFormat(string).toLowerCase())) return null;
                        VideoListItemData data = new VideoListItemData();

                        File file = new File(string);
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        try {
                            mmr.setDataSource(string);
                            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒

                            int dur = StringUtils.toInt(duration, 0);
                            if (dur > 3600000)
                                data.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date(dur)));
                            else
                                data.setTime(new SimpleDateFormat("mm:ss").format(new Date(dur)));

                            data.setName(file.getName());

                            data.setVideoPath(string);

                        } catch (IllegalArgumentException e) {
                            Log.e("initData", "videoListObservable path=" + string);
                            e.printStackTrace();
                            data.setTime("00:00");
                            data.setName("");
                            data.setVideoPath("");
                        } catch (IllegalStateException e) {
                            Log.e("initData", "videoListObservable path=" + string);
                            e.printStackTrace();
                            data.setTime("00:00");
                            data.setName("");
                            data.setVideoPath("");
                        } catch (Exception e) {
                            Log.e("initData", "videoListObservable path=" + string);
                            e.printStackTrace();
                            data.setTime("00:00");
                            data.setName("");
                            data.setVideoPath("");
                        }
                        mmr.release();
//                        SystemClock.sleep(100);
                        return data;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VideoListItemData>() {
                    @Override
                    public void accept(VideoListItemData videoListItemData) throws Exception {
                        if (videoListItemData != null && !TextUtils.isEmpty(videoListItemData.getVideoPath())) {
                            if (VideoListConfig.checkVideoMy(videoListItemData.getVideoPath())) {
                                videoMyListAdapter.addItem(videoListItemData);
                            } else {
                                videoListAdapter.addItem(videoListItemData);
                            }
                        }
                    }
                }));
    }

    Observable<String> videoListObservable() {
        return Observable.generate(new Consumer<Emitter<String>>() {
            @Override
            public void accept(Emitter<String> stringEmitter) throws Exception {
                searchVideo(new File(FileUtil.getSDRoot()), stringEmitter, 0);
                stringEmitter.onComplete();
            }
        });
    }

    private void searchVideo(File dir, Emitter<String> emitter, int depth) {
        if (dir.isDirectory()) {
            File[] temp = dir.listFiles();
            for(File file : temp) {
                if (file.isDirectory()) {
                    if (VideoListConfig.checkVideoPathCanSearch(file.getAbsolutePath(), file.getName()))
                        searchVideo(file, emitter, depth++);
                } else {
                    if (TextUtils.equals("mp4", FileUtil.getFileFormat(file.getAbsolutePath()))) {
                        emitter.onNext(file.getAbsolutePath());
                    }
                }
            }
        } else {
            return;
        }
    }

}
