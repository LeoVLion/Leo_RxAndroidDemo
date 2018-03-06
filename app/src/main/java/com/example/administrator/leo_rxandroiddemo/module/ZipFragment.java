package com.example.administrator.leo_rxandroiddemo.module;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.leo_rxandroiddemo.BaseFragment;
import com.example.administrator.leo_rxandroiddemo.R;
import com.example.administrator.leo_rxandroiddemo.adapter.ItemListAdapter;
import com.example.administrator.leo_rxandroiddemo.model.Item;
import com.example.administrator.leo_rxandroiddemo.model.ZhuangbiImage;
import com.example.administrator.leo_rxandroiddemo.network.Network;
import com.example.administrator.leo_rxandroiddemo.utils.GankBeautyResultToItemMap;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


public class ZipFragment extends BaseFragment {

    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.elementary_rv)
    RecyclerView recyclerView;
    private ItemListAdapter adapter;

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new ItemListAdapter();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        zipLoad();
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_zip;
    }

    private void zipLoad() {
        swipeRefreshLayout.setRefreshing(true);
        subscription = Observable.zip(Network.getGankApi().getBeauties(200, 1).map(GankBeautyResultToItemMap.getIntance()),
                Network.getZhuangbiApi().search2("装逼"), new Func2<List<Item>, List<ZhuangbiImage>, List<Item>>() {
                    @Override
                    public List<Item> call(List<Item> gankItems, List<ZhuangbiImage> zhuangbiImageItems) {
                        List<Item> items = new ArrayList<Item>();
                        for (int i = 0; i < gankItems.size() / 2 && i < zhuangbiImageItems.size(); i++) {
                            items.add(gankItems.get(2 * i));
                            items.add(gankItems.get(2 * i + 1));
                            Item zhuangbi = new Item();
                            zhuangbi.description = zhuangbiImageItems.get(i).description;
                            zhuangbi.imageUrl = zhuangbiImageItems.get(i).image_url;
                            items.add(zhuangbi);
                        }
                        return items;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Item>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.setImages(items);
                    }
                });
    }
}
