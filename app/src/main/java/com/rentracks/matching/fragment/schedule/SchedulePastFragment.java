package com.rentracks.matching.fragment.schedule;


import android.support.v4.app.Fragment;

import com.rentracks.matching.data.api.dto.ListDtoData;
import com.rentracks.matching.data.api.dto.search.EventSearchItem;

import rx.Observable;

public class SchedulePastFragment extends AbstractScheduleFragment {

    public static Fragment getInstance() {
        return new SchedulePastFragment();
    }

    @Override
    protected void loadData(final int page) {
            setUILoading(page);
            int limit = 10;
        Observable<ListDtoData<EventSearchItem>> objectDtoObservable = matchingApi.pastEvent(page, limit);
        callApiTradeSummary(objectDtoObservable, page, limit);
    }

}