package com.finatext.investgate.fragment.summary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finatext.investgate.R;
import com.finatext.investgate.data.api.ApiSubscriber;
import com.finatext.investgate.data.api.dto.ObjectDto;
import com.finatext.investgate.data.api.dto.summary.TradeDto;
import com.finatext.investgate.data.api.dto.summary.TradeSummaryItem;
import com.finatext.investgate.fragment.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by apple on 6/20/16.
 */
public class SummaryHomeFragment extends BaseFragment {

    @BindView(R.id.tv_summary_value)
    TextView txtSummary;
    @BindView(R.id.tv_hisotry_value)
    TextView txtHistory;
    @BindView(R.id.txt_tv_date)
    TextView txtDate;

    TradeSummaryItem item= null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_summary_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String curDate  = new SimpleDateFormat("MM/yy (EEE)").format(new Date());
        txtDate.setText(curDate);

        mCustomHeaderText = getResources().getString(R.string.header_home_summary);
        if(item == null) {
            callApiTradeSummary();
        }else{
            txtSummary.setText(convertMoney(item.daily_commission, false));
            txtHistory.setText("手数料 "+convertMoney(item.daily_profit_loss, false));
        }
    }


    @OnClick(R.id.ln_profit)
    void clickProfit(){
        startFragment(ProfitLossSwipeFragment.getInstance(),true);
    }
    @OnClick(R.id.ln_history)
    void clickTradeHistory(){
        startFragment(CustomTradeHistoryFragment.getInstance(),true);
    }



    private void callApiTradeSummary() {
        showProgressDialog();
        Observable<ObjectDto<TradeDto<TradeSummaryItem>>> objectDtoObservable = investgateApi.getTradeSummary();
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<TradeDto<TradeSummaryItem>>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto<TradeDto<TradeSummaryItem>> tradeSummaryItemListDto) {
                closeDialog();
            }

            @Override
            public void onDataSuccess(ObjectDto<TradeDto<TradeSummaryItem>> tradeSummaryItemListDto) {
                item = tradeSummaryItemListDto.data.valueData;
                txtSummary.setText(convertMoney(item.daily_commission, false));
                txtHistory.setText("手数料 "+convertMoney(item.daily_profit_loss, false));
                closeDialog();
            }
        });
    }

}
