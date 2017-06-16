package com.rentracks.matching.fragment.groupchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.adapter.RecyclerArrayAdapter;
import com.rentracks.matching.adapter.RecyclerArrayViewHolder;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ListDtoData;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.chat.ChatItem;
import com.rentracks.matching.data.api.dto.chat.GroupItem;
import com.rentracks.matching.data.api.dto.user.UserItem;
import com.rentracks.matching.data.image.dataDrawable;
import com.rentracks.matching.data.image.listImageData;
import com.rentracks.matching.data.json.jsonMessage;
import com.rentracks.matching.fragment.AbstractPullAndLoadmoreFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.listener.ListenerClose;
import com.rentracks.matching.utils.CommonUtils;
import com.rentracks.matching.utils.LoadImageUtils;
import com.rentracks.matching.utils.TimeUtils;
import com.squareup.picasso.Callback;

import org.json.JSONException;

import java.text.ParseException;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class DetailChatFragment extends AbstractPullAndLoadmoreFragment {

    public static DetailChatFragment getInstance() {
        return new DetailChatFragment();
    }
    GroupItem mData;
    String mDateTime = TimeUtils.getCurrentTime();
    public static int userId;
    boolean isEndPage = false;

    @BindView(R.id.edt_chat)
    EditText edtChat;

    UserItem mChatUser;
    Comparator sortChatItem = new Comparator<ChatItem>() {
        @Override
        public int compare(ChatItem o1, ChatItem o2) {
            int  result =0;
            try {
                result = TimeUtils.compareTwoDay(o1.created_at, o2.created_at);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return result;
        }
    };

    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_NORMAL;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(new BroadcastReceiver() {
                          @Override
                          public void onReceive(Context context, Intent intent) {
                              String messData = intent.getStringExtra(CommonUtils.BROADCAST_EXTRA);
                              Log.d("detail...", "Receive Mess: " + messData);
                              try {
                                  ChatItem item = jsonMessage.getInstance(messData).getChatItem();
                                  if(mAdapter != null
                                          && mData != null
                                          && item.group_id == mData.group_id){
                                      mAdapter.add(item);
                                      mAdapter.sort(sortChatItem);
                                      notifyDataSetChanged();
                                  }else if(mAdapter != null
                                          && mChatUser != null
                                          && item.uid == mChatUser.uid){
                                      mAdapter.add(item);
                                      mAdapter.sort(sortChatItem);
                                      notifyDataSetChanged();
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
        isReversScroll = true;
        Bundle args = getArguments();
        if(args != null) {
            mData = (GroupItem) args
                    .getParcelable("group_detail");
            if(mData != null) {
                mCustomHeaderText = mData.group_name;
            }else{
                mChatUser = args.getParcelable("user_detail");
            }
        }
        userId = preferenceData.getUserId();
        setDivider(false);


        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.requestFocusFromTouch();
                return false;
            }
        });
        edtChat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                String text_search = edtChat.getText().toString();

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if(text_search.length() > 0) {
                        chatAction(text_search);
                    }
//                    return true;
                }
                return true;
            }
        });
    }

    ChatAdapter mAdapter;

    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);
    }

    @OnClick(R.id.btn_chat)
    public void onClickChat(){
        String mess = edtChat.getText().toString();
        chatAction(mess);
    }
    public void chatAction(String mess){
        if(mess.isEmpty() == false && mAdapter!= null){
            ChatItem item = new ChatItem();
            item.uid = preferenceData.getUserId();
            item.created_at = TimeUtils.getCurrentTime();
            item.message = mess;
            mAdapter.add(item);
            mAdapter.sort(sortChatItem);
            notifyDataSetChanged();
            edtChat.setText("");
            mRecyclerView.scrollToPosition(0);

            if(mData != null ) {
                callApiChat(matchingApi.sendMessToGroup(mData.group_id, mess, "no-title"));
            }else if(mChatUser != null){
                callApiChat(matchingApi.sendMessToUser(mChatUser.uid, mess, "no-title"));
            }
        }
    }
    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new ChatAdapter();
        }
        return mAdapter;
    }
    @Override
    public void onItemClick(View view, int position) {
//        startFragment(new EventDetailFragment(),true);
    }
    @Override
    protected void loadDirection(boolean is_load_up) {
//        if(mAdapter.getItemCount() > 0){
//            if(is_load_up == false){
//                mDateTime = mAdapter.getItem(0).created_at;
//            }else{
//                mDateTime = mAdapter.getItem(mAdapter.getItemCount()-1).created_at;
//            }
//        }
    }
    @Override
    protected void loadData(final int page) {
        if(isEndPage){
            notifyLoadFail(page);
            return;
        }
        int limit = 5;

        if(mData != null) {
            callApiGroupList(matchingApi.getListChat(mData.group_id, mDateTime, limit), page, limit);
        }else if(mChatUser != null){
            callApiGroupList(matchingApi.getList2UserChat(mChatUser.uid, mDateTime, limit), page, limit);
        }
    }


    public void callApiGroupList(Observable<ListDtoData<ChatItem>> objectDtoObservable, final int page, final int limit) {


        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDtoData<ChatItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ListDtoData<ChatItem> events) {
                notifyLoadFail(page);
            }

            @Override
            public void onDataSuccess(ListDtoData<ChatItem> events) {
                int last_page = page + 1;

                if(events.data.size() < limit){
                    last_page = page;
                    isEndPage = true;
                }
                //step2 notify loaded data
                notifyLoaded(page, last_page, events.data);
                //step 3 add data to list
                if(events.data !=null && events.data.size() > 0 ) {
                    mAdapter.addAll(events.data);

                    mAdapter.sort(sortChatItem);
                    if(mAdapter.getItemCount() > 0) {
                        mDateTime = mAdapter.getItem(mAdapter.getItemCount() - 1).created_at;
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }


    public static class ChatAdapter extends RecyclerArrayAdapter<ChatItem, ChatItemViewHolder> {
        @Override
        public ChatItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat, parent, false);
            return new ChatItemViewHolder(view,onItemRecyclerClick) ;
        }

        @Override
        public void onBindViewHolder(final ChatItemViewHolder holder, int position) {
            if(position < 0){
                return;
            }
            final ChatItem item = getItem(position);
            holder.txtMessage.setText(item.message);

            if(item.uid == userId){
                holder.llChat.setGravity(Gravity.RIGHT);
                holder.img_avt.setVisibility(View.GONE);
                holder.txtMessage.setBackgroundResource(R.mipmap.balloon_blue);
                if(position > 0) {
                    ChatItem item_pre = getItem(position - 1);
                    if (item_pre.uid == item.uid) {
                        holder.txtMessage.setBackgroundResource(R.mipmap.balloon_blue_circle);
                    }
                }
            }else{
                holder.llChat.setGravity(Gravity.LEFT);
                holder.txtMessage.setBackgroundResource(R.mipmap.balloon_gray);
                holder.img_avt.setVisibility(View.VISIBLE);
                if(position > 0){
                    ChatItem item_pre = getItem(position-1);
                    if(item_pre.uid == item.uid){
                        holder.img_avt.setVisibility(View.GONE);
                        holder.txtMessage.setBackgroundResource(R.mipmap.balloon_gray_circle);
                    }
                }
                dataDrawable mDrawable = listImageData.getInstance().getWithId(item.uid);
                if(mDrawable == null) {
                    Log.d("ChatDetail...", "Load new image" );
                    String picUrl = CommonUtils.getFullPicUrl(holder.img_avt.getContext(), item.getPic());
                    LoadImageUtils.load(holder.img_avt.getContext(), picUrl)
                            .error(R.mipmap.noimage)
                            .placeholder(R.drawable.bg_circle)
                            .resizeDimen(R.dimen.avatar_user_size, R.dimen.avatar_user_size)
                            .centerCrop()
                            .into(holder.img_avt, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Log.d("ChatDetail...", "Load new image -- onSuccess" );
                                    dataDrawable itemDrawable = new dataDrawable(item.uid, holder.img_avt.getDrawable());
                                    listImageData.getInstance().add(itemDrawable);
                                    holder.progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    Log.d("ChatDetail...", "Load new image -- onError" );
                                    dataDrawable itemDrawable = new dataDrawable(item.uid, holder.img_avt.getDrawable());
                                    listImageData.getInstance().add(itemDrawable);
                                    holder.progressBar.setVisibility(View.GONE);
                                }
                            });
                }else{
                    Log.d("ChatDetail...", "Load old image" );
                    holder.img_avt.setImageDrawable(listImageData.getInstance().getWithId(item.uid).getDrawable());
                }

            }
            holder.progressBar.setVisibility(View.GONE);


        }


    }

    public static class ChatItemViewHolder extends RecyclerArrayViewHolder {

        @BindView(R.id.img_rit)
        public ImageView img_avt;
        @BindView(R.id.txt_chat)
        public TextView txtMessage;
        @BindView(R.id.progress_rit)
        public ProgressBar progressBar;
        @BindView(R.id.ll_text_chat)
        LinearLayout llChat;

        public ChatItemViewHolder(View itemView, OnItemRecyclerClick onItemRecyclerClick) {
            super(itemView,onItemRecyclerClick);
            ButterKnife.bind(this, itemView);
        }

    }

    public void callApiChat(Observable<ObjectDto> objectDtoObservable ) {

        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto>(this.getMainActivity(), true) {
            @Override
            protected void onDataError(ObjectDto events) {

            }

            @Override
            public void onDataSuccess(ObjectDto events) {
            }

        });
    }

}
