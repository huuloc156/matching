package com.rentracks.matching.fragment.myaccount;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ListDtoData;
import com.rentracks.matching.data.api.dto.user.HobbyItem;
import com.rentracks.matching.data.api.dto.user.UserItem;
import com.rentracks.matching.fragment.BaseFragment;
import com.rentracks.matching.fragment.header.ListenerClose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by HuuLoc on 5/29/17.
 */

public class EditAccountFragment1 extends BaseFragment {

    public static EditAccountFragment1 getInstance(){
        return new EditAccountFragment1();
    }
    UserItem mData;
    @BindView(R.id.ll_suggess_hobby_fea1)
    LinearLayout ll_suggess_hobby;
    @BindView(R.id.ll_hobby_fea1)
    LinearLayout ll_hobby;

    ListenerClose mListener;
    public void setListenerClose(ListenerClose l){
        mListener = l;
    }


    List<String> hobbies = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();

        mData = (UserItem) args
                .getParcelable("user_data");
        mCustomHeaderText = getString(R.string.title_edit_account);

        return inflater.inflate(R.layout.fragment_edit_account_1, container, false);
    }

    @Override
    public void onClickHeaderLeftButton(View view) {
        goBack();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callApi();
        addLisHobby();
    }

    protected void callApi() {

        Observable<ListDtoData<HobbyItem>> objectDtoObservable = matchingApi.getHobby();
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ListDtoData<HobbyItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ListDtoData<HobbyItem> events) {
            }

            @Override
            public void onDataSuccess(ListDtoData<HobbyItem> events) {
                if(events.data != null){
                    for(int i =0;i<events.data.size(); i++){
                        addSuggestHobby(events.data.get(i).name);
                    }
                }
            }
        });
    }

    private void addSuggestHobby(final String hobby){
        TextView txt = new TextView(getContext());
        txt.setBackgroundResource(R.drawable.bg_boder_text_padding);
        txt.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        txt.setText("#" + hobby);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHobby(hobby);
            }
        });
        ll_suggess_hobby.addView(txt);
    }

    private void addLisHobby(){
        if(mData == null || mData.hobby == null){
            return;
        }
        List<String> hobbies = Arrays.asList(mData.hobby.split("#"));
        for(int i=0; i < hobbies.size(); i++){
            addHobby(hobbies.get(i));
        }
    }
    protected void addHobby(final String hobby){
        if(hobby.equals("")){
            return;
        }
        if(hobbies.contains(hobby) == false){
            hobbies.add(hobby);
        }else{
            return;
        }
        View ll_row = LayoutInflater.from(getContext()).inflate(
                R.layout.row_text_img_delete, null);
        TextView txt = (TextView)ll_row.findViewById(R.id.txt_row_rtid);
        txt.setText("#" + hobby);
        ImageView img_ = (ImageView)ll_row.findViewById(R.id.img_row_rtid);
        img_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View p = (View) view.getParent();
                ll_hobby.removeView(p);
                hobbies.remove(hobby);
            }
        });
        ll_hobby.addView(ll_row);
    }

    @OnClick(R.id.btn_next_fea1)
    public void clickNext(){
        String listString = "";

        for (String s : hobbies)
        {
            listString += "#" + s.trim();
        }
        mData.hobby = listString;

        EditAccountFragment2 fragment = EditAccountFragment2.getInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user_data",  mData);
        fragment.setListenerClose(mListener);
        fragment.setArguments(bundle);
        startFragment(fragment,true);

    }
}
