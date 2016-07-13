package com.finatext.investgate.fragment.summary;

import android.graphics.Color;
import android.os.Bundle;
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
import com.finatext.investgate.data.api.dto.summary.ProfitLossYearItem;
import com.finatext.investgate.fragment.AbstractPullAndLoadmoreFragment;

import java.text.ParseException;
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
public class ProfitLossTabYearFragment extends AbstractPullAndLoadmoreFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setDivider(false);
        return inflater.inflate(R.layout.fragment_summary_profit_lost_tab_year, container, false);
    }
    ProfitLossYearItemAdapter mAdapter;

    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new ProfitLossYearItemAdapter();
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
    public void callApiTradeSummary(final int page) {

        Observable<ListDto<ProfitLossYearItem>> objectDtoObservable = investgateApi.getDailyPLSummary(page);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDto<ProfitLossYearItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ListDto<ProfitLossYearItem> tradeSummaryItemListDto) {
                notifyLoadFail(page);
            }

            @Override
            public void onDataSuccess(ListDto<ProfitLossYearItem> tradeSummaryYearItem) {
                List<ProfitLossYearItem> items = tradeSummaryYearItem.data.items;
                for(int i = 0; i< items.size(); i++){
                    try {
                        ProfitLossYearItem item = items.get(i);
                        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(item.datetime);
                        item.datetime = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);
                        item.year = new SimpleDateFormat("yyyy/MM/dd").format(date);
                        if(i == 0){
                            item.isHeader = true;
                        }
                        items.set(i, item);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                int last_page = tradeSummaryYearItem.data.pageInfo.lastPage;
                int page = tradeSummaryYearItem.data.pageInfo.page;
                if(last_page > page || last_page == 0){
                    last_page = 1;
                    page = 1;
                }

                //step2 notify loaded data
                notifyLoaded(page, last_page, items);
                //step 3 add data to list
                if(items!=null) {
                    mAdapter.addAll(items);
                    notifyDataSetChanged();
                }else{
                    notifyLoadFail(page);
                }
            }
        });
    }
    @Override
    public void onItemClick(View view, int position) {
        startFragment(new TradeDetailFragment(),true);
    }

    public static class ProfitLossYearItemAdapter extends RecyclerArrayAdapter<ProfitLossYearItem, ProfitLossYearItemViewHolder>{
        @Override
        public ProfitLossYearItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_summary_profit_loss_year, parent, false);
            return new ProfitLossYearItemViewHolder(view,onItemRecyclerClick) ;
        }

        @Override
        public void onBindViewHolder(ProfitLossYearItemViewHolder holder, int position) {
            ProfitLossYearItem item = getItem(position);
            holder.txtyType.setTextColor(Color.parseColor("#ffffff"));
            holder.txtyCompanyName.setTextColor(Color.parseColor("#ffffff"));
            if(position%4==0)
            {
                holder.txtyType.setBackgroundResource(R.drawable.background_1);
                holder.txtyCompanyName.setBackgroundResource(R.drawable.background_1);
            }
            if(position%4==1)
            {
                holder.txtyType.setBackgroundResource(R.drawable.background_2);
                holder.txtyCompanyName.setBackgroundResource(R.drawable.background_2);
            }
            if(position%4==2)
            {
                holder.txtyType.setBackgroundResource(R.drawable.background_3);
                holder.txtyCompanyName.setBackgroundResource(R.drawable.background_3);
            }
            if(position%4==3)
            {
                holder.txtyType.setBackgroundResource(R.drawable.background_4);
                holder.txtyCompanyName.setBackgroundResource(R.drawable.background_4);
            }
            holder.txtyStatus.setText(item.status);
            holder.txtyName.setText(item.name);
            holder.txtyCompanyName.setText(item.companyname);
            holder.txtyType.setText(item.type);
            holder.txtyPositionPl.setText(item.position_pl+"円");
            holder.txtyCloseDate.setText(item.datetime);
            holder.txtYear.setText(String.valueOf(item.year));
            if(item.isHeader){
                holder.llHeader.setVisibility(View.VISIBLE);
                holder.llLine.setVisibility(View.GONE);
            }else{
                holder.llHeader.setVisibility(View.GONE);
                holder.llLine.setVisibility(View.VISIBLE);
            }
        }

    }

    public static class ProfitLossYearItemViewHolder extends RecyclerArrayViewHolder {
//        @BindView(android.R.id.text1)
//        TextView tvUserName;
        @BindView(R.id.txtystatus)
        TextView txtyStatus;
        @BindView(R.id.txtyname)
        TextView txtyName;
        @BindView(R.id.txtytype)
        TextView txtyType;
        @BindView(R.id.txtycompanyname)
        TextView txtyCompanyName;
        @BindView(R.id.txty_position_pl)
        TextView txtyPositionPl;
        @BindView(R.id.txty_close_date_time)
        TextView txtyCloseDate;
        @BindView(R.id.txt_profit_item_header)
        TextView txtYear;
        @BindView(R.id.ll_summary_profit_loss_header)
        LinearLayout llHeader;
        @BindView(R.id.ll_summary_profit_loss_line)
        LinearLayout llLine;

        public ProfitLossYearItemViewHolder(View itemView, OnItemRecyclerClick onItemRecyclerClick) {
            super(itemView,onItemRecyclerClick);
            ButterKnife.bind(this, itemView);
        }

    }
    public void demoData(final int page){
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<ProfitLossYearItem> items = new ArrayList<>();
                if(page <=3) {
                    int year = 2016;
                    for (int i = 0; i < 20; i++) {
//                        items.add("item page " + page + "  order" + i);
                        ProfitLossYearItem temp= new ProfitLossYearItem();
                        temp.name="Name"+i;
                        temp.position_pl="+435...";
                        temp.datetime="2016/05/01 23:09";
                        temp.companyname="abgdfgdfgdf gdfgfgdc"+i;
                        temp.status="ssss";
                        temp.type="xyz";
                        temp.isHeader = i%3 ==0;
                        if(temp.isHeader){
                            year--;
                        }
                        temp.year = String.valueOf(year);
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
