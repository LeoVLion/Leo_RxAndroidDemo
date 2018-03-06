package com.example.administrator.leo_rxandroiddemo.network.api;

import com.example.administrator.leo_rxandroiddemo.model.ZhuangbiImage;

import java.util.List;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2018/2/8 0008.
 */

public interface ZhuangbiApi {
    /**
     * 配合RxJava使用 返回值不再是一个Call ,而是返回的一个Observble.
     * @param query
     * @return
     */
    @GET("search")
    Observable<Result<List<ZhuangbiImage>>> search(@Query("q") String query);

    @GET("search")
    Observable<List<ZhuangbiImage>> search2(@Query("q") String query);
}
