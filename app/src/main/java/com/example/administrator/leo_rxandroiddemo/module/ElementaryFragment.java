package com.example.administrator.leo_rxandroiddemo.module;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.leo_rxandroiddemo.BaseFragment;
import com.example.administrator.leo_rxandroiddemo.R;
import com.example.administrator.leo_rxandroiddemo.adapter.ZhuangbiListAdapter;
import com.example.administrator.leo_rxandroiddemo.model.ZhuangbiImage;
import com.example.administrator.leo_rxandroiddemo.network.Network;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import retrofit2.adapter.rxjava.Result;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/8 0008.
 */

public class ElementaryFragment extends BaseFragment {

    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.elementary_rv)
    RecyclerView elementary_rv;

    private ZhuangbiListAdapter adapter = null;

    @Override
    public int setContentView() {
        return R.layout.fragment_elementary;
    }

    @Override
    public void initView() {
        elementary_rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new ZhuangbiListAdapter();
        elementary_rv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        radioGroup.check(R.id.rb1);
    }

    @OnCheckedChanged({R.id.rb1,R.id.rb2,R.id.rb3,R.id.rb4})
    public void onCheckChange(RadioButton rb, boolean checked) {
        if (checked) {
            unsubscribe();
            swipeRefreshLayout.setRefreshing(true);
            searchImage(rb.getText().toString());
        }
    }

    private void searchImage(String key) {
        subscription = Network.getZhuangbiApi()
                .search(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private Observer<Result<List<ZhuangbiImage>>> observer = new Observer<Result<List<ZhuangbiImage>>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(Result<List<ZhuangbiImage>> zhuangbiImages) {
            swipeRefreshLayout.setRefreshing(false);
            if (zhuangbiImages.response().isSuccessful()) {
                adapter.setImages(zhuangbiImages.response().body());
            } else {
            }
        }
    };
}
