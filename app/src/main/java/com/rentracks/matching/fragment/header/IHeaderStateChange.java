package com.rentracks.matching.fragment.header;

/**
 * Created by apple on 6/16/16.
 */
public interface IHeaderStateChange {
    void checkHeaderState();
    void  setHeaderInfo(IHeaderInfo iHeaderInfo,String headerCustomText);
}
