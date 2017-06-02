package com.rentracks.matching.data.api.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HuuLoc on 5/26/17.
 */

public class ListDtoData <T> extends BaseApiDto{
    @SerializedName("data")
    public List<T> data;
}
