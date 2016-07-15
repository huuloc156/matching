package com.finatext.investgate.fragment.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finatext.investgate.R;
import com.finatext.investgate.adapter.RecyclerArrayAdapter;
import com.finatext.investgate.adapter.RecyclerArrayViewHolder;
import com.finatext.investgate.data.api.dto.summary.ProfitLossYearDetailItem;
import com.finatext.investgate.fragment.AbstractPullAndLoadmoreFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by apple on 6/20/16.
 */
public class TradeDetailFragment extends AbstractPullAndLoadmoreFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_trade_detail, container, false);
    }
    ProfitLossYearDetailAdapter mAdapter;
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
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<ProfitLossYearDetailItem> items = new ArrayList<>();
                if(page <=3) {
                    for (int i = 0; i < 20; i++) {
                        ProfitLossYearDetailItem temp= new ProfitLossYearDetailItem();
                        temp.name="Name"+i;
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
            holder.txtName.setText(item.name);
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
}
