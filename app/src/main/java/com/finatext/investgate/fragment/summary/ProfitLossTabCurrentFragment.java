package com.finatext.investgate.fragment.summary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finatext.investgate.R;
import com.finatext.investgate.adapter.RecyclerArrayAdapter;
import com.finatext.investgate.adapter.RecyclerArrayViewHolder;
import com.finatext.investgate.data.api.ApiSubscriber;
import com.finatext.investgate.data.api.dto.ListDto;
import com.finatext.investgate.data.api.dto.PageInfo;
import com.finatext.investgate.data.api.dto.summary.ProfitLossItem;
import com.finatext.investgate.fragment.AbstractPullAndLoadmoreFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by apple on 6/20/16.
 */
public class ProfitLossTabCurrentFragment extends AbstractPullAndLoadmoreFragment {
    @BindView(R.id.txt_profit_sum_all)
    TextView txtSumAll;
    @BindView(R.id.txt_profit_kabu_pl)
    TextView txtKabuPL;
    @BindView(R.id.txt_profit_fx_pl)
    TextView txtFxPL;
    @BindView(R.id.txt_profit_trust_pl)
    TextView txtTrustPl;
    PageInfo DataItemAttr = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setDivider(false);

        return inflater.inflate(R.layout.fragment_summary_profit_lost_tab_current, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(DataItemAttr != null){
            setValue(DataItemAttr);
        }
    }
    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);
        onRefresh();
    }
    ProfitLossAdapter mAdapter;

    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new ProfitLossAdapter();
        }
        return mAdapter;
    }

    @Override
    protected void loadData(final int page) {
        //step1 show loading UI
        setUILoading(page);
        callApiTradeSummary(page);
//        demoData(page);
    }

    @Override
    public void onItemClick(View view, int position) {
        startFragment(new TradeDetailFragment(),true);
    }
    public void callApiTradeSummary(final int page) {

        Observable<ListDto<ProfitLossItem>> objectDtoObservable = investgateApi.getTradeSummaryYear();
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDto<ProfitLossItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ListDto<ProfitLossItem> tradeSummaryItemListDto) {
                notifyLoadFail(page);
            }

            @Override
            public void onDataSuccess(ListDto<ProfitLossItem> tradeSummaryYearItem) {
                List<ProfitLossItem> items = tradeSummaryYearItem.data.items;
                DataItemAttr = tradeSummaryYearItem.data.pageInfo;
                int last_page = tradeSummaryYearItem.data.pageInfo.lastPage;
                int page = tradeSummaryYearItem.data.pageInfo.page;
                if(last_page > page || last_page == 0){
                    last_page = 1;
                    page = 1;
                }
                setValue(DataItemAttr);

                int year = 0;
                for(int i = 0; i< items.size(); i++){
                    ProfitLossItem item = items.get(i);
                    if(item.year != year){
                        item.isHeader = true;
                        year = item.year;
                    }
                    items.set(i, item);
                }
                //step2 notify loaded data
                notifyLoaded(page, last_page, items);
                //step 3 add data to list
                if(items!=null) {
                    mAdapter.addAll(items);
                }else{
                    notifyLoadFail(page);
                }
            }
        });
    }
    private void setValue(PageInfo data){
        int sum_all = data.sum_all;
        int sum_stock = data.sum_stock;
        int sum_fx = data.sum_fx;
        int all = sum_all + sum_stock + sum_fx;
        txtSumAll.setText(((all>0)?"+":"-")+all+"円");
        txtKabuPL.setText(((sum_all>0)?"+":"-")+sum_all+"円");
        txtFxPL.setText(((sum_fx>0)?"+":"-")+sum_fx+"円");
        txtTrustPl.setText(((sum_stock>0)?"+":"-")+sum_stock+"円");

    }
    class ProfitLossAdapter extends RecyclerArrayAdapter<ProfitLossItem, ProfitLossViewHolder>{

        @Override
        public ProfitLossViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_summary_profit_loss, parent, false);
                return new ProfitLossViewHolder(view,onItemRecyclerClick) ;
        }

        @Override
        public void onBindViewHolder(ProfitLossViewHolder holderItem, int position) {
            ProfitLossItem item = getItem(position);
                if(item.type == "stock") {
                    holderItem.txtProfitName.setText("投信");
                }else if(item.type == "fx"){
                    holderItem.txtProfitName.setText("FX");
                }else if(item.type == "trust"){
                    holderItem.txtProfitName.setText("投信");
                }else{
                        holderItem.txtProfitName.setText("other");
                }
                holderItem.txtProfitMoney.setText(item.position_pl);
                holderItem.txtCloseDate.setText(item.datetime);
                holderItem.txtYear.setText(String.valueOf(item.year));
                if(item.isHeader){
                    holderItem.llHeader.setVisibility(View.VISIBLE);
                    holderItem.llLine.setVisibility(View.GONE);
                }else{
                    holderItem.llHeader.setVisibility(View.GONE);
                    holderItem.llLine.setVisibility(View.VISIBLE);
                }
        }
    }

    static class ProfitLossViewHolder extends RecyclerArrayViewHolder {
        @BindView(R.id.txt_profit_name)
        TextView txtProfitName;
        @BindView(R.id.txt_profit_money_pl)
        TextView txtProfitMoney;
        @BindView(R.id.txt_profit_close_date_time)
        TextView txtCloseDate;
        @BindView(R.id.txt_profit_item_header)
        TextView txtYear;
        @BindView(R.id.ll_summary_profit_loss_header)
        LinearLayout llHeader;
        @BindView(R.id.ll_summary_profit_loss_line)
        LinearLayout llLine;

        public ProfitLossViewHolder(View itemView, OnItemRecyclerClick onItemRecyclerClick) {
            super(itemView, onItemRecyclerClick);
            ButterKnife.bind(this, itemView);
        }
    }
    public void demoData(final int page){
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<ProfitLossItem> items = new ArrayList<>();
                if(page <=3) {
                    int year = 2016;
                    for (int i = 0; i < 20; i++) {
//                        items.add("item page " + page + "  order" + i);
                        ProfitLossItem temp= new ProfitLossItem();
                        temp.name="Name"+i;
                        temp.position_pl="+435...";
                        temp.datetime="2016/05/01 23:09";
                        temp.isHeader = i%3 ==0;
                        if(temp.isHeader){
                            temp.year = year--;
                        }
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
        //TODO if load fail  notifyLoadFail(page);
    }
}
