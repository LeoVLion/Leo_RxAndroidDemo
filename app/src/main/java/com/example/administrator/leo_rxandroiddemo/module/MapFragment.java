package com.example.administrator.leo_rxandroiddemo.module;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.leo_rxandroiddemo.BaseFragment;
import com.example.administrator.leo_rxandroiddemo.R;
import com.example.administrator.leo_rxandroiddemo.adapter.ItemListAdapter;
import com.example.administrator.leo_rxandroiddemo.model.Item;
import com.example.administrator.leo_rxandroiddemo.network.Network;
import com.example.administrator.leo_rxandroiddemo.utils.GankBeautyResultToItemMap;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/9 0009.
 */

public class MapFragment extends BaseFragment {

    @Bind(R.id.ll_content)
    LinearLayout ll_content;
    @Bind(R.id.tv_current_page)
    TextView tv_current_page;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.btn_previous)
    Button btn_previous;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.elementary_rv)
    RecyclerView elementary_rv;
    private ItemListAdapter adapter = null;
    private int currentPage = 0;

    @Override
    public void initView() {
        elementary_rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new ItemListAdapter();
        elementary_rv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        btn_previous.setEnabled(false);
        if (currentPage == 0) {
            loadPage(++currentPage);
        }
        tv_current_page.setText(getString(R.string.str_current_page, currentPage));
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_map;
    }

    @OnClick(R.id.btn_next)
    void next() {
        loadPage(++currentPage);
        if (currentPage > 1) {
            btn_previous.setEnabled(true);
        }
    }

    @OnClick(R.id.btn_previous)
    void previous() {
        loadPage(--currentPage);
        if (currentPage == 1) {
            btn_previous.setEnabled(false);
        }
    }

    private void loadPage(final int page) {
        swipeRefreshLayout.setRefreshing(true);
        subscription = Network.getGankApi()
                .getBeauties(10, page)
                .map(GankBeautyResultToItemMap.getIntance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Item>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(ll_content, "数据加载失败", 2000).show();
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(ll_content, "数据加载完成", 2000).show();
                        tv_current_page.setText(getString(R.string.str_current_page, page));
                        adapter.setImages(items);
                    }
                });

    }
}
