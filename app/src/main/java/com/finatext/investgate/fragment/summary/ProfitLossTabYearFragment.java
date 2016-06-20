package com.finatext.investgate.fragment.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finatext.investgate.R;
import com.finatext.investgate.fragment.BaseFragment;

/**
 * Created by apple on 6/20/16.
 */
public class ProfitLossTabYearFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_profit_lost_tab_year, container, false);
    }
}
