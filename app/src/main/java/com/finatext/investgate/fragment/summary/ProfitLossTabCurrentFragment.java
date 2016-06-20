package com.finatext.investgate.fragment.summary;

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
                        items.add(new ProfitLossItem());
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
        }

    }
    static class ProfitLossViewHolder extends RecyclerArrayViewHolder {
//        @BindView(android.R.id.text1)
//        TextView tvUserName;
        public ProfitLossViewHolder(View itemView, OnItemRecyclerClick onItemRecyclerClick) {
            super(itemView,onItemRecyclerClick);
            ButterKnife.bind(this, itemView);
        }

    }

}
