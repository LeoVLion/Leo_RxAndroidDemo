package com.example.administrator.leo_rxandroiddemo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.administrator.leo_rxandroiddemo.module.ElementaryFragment;
import com.example.administrator.leo_rxandroiddemo.module.MapFragment;
import com.example.administrator.leo_rxandroiddemo.module.ZipFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RxJavaActivity extends AppCompatActivity {

    @Bind(R.id.toolBar)
    Toolbar toolbar;
    @Bind((R.id.tab))
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        ButterKnife.bind(this);
        toolbar.setTitle("RxJava与网络框架结合使用");
        setSupportActionBar(toolbar);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ElementaryFragment();
                    case 1:
                        return new MapFragment();
                    case 2:
                        return new ZipFragment();
                    default:
                        return new ElementaryFragment();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            /**
             * ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
             * @param position
             * @return
             */
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.str_base);
                    case 1:
                        return getString(R.string.map);
                    case 2:
                        return getString(R.string.str_zip);
                }
                return super.getPageTitle(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }
}
