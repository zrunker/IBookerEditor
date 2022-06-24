package cc.ibooker.ibookereditor.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cc.ibooker.ibookereditor.R;
import cc.ibooker.ibookereditor.adapter.ImgVPagerAdapter;
import cc.ibooker.ibookereditor.base.BaseActivity;
import cc.ibooker.ibookereditorlib.IbookerEditorScaleImageView;

/**
 * 图片预览Activity
 * <p>
 * Created by 邹峰立 on 2018/3/13 0013.
 */
public class ImgVPagerActivity extends BaseActivity implements View.OnClickListener {
    private String currentPath;
    private int currentPosition;
    private ArrayList<String> imgAllPathList;

    private ViewPager mViewPager;
    private ImgVPagerAdapter mAdapter;
    private TextView indicatorTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgvpger);
        setStatusBarColor(R.color.colorBlank);

        // 获取上一个界面传值
        currentPath = getIntent().getStringExtra("currentPath");
        currentPosition = getIntent().getIntExtra("position", 0);
        imgAllPathList = getIntent().getStringArrayListExtra("imgAllPathList");

        // 初始化
        if (imgAllPathList != null && imgAllPathList.size() > 0)
            init();
    }

    // 初始化
    private void init() {
        ImageView backImg = findViewById(R.id.img_back);
        backImg.setOnClickListener(this);
        mViewPager = findViewById(R.id.id_viewpager);
        ImageView leftImg = findViewById(R.id.img_left);
        leftImg.setOnClickListener(this);
        ImageView rightImg = findViewById(R.id.img_right);
        rightImg.setOnClickListener(this);
        indicatorTv = findViewById(R.id.tv_indicator);

        // 初始化数据
        initVpData();
        mViewPager.setCurrentItem(currentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                currentPath = imgAllPathList.get(position);
                updateIndicatorTv(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateIndicatorTv(currentPosition);
    }

    // 格式化indicatorTv
    private void updateIndicatorTv(int currentPosition) {
        String indicatorTip = (currentPosition + 1) + "/" + imgAllPathList.size();
        indicatorTv.setText(indicatorTip);
    }

    // 初始化ViewPager数据
    private void initVpData() {
        if (imgAllPathList != null && imgAllPathList.size() > 0) {
            ArrayList<IbookerEditorScaleImageView> imageViews = new ArrayList<>();
            // 获取图片资源，并保存到imageViews中
            for (int i = 0; i < imgAllPathList.size(); i++) {
                IbookerEditorScaleImageView imageView = new IbookerEditorScaleImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setOnMyClickListener(new IbookerEditorScaleImageView.OnMyClickListener() {
                    @Override
                    public void onMyClick(View v) {// 点击事件
                        finish();
                    }
                });
                final String imgPath = imgAllPathList.get(i);
                imageView.setOnMyLongClickListener(new IbookerEditorScaleImageView.OnMyLongClickListener() {
                    @Override
                    public void onMyLongClick(View v) {// 长按事件
                    }
                });
                Glide.with(this).load(imgPath).into(imageView);
                imageViews.add(imageView);
            }
            // 刷新数据
            setAdapter(imageViews);
        }
    }

    // 自定义setAdapter
    private void setAdapter(ArrayList<IbookerEditorScaleImageView> list) {
        if (mAdapter == null) {
            mAdapter = new ImgVPagerAdapter(list);
            mViewPager.setAdapter(mAdapter);
        } else {
            mAdapter.reflashData(list);
        }
    }

    // 点击事件监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:// 返回
                finish();
                break;
            case R.id.img_left:// 左移图片
                mViewPager.setCurrentItem(currentPosition == 0 ? imgAllPathList.size() - 1 : currentPosition - 1);
                break;
            case R.id.img_right:// 右移事件
                mViewPager.setCurrentItem(currentPosition == imgAllPathList.size() - 1 ? 0 : currentPosition + 1);
                break;
        }
    }
}
