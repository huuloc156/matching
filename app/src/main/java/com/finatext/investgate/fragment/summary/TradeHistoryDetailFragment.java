package com.finatext.investgate.fragment.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finatext.investgate.R;
import com.finatext.investgate.fragment.BaseFragment;

public class TradeHistoryDetailFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_trade_history_detail, container, false);
    }

}
