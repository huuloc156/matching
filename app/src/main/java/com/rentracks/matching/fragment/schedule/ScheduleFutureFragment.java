package com.rentracks.matching.fragment.schedule;


import android.support.v4.app.Fragment;

import com.rentracks.matching.data.api.dto.ListDtoData;
import com.rentracks.matching.data.api.dto.search.EventSearchItem;

import rx.Observable;

public class ScheduleFutureFragment extends AbstractScheduleFragment {

    public static Fragment getInstance() {
        return new ScheduleFutureFragment();
    }

    @Override
    protected void loadData(final int page) {
            setUILoading(page);
            int limit = 10;
        Observable<ListDtoData<EventSearchItem>> objectDtoObservable = matchingApi.futureEvent(page, limit);
        callApiTradeSummary(objectDtoObservable, page, limit);
    }

}