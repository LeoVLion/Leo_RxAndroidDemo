package com.example.administrator.leo_rxandroiddemo.utils;

import com.example.administrator.leo_rxandroiddemo.model.GankBeauty;
import com.example.administrator.leo_rxandroiddemo.model.GankBeautyResult;
import com.example.administrator.leo_rxandroiddemo.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by Administrator on 2018/2/9 0009.
 */

public class GankBeautyResultToItemMap implements Func1<GankBeautyResult, List<Item>> {

    private static GankBeautyResultToItemMap INSTANCE = new GankBeautyResultToItemMap();

    public GankBeautyResultToItemMap() {

    }

    public static GankBeautyResultToItemMap getIntance() {
        return INSTANCE;
    }

    @Override
    public List<Item> call(GankBeautyResult gankBeautyResult) {
        List<GankBeauty> gankBeauties = gankBeautyResult.beauties;
        List<Item> items = new ArrayList<Item>();
        SimpleDateFormat formatBefore = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat formatAfter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//去掉时间里面的T和Z字符 和毫秒
        for (GankBeauty gankBeauty: gankBeauties) {
            Item item = new Item();
            try {
                Date date = formatBefore.parse(gankBeauty.createdAt);
                item.description = formatAfter.format(date);
                item.imageUrl = gankBeauty.url;
            } catch (ParseException e) {
                e.printStackTrace();
                item.description = "unknown date";
            }
            items.add(item);
        }
        return items;
    }
}
