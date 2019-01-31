package com.example.xiaojin20135.basemodule.activity.recycle;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xiaojin20135.basemodule.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class PullToRefreshActivity<T> extends BaseRecyclerActivity<T> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isShowProgressDialog=false;
        super.onCreate(savedInstanceState);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                loadFirstData();
            }
        });
    }

    @Override
    protected void initView() {
        initItemLayout ();
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById (R.id.base_swipe_refresh_lay);
        swipeMenuRecyclerView = (SwipeMenuRecyclerView)findViewById (R.id.base_rv_list);
        setRefreshEnable (canRefresh);
        chooseListTye (listType,isVertical);
        if(layoutResId == -1){
            throw new RuntimeException ("layoutResId is null!");
        }
        rvAdapter = new RvAdapter (layoutResId,new ArrayList<T>());
        rvAdapter.setLoadMoreView(new CustomLoadMoreView());
        swipeMenuRecyclerView.setAdapter (rvAdapter);
        swipeRefreshLayout .setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        rvAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMoreData();
            }
        });
    }
    /**
     * @author lixiaojin
     * @createon 2018-07-14 16:27
     * @Describe 设置加载更多
     */
    protected void setLoadMoreEnable(){
        //添加滚动监听
       // swipeMenuRecyclerView.addOnScrollListener (onScrollListener);
    }

    @Override
    protected int getLayoutId () {
        return R.layout.activity_base_list_recycler;
    }

    protected  void loadFirstData(){
        swipeRefreshLayout.setRefreshing(true);
        rvAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
    };

    /**
     * @author lixiaojin
     * @createon 2018-08-11 17:06
     * @Describe  数据展示
     */
    protected void showList(List<T> dataList){
        if(dataList != null && dataList.size () > 0){
            if(page == 1){
                rvAdapter.setNewData (new ArrayList<T> ());
            }
            rvAdapter.addData (dataList);
            if(dataList.size ()<rows&&page != 1){
                canLoadMore=false;
                rvAdapter.loadMoreEnd(page==1);
               // showToast (this,R.string.no_more);
            }else{
                rvAdapter.loadMoreComplete();
            }
        }else{
            if(page == 1){
                setEmpty ();
            }else{
                canLoadMore=false;
               // showToast (this,R.string.no_more);
                rvAdapter.loadMoreEnd(false);
            }
        }
    }
}
