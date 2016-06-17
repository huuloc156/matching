package com.finatext.investgate.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public abstract class RecyclerArrayAdapter<M, VH extends RecyclerArrayViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected RecyclerArrayViewHolder.OnItemRecyclerClick onItemRecyclerClick;
    public void setOnItemRecyclerClick(RecyclerArrayViewHolder.OnItemRecyclerClick onItemRecyclerClick){
        this.onItemRecyclerClick = onItemRecyclerClick;
    }
    private ArrayList<M> items = new ArrayList<M>();

    public RecyclerArrayAdapter() {
        setHasStableIds(true);
    }

    public void add(M object) {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, M object) {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends M> collection) {
        if (collection != null) {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(M... items) {
        addAll(Arrays.asList(items));
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(M object) {
        items.remove(object);
        notifyDataSetChanged();
    }

    public M getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean contains(Object dto) {
        return items.contains(dto);
    }
}
