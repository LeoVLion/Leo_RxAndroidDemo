package com.example.administrator.leo_rxandroiddemo.network;

import com.example.administrator.leo_rxandroiddemo.network.api.GankApi;
import com.example.administrator.leo_rxandroiddemo.network.api.ZhuangbiApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/2/8 0008.
 */

public class Network {

    private static ZhuangbiApi zhuangbiApi;
    private static GankApi gankApi;
    private static OkHttpClient okHttpClient = null;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    static {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(logInterceptor).build();
    }

    public static ZhuangbiApi getZhuangbiApi() {
        if (zhuangbiApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://www.zhuangbi.info/")
                    .addConverterFactory(gsonConverterFactory)//Retrofit2 的baseUlr路径(path)必须以 /（斜线） 结束，不然会抛出一个IllegalArgumentException
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)//配合RxJava 使用
                    .build();
             zhuangbiApi = retrofit.create(ZhuangbiApi.class);
        }

        return zhuangbiApi;
    }

    public static GankApi getGankApi() {
        if (gankApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi = retrofit.create(GankApi.class);
        }
        return gankApi;
    }
}
