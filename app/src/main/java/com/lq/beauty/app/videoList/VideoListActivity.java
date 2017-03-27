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
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class VideoListActivity extends BaseBackBeautyActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rvVideoList)
    protected RecyclerView rvVideoList;

    @BindView(R.id.rvMyVideoList)
    protected RecyclerView rvMyVideoList;

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

            }
        });
        rvVideoList.setLayoutManager(new GridLayoutManager(this, 3));
        rvVideoList.setAdapter(videoListAdapter);

        videoMyListAdapter = new VideoListAdapter(this);
        videoMyListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                int i= position;
                int j = i + 1;
            }
        });
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        glm.setAutoMeasureEnabled(true);
        glm.setSmoothScrollbarEnabled(true);
        rvMyVideoList.setLayoutManager(glm);
//        rvMyVideoList.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
//        rvMyVideoList.setHasFixedSize(true);
        rvMyVideoList.setNestedScrollingEnabled(false);
        rvMyVideoList.setAdapter(videoMyListAdapter);
    }

    @Override
    protected void initData() {
        initVideoListAdapterData();

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
                                videoMyListAdapter.addItem(videoListItemData);
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

    class FullyLinearLayoutManager extends LinearLayoutManager {

        private final String TAG = FullyLinearLayoutManager.class.getSimpleName();

        public FullyLinearLayoutManager(Context context) {
            super(context);
        }

        public FullyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        private int[] mMeasuredDimension = new int[2];

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                              int widthSpec, int heightSpec) {

            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);

            Log.i(TAG, "onMeasure called. \nwidthMode " + widthMode
                    + " \nheightMode " + heightSpec
                    + " \nwidthSize " + widthSize
                    + " \nheightSize " + heightSize
                    + " \ngetItemCount() " + getItemCount());

            int width = 0;
            int height = 0;
            for (int i = 0; i < getItemCount(); i++) {
                measureScrapChild(recycler, i,
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);

                if (getOrientation() == HORIZONTAL) {
                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }
            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                       int heightSpec, int[] measuredDimension) {
            try {
                View view = recycler.getViewForPosition(0);//fix 动态添加时报IndexOutOfBoundsException

                if (view != null) {
                    RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();

                    int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                            getPaddingLeft() + getPaddingRight(), p.width);

                    int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                            getPaddingTop() + getPaddingBottom(), p.height);

                    view.measure(childWidthSpec, childHeightSpec);
                    measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                    measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                    recycler.recycleView(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }
}
