package com.finatext.investgate.fragment.summary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finatext.investgate.R;
import com.finatext.investgate.data.api.dto.summary.TradeEach;
import com.finatext.investgate.fragment.BaseFragment;

import butterknife.BindView;

public class TradeHistoryDetailFragment extends BaseFragment {

    @BindView(R.id.txt_trade_each_type)
    TextView txttype;

    @BindView(R.id.txt_trade_each_name)
    TextView txtName;

    @BindView(R.id.txt_trade_each_date)
    TextView txtdate;

    @BindView(R.id.txt_trade_each_pl)
    TextView txtTraddingVolumne;

    @BindView(R.id.txt_trade_each_commission)
    TextView txtComission;

    @BindView(R.id.txt_trade_each_interest)
    TextView txtInterest;


    TradeEach mData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCustomHeaderText = getResources().getString(R.string.header_history_detail);
        Bundle args = getArguments();
        mData = (TradeEach) args
                .getParcelable("TradeEachData");
        return inflater.inflate(R.layout.fragment_summary_trade_history_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mData != null){
            txttype.setText(mData.type);
            txtName.setText(mData.name);
            txtdate.setText(mData.date);
            txtTraddingVolumne.setText(convertMoney(mData.trading_volumne, false));
            txtComission.setText(convertMoney(mData.commission_fee, false));
            txtInterest.setText(convertMoney(mData.interest, false));
        }
    }
}
