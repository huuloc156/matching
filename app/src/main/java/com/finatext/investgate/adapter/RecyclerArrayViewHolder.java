package com.finatext.investgate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by apple on 9/12/15.
 */
public class RecyclerArrayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    OnItemRecyclerClick onRecylerItemClick;
    public RecyclerArrayViewHolder(View itemView,OnItemRecyclerClick onRecylerItemClick) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.onRecylerItemClick = onRecylerItemClick;
    }

    @Override
    public void onClick(View view) {
        if(onRecylerItemClick!=null){
            int adapterPosition = getAdapterPosition();
            if(adapterPosition!= RecyclerView.NO_POSITION) {
                onRecylerItemClick.onItemClick(view, adapterPosition);
            }
        }
    }
    public interface OnItemRecyclerClick {
        public void onItemClick(View view, int position);
    }
}
