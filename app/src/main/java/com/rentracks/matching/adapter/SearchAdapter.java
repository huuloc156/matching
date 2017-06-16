package com.rentracks.matching.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.data.api.dto.search.EventSearchItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HuuLoc on 5/26/17.
 */

public class SearchAdapter {


    public static class SearchItemAdapter extends RecyclerArrayAdapter<EventSearchItem, SearchItemViewHolder>{
        @Override
        public SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_timeline, parent, false);
            return new SearchItemViewHolder(view,onItemRecyclerClick) ;
        }

        @Override
        public void onBindViewHolder(SearchItemViewHolder holder, int position) {
//            EventSearchItem item = getItem(position);
//            holder.txtyType.setTextColor(Color.parseColor("#ffffff"));
//            holder.txtyCompanyName.setTextColor(Color.parseColor("#ffffff"));
//            if(position%4==0)
//            {
//                holder.txtyType.setBackgroundResource(R.drawable.background_1);
//                holder.txtyCompanyName.setBackgroundResource(R.drawable.background_1);
//            }
//            if(position%4==1)
//            {
//                holder.txtyType.setBackgroundResource(R.drawable.background_2);
//                holder.txtyCompanyName.setBackgroundResource(R.drawable.background_2);
//            }
//            if(position%4==2)
//            {
//                holder.txtyType.setBackgroundResource(R.drawable.background_3);
//                holder.txtyCompanyName.setBackgroundResource(R.drawable.background_3);
//            }
//            if(position%4==3)
//            {
//                holder.txtyType.setBackgroundResource(R.drawable.background_4);
//                holder.txtyCompanyName.setBackgroundResource(R.drawable.background_4);
//            }
//            holder.txtyStatus.setText(item.status);
//            holder.txtyName.setText(item.name);
//            holder.txtyCompanyName.setText(item.companyname);
//            holder.txtyType.setText(convertStyle(item.type));
//            holder.txtyCloseDate.setText(item.datetime);
//            holder.txtYear.setText(String.valueOf(item.year));
//            if(item.isHeader){
//                holder.llHeader.setVisibility(View.VISIBLE);
//                holder.llLine.setVisibility(View.GONE);
//            }else{
//                holder.llHeader.setVisibility(View.GONE);
//                holder.llLine.setVisibility(View.VISIBLE);
//            }
        }


    }

    public static class SearchItemViewHolder extends RecyclerArrayViewHolder {

        @BindView(R.id.img_rit)
        ImageView img_avt;
        @BindView(R.id.txt_top_rit)
        TextView txtTitle;
        @BindView(R.id.txt_bottom_rit)
        TextView txtDescription;

        public SearchItemViewHolder(View itemView, OnItemRecyclerClick onItemRecyclerClick) {
            super(itemView,onItemRecyclerClick);
            ButterKnife.bind(this, itemView);
        }

    }
}
