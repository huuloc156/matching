package com.rentracks.matching.fragment.groupchat;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentracks.matching.R;
import com.rentracks.matching.adapter.RecyclerArrayAdapter;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ListDtoData;
import com.rentracks.matching.data.api.dto.chat.GroupItem;
import com.rentracks.matching.data.json.jsonMessage;
import com.rentracks.matching.fragment.AbstractPullAndLoadmoreFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.fragment.timeline.SearchEventHomeFragment;
import com.rentracks.matching.listener.ListenerClose;
import com.rentracks.matching.utils.CommonUtils;
import com.rentracks.matching.utils.LoadImageUtils;
import com.squareup.picasso.Callback;

import org.json.JSONException;

import rx.Observable;

public class GroupChatFragment extends AbstractPullAndLoadmoreFragment implements ListenerClose{

    String searchText = null;

    public static GroupChatFragment getInstance() {
        return new GroupChatFragment();
    }

    @Override
    public int getHeaderRightButtonImageResId() {
        return R.mipmap.add;
    }

    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_SEARCH;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(new BroadcastReceiver() {
                                      @Override
                                      public void onReceive(Context context, Intent intent) {
                                          String messData = intent.getStringExtra(CommonUtils.BROADCAST_EXTRA);
                                          Log.d("GroupChatFragment...", "Receive Mess: " + messData);
                                          try {
                                              GroupItem groupItem = jsonMessage.getInstance(messData).getGroupItem();
                                              if(mAdapter != null){
                                                  for(int i = 0; i< mAdapter.getItemCount(); i++){
                                                      if(mAdapter.getItem(i).group_id == groupItem.group_id){
                                                          mAdapter.getItem(i).last_name = groupItem.last_name;
                                                          mAdapter.getItem(i).last_mess = groupItem.last_mess;
                                                          notifyDataSetChanged();
                                                          break;
                                                      }
                                                  }
                                              }
                                          } catch (JSONException e) {
                                              e.printStackTrace();
                                          }

                                      }
                                  },
                        new IntentFilter(CommonUtils.BROADCAST_INTANT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setDivider(false);
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(preferenceData.getLoadGroupChat() == true){
            preferenceData.setLoadGroupChat(false);
            loadData(1);
        }
    }


    @Override
    protected void loadDirection(boolean is_load_up) {

    }

    ChatGroupAdapter mAdapter;

    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);

        FragmentManager fm = getActivity().getFragmentManager();
        DialogAddGroup mdialog = DialogAddGroup.newInstance("test");
        mdialog.setListener(this);
        mdialog.show(fm, "");
    }

    @Override
    public void close(Object newGroup) {
        //success
        GroupItem item = (GroupItem)newGroup;
        loadData(1);
        goToChat(item);
    }

    @Override
    public void clsee2(Object Objects) {
        //error

    }


    @Override
    public void SearchAction(String s) {
        super.SearchAction(s);
        searchText = s;
        loadData(1);
    }

    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new ChatGroupAdapter();
        }
        return mAdapter;
    }
    @Override
    public void onItemClick(View view, int position) {
//        startFragment(new EventDetailFragment(),true);
        GroupItem item = mAdapter.getItem(position);
        goToChat(item);
    }
    public void goToChat(GroupItem group){
        android.support.v4.app.Fragment fragment = new DetailChatFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("group_detail", group);
        fragment.setArguments(bundle);
        startFragment(fragment,true);
    }

    @Override
    protected void loadData(final int page) {
        Log.d("GroupChatFragment...", "loadData....");
        int limit = 30;
        callApiGroupList(matchingApi.getListGroup(page, searchText, limit), page, limit);
    }


    public void callApiGroupList(Observable<ListDtoData<GroupItem>> objectDtoObservable, final int page, final int limit) {


        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDtoData<GroupItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ListDtoData<GroupItem> events) {
                notifyLoadFail(page);
            }

            @Override
            public void onDataSuccess(ListDtoData<GroupItem> events) {
                int last_page = page + 1;

                if(events.data.size() < limit){
                    last_page = page;
                }
                //step2 notify loaded data
                notifyLoaded(page, last_page, events.data);
                //step 3 add data to list
                if(events.data!=null) {
                    mAdapter.addAll(events.data);
                    notifyDataSetChanged();
                }else{
                    notifyLoadFail(page);
                }
            }
        });
    }



    public static class ChatGroupAdapter extends RecyclerArrayAdapter<GroupItem, SearchEventHomeFragment.SearchItemViewHolder> {


        @Override
        public SearchEventHomeFragment.SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_timeline, parent, false);
            return new SearchEventHomeFragment.SearchItemViewHolder(view,onItemRecyclerClick) ;
        }

        @Override
        public void onBindViewHolder(final SearchEventHomeFragment.SearchItemViewHolder holder, int position) {
            GroupItem item = getItem(position);
            holder.txtTitle.setText(item.group_name);
            String lastMess = "";
            if(item.last_name != null){
                lastMess = item.last_name + " : ";
            }
            if(item.last_mess != null){
                lastMess += (item.last_mess);
            }
            holder.txtDescription.setText(lastMess);
            String picUrl = CommonUtils.getFullPicUrl(holder.img_avt.getContext(), item.getPic());
            LoadImageUtils.load(holder.img_avt.getContext(), picUrl)
                    .error(R.mipmap.noimage)
                    .placeholder(R.drawable.bg_circle)
                    .resizeDimen(R.dimen.avatar_user_size, R.dimen.avatar_user_size)
                    .centerCrop()
//                    .centerInside()
                    .into(holder.img_avt,new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });

        }


    }



}
