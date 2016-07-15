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

    TradeSummaryItem item= null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_summary_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(item == null) {
            callApiTradeSummary();
        }else{
            txtSummary.setText(item.daily_commission+"円");
            txtHistory.setText(item.daily_profit_loss+"円");
        }
    }

    @OnClick(R.id.ln_profit)
    void clickProfit(){
        startFragment(new ProfitLossSwipeFragment(),true);
    }
    @OnClick(R.id.ln_history)
    void clickTradeHistory(){
        startFragment(new TradeHistoryFragmentAbstractSwipeFragment(),true);
    }




    public void callApiTradeSummary() {
        Observable<ObjectDto<TradeDto<TradeSummaryItem>>> objectDtoObservable = investgateApi.getTradeSummary();
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<TradeDto<TradeSummaryItem>>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto<TradeDto<TradeSummaryItem>> tradeSummaryItemListDto) {

            }

            @Override
            public void onDataSuccess(ObjectDto<TradeDto<TradeSummaryItem>> tradeSummaryItemListDto) {
                item = tradeSummaryItemListDto.data.ValueData;
                txtSummary.setText(item.daily_commission+"円");
                txtHistory.setText(item.daily_profit_loss+"円");
            }
        });
    }

}
