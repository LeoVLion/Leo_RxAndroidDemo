package com.example.administrator.leo_rxandroiddemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GankBeautyResult {
    public boolean error;
    //@SerializedName注解 将对象里的属性beauties跟json里字段results对应值匹配起来。
    public @SerializedName("results")
    List<GankBeauty> beauties;
}
