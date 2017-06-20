package com.rentracks.matching.fragment.groupchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.adapter.RecyclerArrayAdapter;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ListDtoData;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.chat.GroupItem;
import com.rentracks.matching.data.api.dto.user.UserItem;
import com.rentracks.matching.fragment.AbstractPullAndLoadmoreDialogFragment;
import com.rentracks.matching.fragment.timeline.SearchUserHomeFragment;
import com.rentracks.matching.listener.ListenerClose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by HuuLoc on 6/14/17.
 */

public class DialogAddGroup extends AbstractPullAndLoadmoreDialogFragment {

    public static DialogAddGroup newInstance(String title) {
        DialogAddGroup frag = new DialogAddGroup();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    ArrayList<UserItem> listGroupUser = new ArrayList<>();
    SearchUserHomeFragment.SearchUserItemAdapter mAdapter;
    @BindView(R.id.table_layout_add_user)
    GridLayout ll_add_user;
    ListenerClose mListener;
    @BindView(R.id.edt_search_box)
    EditText searchBox;


    public void setListener(ListenerClose l){
        mListener = l;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_add_user_to_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadData(1);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected RecyclerArrayAdapter createAdapter() {
        if(mAdapter == null){
            mAdapter = new  SearchUserHomeFragment.SearchUserItemAdapter();
        }
        mAdapter.setNotshowArrow(true);
        return mAdapter;
    }

    @Override
    protected void loadData(int page) {
        setUILoading(page);
        int distance = 200;
        int limit = 10;
        int age_from = 1;
        int age_to = 40;
        int gender = -1;

        callApiTradeSummary(page, distance, limit, age_from, age_to, gender, searchBox.getText().toString());
    }

    @Override
    public void onItemClick(View view, int position) {

        final UserItem item = mAdapter.getItem(position);
        Log.d("------", "click item " + item.email);
        if(listGroupUser.contains(item)){
            return;
        }
        listGroupUser.add(item);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView txt = new TextView(getActivity());
        txt.setBackgroundResource(R.drawable.bg_boder_text_padding);
        txt.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        txt.setLayoutParams(params);
        txt.setText(item.name);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_add_user.removeView(view);
                listGroupUser.remove(item);
            }
        });
        ll_add_user.addView(txt);

    }

    protected void callApiTradeSummary(final int page,
                                       final int distance,
                                       final int limit,
                                       final int age_from,
                                       final int age_to,
                                       final int gender,
                                       final String search) {

        Observable<ListDtoData<UserItem>> objectDtoObservable = matchingApi.searchUser(page, distance, limit, age_from, age_to, gender, search);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDtoData<UserItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ListDtoData<UserItem> events) {
                notifyLoadFail(page);
            }

            @Override
            public void onDataSuccess(ListDtoData<UserItem> events) {
                if(events.data != null){
                    List<UserItem> items = events.data;
                    int last_page = page + 1;
                    if(items.size() < limit){
                        last_page = page;
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

                }else {
                    notifyLoadFail(page);
                }
            }
        });
    }
    @OnClick(R.id.btn_add_user_cancel)
    public void clickCancel(){
        dismiss();
    }
    @OnClick(R.id.btn_add_user_ok)
    public void clickOk(){
        dismiss();
        if(listGroupUser.size() > 1){
            String groupName = "";
            String user_ids = "";
            for(int i = 0; i<listGroupUser.size(); i++){

                if(i == listGroupUser.size()- 2) {
                    groupName += listGroupUser.get(i).name + "%" + listGroupUser.get(i+1).name;
                    user_ids += listGroupUser.get(i).uid + "," + listGroupUser.get(i+1).uid;
                    break;
                }
//                if(i > 1 && i < listGroupUser.size() -2) {
//                if(listGroupUser.size() > 2 && i == 2) {
//                    groupName += "... ";
//                }else if(listGroupUser.size() > 2 && (i == 0 || i == 1)){
//                    groupName += listGroupUser.get(i).name + ", ";
//                }
                groupName += listGroupUser.get(i).name + "%";
                user_ids += listGroupUser.get(i).uid + ",";
            }

            callApiCreatGroup(matchingApi.createGroup(groupName, user_ids));
        }
    }
    public void callApiCreatGroup(Observable<ObjectDto<GroupItem>> objectDtoObservable) {


        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<GroupItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto<GroupItem> events) {
                if (mListener != null) {
                    mListener.clsee2(events.data);
                }
            }

            @Override
            public void onDataSuccess(ObjectDto<GroupItem> events) {

                if (mListener != null) {
                    mListener.close(events.data);
                }
            }
        });
    }
}
