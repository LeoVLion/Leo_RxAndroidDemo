package com.example.administrator.leo_rxandroiddemo;
;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by Administrator on 2018/2/8 0008.
 */

public abstract class BaseFragment extends Fragment {

    public Subscription subscription;
    public View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(setContentView(), container, false);
        }
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    protected abstract void initView();

    public abstract int setContentView();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    public void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
