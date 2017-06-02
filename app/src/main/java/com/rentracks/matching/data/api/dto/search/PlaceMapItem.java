package com.rentracks.matching.data.api.dto.search;

import com.google.gson.annotations.SerializedName;
import com.rentracks.matching.data.api.dto.BaseApiDto;

import java.util.List;

/**
 * Created by HuuLoc on 5/30/17.
 */

public class PlaceMapItem<T> extends BaseApiDto {

    @SerializedName("results")
    public List<PlaceItem> results;

//    @SerializedName("results")
//    public List<PlaceItem> results;






}
