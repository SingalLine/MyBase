package com.rilin.lzy.mybase.my.lunbo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.application.MyApplication;
import com.rilin.lzy.mybase.model.BannerModel;
import com.rilin.lzy.mybase.model.Engine;
import com.rilin.lzy.mybase.model.RefreshModel;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.bgabanner.BGABanner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListWithHeadActivity extends AppCompatActivity {

    private ListView mContentLv;
    private BGABanner mBanner;
    private ContentAdapter mContentAdapter;

    private Engine mEngine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_head);

        mContentLv = (ListView)findViewById(R.id.lv_content);
        mEngine = MyApplication.getApplication().getEngine();

        initView();

        loadBannerData();
        loadContentData();
    }

    //加载内容列表数据
    private void loadContentData() {
        mEngine.loadContentData("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/defaultdata.json").enqueue(new Callback<List<RefreshModel>>() {
            @Override
            public void onResponse(Call<List<RefreshModel>> call, Response<List<RefreshModel>> response) {
                mContentAdapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<RefreshModel>> call, Throwable t) {
                Toast.makeText(MyApplication.getApplication(), "加载内容数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //加载头布局广告条数据
    private void loadBannerData() {
        mEngine.fetchItemsWithItemCount(5).enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                BannerModel bannerModel = response.body();
                mBanner.setData(bannerModel.imgs, bannerModel.tips);
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                Toast.makeText(MyApplication.getApplication(), "加载广告条数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        View headerView = View.inflate(this,R.layout.layout_header_banner,null);
        mBanner = (BGABanner)headerView.findViewById(R.id.banner);
        mBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                Glide.with(banner.getContext()).load(model).placeholder(R.mipmap.holder).error(R.mipmap.holder)
                        .dontAnimate().thumbnail(0.1f).into((ImageView)view);
            }
        });
        mContentLv.addHeaderView(headerView);
        mContentAdapter = new ContentAdapter(this);
        mContentLv.setAdapter(mContentAdapter);
    }


    private class ContentAdapter extends BGAAdapterViewAdapter<RefreshModel> {

        public ContentAdapter(Context context) {
            super(context, R.layout.item_normal);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, RefreshModel model) {
            helper.setText(R.id.tv_item_normal_title, model.title).setText(R.id.tv_item_normal_detail, model.detail);
        }
    }
}
