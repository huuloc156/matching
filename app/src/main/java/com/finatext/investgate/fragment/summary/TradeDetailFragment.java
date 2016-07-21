package com.finatext.investgate.fragment.summary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finatext.investgate.R;
import com.finatext.investgate.adapter.RecyclerArrayAdapter;
import com.finatext.investgate.adapter.RecyclerArrayViewHolder;
import com.finatext.investgate.data.api.ApiSubscriber;
import com.finatext.investgate.data.api.dto.ListDto;
import com.finatext.investgate.data.api.dto.summary.ProfitLossYearDetailItem;
import com.finatext.investgate.fragment.AbstractPullAndLoadmoreFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by apple on 6/20/16.
 */
public class TradeDetailFragment extends AbstractPullAndLoadmoreFragment {

    List<ProfitLossYearDetailItem> items = new ArrayList<>();
    @BindView(R.id.txt_profit_loss_year_sum_pl_by_year)
    TextView txtSumPlbyYear;
    @BindView(R.id.txt_profit_loss_year_sum_profit)
    TextView txtSumProfit;
    @BindView(R.id.txt_profit_loss_year_sum_loss)
    TextView txtSumLoss;
    @BindView(R.id.txt_profit_loss_year_detail_day)
    TextView txtYearDetail;
    private int mYear;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String currYear  = new SimpleDateFormat("yyyy").format(new Date());
        int currYearInt = Integer.parseInt(currYear);
        Bundle args = getArguments();
        mYear = args.getInt("ProfitLossData",currYearInt);

        mCustomHeaderText = mYear+"年の株の損益分析";

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_trade_detail, container, false);
    }

    public static Fragment getInstance(){
        return new TradeDetailFragment();
    }
    ProfitLossYearDetailAdapter mAdapter;

    @Override
    public int getHeaderRightButtonImageResId() {
        return R.mipmap.header_reload;
    }

    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);
        onRefresh();
    }
    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new ProfitLossYearDetailAdapter();
        }
        return mAdapter;
    }


    @Override
    protected void loadData(final int page) {
        setUILoading(page);
        callApiProfitLoss(page, mYear);
    }
    protected void callApiProfitLoss(final int page, final int year) {

        Observable<ListDto<ProfitLossYearDetailItem>> objectDtoObservable = investgateApi.getYearlySummary(year);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDto<ProfitLossYearDetailItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ListDto<ProfitLossYearDetailItem> profitLossItem) {
                notifyLoadFail(page);
            }

            @Override
            public void onDataSuccess(ListDto<ProfitLossYearDetailItem> profitLossItem) {
                if(profitLossItem.data != null){
                    txtYearDetail.setText(year+"年の株の損益");
                    txtSumPlbyYear.setText(convertMoney(profitLossItem.data.pageInfo.sum_pl_by_year, true));
                    txtSumProfit.setText(convertMoney(profitLossItem.data.pageInfo.sum_profit, true));
                    txtSumLoss.setText(convertMoney(profitLossItem.data.pageInfo.sum_loss, true));
                    items = profitLossItem.data.items;
                    int last_page = page;
                    int curr_page = page;
                    if(profitLossItem.data.pageInfo != null){
                        if(profitLossItem.data.pageInfo.lastPage != 0) {
                            last_page = profitLossItem.data.pageInfo.lastPage;
                            curr_page = profitLossItem.data.pageInfo.page;
                        }
                    }
                    notifyLoaded(curr_page, last_page, items);
                    //step 3 add data to list
                    if(items!=null) {
                        mAdapter.addAll(items);
                        notifyDataSetChanged();
                    }
                }else {
                    notifyLoadFail(page);
                }
            }
        });
    }
    static class ProfitLossYearDetailAdapter extends RecyclerArrayAdapter<ProfitLossYearDetailItem, ProfitLossYearDetailViewHolder>{

        @Override
        public ProfitLossYearDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_summary_profit_loss_year_detail, parent, false);
            return new ProfitLossYearDetailViewHolder(view,onItemRecyclerClick) ;
        }

        @Override
        public void onBindViewHolder(ProfitLossYearDetailViewHolder holder, int position) {
            ProfitLossYearDetailItem item = getItem(position);
            holder.txtName.setText(item.companyName);
            holder.txtMoney.setText(convertMoney(item.p_l, true));
        }
    }
    static  class ProfitLossYearDetailViewHolder extends RecyclerArrayViewHolder{

        @BindView(R.id.txt_profit_loss_year_detail_item_name)
        TextView txtName;
        @BindView(R.id.txt_profit_loss_year_detail_item_money)
        TextView txtMoney;
        @BindView(R.id.txt_profit_loss_year_detail_item_status)
        TextView txtTatus;
        public ProfitLossYearDetailViewHolder(View itemView, OnItemRecyclerClick onRecylerItemClick) {

            super(itemView,onRecylerItemClick);
            ButterKnife.bind(this, itemView);
        }
    }
    private void demoData(final int page){
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<ProfitLossYearDetailItem> items = new ArrayList<>();
                if(page <=3) {
                    for (int i = 0; i < 30; i++) {
                        ProfitLossYearDetailItem temp= new ProfitLossYearDetailItem();
//                        temp.name="Name"+i;
                        items.add(temp);
                    }
                }
                //step2 notify loaded data
                notifyLoaded(page, 3, items);
                //step 3 add data to list
                if(items!=null) {
                    mAdapter.addAll(items);
                    notifyDataSetChanged();
                }
            }
        },1500);
    }
}
