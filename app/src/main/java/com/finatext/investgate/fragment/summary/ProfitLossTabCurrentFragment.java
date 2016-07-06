package com.finatext.investgate.fragment.summary;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finatext.investgate.R;
import com.finatext.investgate.adapter.RecyclerArrayAdapter;
import com.finatext.investgate.adapter.RecyclerArrayViewHolder;
import com.finatext.investgate.data.api.dto.summary.ProfitLossItem;
import com.finatext.investgate.fragment.AbstractPullAndLoadmoreFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by apple on 6/20/16.
 */
public class ProfitLossTabCurrentFragment extends AbstractPullAndLoadmoreFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_profit_lost_tab_current, container, false);
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
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<ProfitLossItem> items = new ArrayList<>();
                if(page <=3) {
                    for (int i = 0; i < 20; i++) {
//                        items.add("item page " + page + "  order" + i);
                        ProfitLossItem temp= new ProfitLossItem();
                        temp.status="sta"+i;
                        temp.companyname="  cong ty"+i+"  ";
                        temp.type="  type"+i+"  ";
                        temp.name="Name"+i;
                        temp.position_pl="+435...";
                        temp.datetime="2016/05/01 23:09";
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

    @Override
    public void onItemClick(View view, int position) {
        startFragment(new TradeDetailFragment(),true);
    }

    static class ProfitLossAdapter extends RecyclerArrayAdapter<ProfitLossItem, ProfitLossViewHolder>{
        @Override
        public ProfitLossViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_summary_profit_loss, parent, false);
            return new ProfitLossViewHolder(view,onItemRecyclerClick) ;
        }

        @Override
        public void onBindViewHolder(ProfitLossViewHolder holder, int position) {
            ProfitLossItem item = getItem(position);
//            holder.tvUserName.setText(item);
            holder.txtvType.setTextColor(Color.parseColor("#ffffff"));
            holder.txtvCompanyName.setTextColor(Color.parseColor("#ffffff"));
            if(position%4==0)
            {
                holder.txtvType.setBackgroundResource(R.drawable.background_1);
                holder.txtvCompanyName.setBackgroundResource(R.drawable.background_1);
            }
            if(position%4==1)
            {
                holder.txtvType.setBackgroundResource(R.drawable.background_2);
                holder.txtvCompanyName.setBackgroundResource(R.drawable.background_2);
            }
            if(position%4==2)
            {
                holder.txtvType.setBackgroundResource(R.drawable.background_3);
                holder.txtvCompanyName.setBackgroundResource(R.drawable.background_3);
            }
            if(position%4==3)
            {
                holder.txtvType.setBackgroundResource(R.drawable.background_4);
                holder.txtvCompanyName.setBackgroundResource(R.drawable.background_4);
            }
              holder.txtvStatus.setText(item.status);
              holder.txtvName.setText(item.name);
              holder.txtvCompanyName.setText(item.companyname);
              holder.txtvType.setText(item.type);
              holder.txtvPositionPl.setText(item.position_pl);
              holder.txtvCloseDate.setText(item.datetime);
        }

    }
    static class ProfitLossViewHolder extends RecyclerArrayViewHolder {
//        @BindView(android.R.id.text1)
//        TextView tvUserName;
        @BindView(R.id.txtvstatus)
        TextView txtvStatus;
        @BindView(R.id.txtvname)
        TextView txtvName;
        @BindView(R.id.txtvtype)
        TextView txtvType;
        @BindView(R.id.txtvcompanyname)
        TextView txtvCompanyName;
        @BindView(R.id.txtv_position_pl)
        TextView txtvPositionPl;
        @BindView(R.id.txtv_close_date_time)
        TextView txtvCloseDate;
        public ProfitLossViewHolder(View itemView, OnItemRecyclerClick onItemRecyclerClick) {
            super(itemView,onItemRecyclerClick);
            ButterKnife.bind(this, itemView);
        }

    }

}
