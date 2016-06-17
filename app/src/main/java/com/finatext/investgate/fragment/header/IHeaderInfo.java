package com.finatext.investgate.fragment.header;

import android.view.View;

/**
 * Created by apple on 10/3/15.
 */
public interface IHeaderInfo {
    int HEADER_MODE_NORMAL = 1;
    int HEADER_MODE_NONE = 4;
    int HEADER_MODE_SEARCH = 2;
    int HEADER_MODE_SEARCH_INACTIVE = 3;

    int getHeaderMode();
    int getHeaderTitleResId();
    int getHeaderRightButtonImageResId();
    int getHeaderLeftButtonImageResId();
    void onClickHeaderLeftButton(View view);
    void onClickHeaderRightButton(View view);
    boolean haveHeaderBackButton();
    int getHeaderSearchHintResId();
    String getSearchKeyword();
}
