package com.rentracks.matching.fragment.myaccount;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.user.UserItem;
import com.rentracks.matching.fragment.BaseFragment;
import com.rentracks.matching.fragment.groupchat.DetailChatFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.listener.ListenerClose;
import com.rentracks.matching.utils.CommonUtils;
import com.rentracks.matching.utils.LoadImageUtils;
import com.squareup.picasso.Callback;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by apple on 6/20/16.
 */
public class AccountFragment extends BaseFragment implements ListenerClose{

    @BindView(R.id.img_fed)
    ImageView imgAvt;
    @BindView(R.id.progress_fed)
    ProgressBar progressBar;
    @BindView(R.id.txt_location_fmh)
    TextView txtLocation;
    @BindView(R.id.txt_age_gender)
    TextView txtAgeGender;
    @BindView(R.id.edt_descripton_fmh)
    EditText edtDescription;
    @BindView(R.id.ll_table_fmh)
    TableLayout ll_table;
    @BindView(R.id.ll_chat_friend_child)
    LinearLayout llChatFriend;
    @BindView(R.id.img_chat)
            ImageView imgChat;
    @BindView(R.id.img_make_friend)
            ImageView imgFriend;


    UserItem mData;
    UserItem mNewData;
    boolean isOwner = true;
    Intent mDataAvt;
    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_NORMAL;
    }

    @Override
    public int getHeaderRightButtonImageResId() {
        if(isOwner == false){
            return 0;
        }
        return R.mipmap.menu_1;
    }

    @Override
    public int getHeaderLeftButtonImageResId() {
        return R.mipmap.back;
    }

    @Override
    public void onClickHeaderLeftButton(View view) {
        goBack();
    }

    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);
        clickEditAccount();
    }
    private void clickEditAccount(){
        EditAccountFragment1 fragment = EditAccountFragment1.getInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user_data",  mData);
        fragment.setListenerClose(this);
        fragment.setArguments(bundle);
        startFragment(fragment,true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(args != null) {
            mData = (UserItem) args
                    .getParcelable("user_detail");
            isOwner = false;
        }
        if(mNewData != null){
            mData = mNewData;
        }
        return inflater.inflate(R.layout.fragment_myaccount_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        llChatFriend.setVisibility(View.GONE);
        if(isOwner == false){
            llChatFriend.setVisibility(View.VISIBLE);
        }
        if(mData != null){
            setUserView();
        }else{
            callApi();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (edtDescription != null && mData != null){
            edtDescription.setText(mData.description);
        }
    }

    protected void callApi() {
        int uid = 0;
        if(mData != null){
            uid = mData.uid;
        }
        Observable<ObjectDto<UserItem>> objectDtoObservable = matchingApi.userDetail(uid);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<UserItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto<UserItem> events) {
            }

            @Override
            public void onDataSuccess(ObjectDto<UserItem> events) {
                if(events.data != null){

                    mData = events.data;
                    if(mData.name == null || mData.name.equals("") ){
                        clickEditAccount();
                    }else{
                        preferenceData.setUserName(mData.name);
                        setUserView();
                    }
                }
            }
        });
    }


    public void setUserView(){
        setFriendStatus();
        mCustomHeaderText = mData.name;

        checkHeader();
        txtLocation.setText(mData.location);
        txtAgeGender.setText(mData.age +" Age  /  " + ((mData.gender == 0)? "Male":"Female"));

        if(mDataAvt != null){
            setAvt(mDataAvt);
            progressBar.setVisibility(View.GONE);
        }else {
            String picUrl = CommonUtils.getFullPicUrl(getContext(), mData.getPic());
            LoadImageUtils.load(getContext(), picUrl)
                    .error(R.mipmap.noimage)
//                .placeholder(R.drawable.bg_circle)
//                            .resizeDimen(R.dimen.avatar_user_size, R.dimen.avatar_user_size)
//                            .centerCrop()
                    .fit()
                    .into(imgAvt, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
        edtDescription.setText(mData.description);
        edtDescription.invalidate();
        edtDescription.setFocusable(false);
        edtDescription.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        edtDescription.setClickable(false);


        int max_col = 4;
        int row_i = 0;
        if(mData.hobby == null){
            return;
        }
        List<String> hobbies = Arrays.asList(mData.hobby.split("#"));
        for(int i = 0; i<hobbies.size(); i += max_col){
            TableRow row = new TableRow(getContext());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            row.setGravity(Gravity.CENTER);
//                        row.setBackgroundColor(Color.parseColor("#fffEEf"));
            row_i = 0;
            for(int j = 0; row_i< max_col && (j+i) < hobbies.size(); j++) {
                if(hobbies.get(i+ j).equals("")){
                    continue;
                }
                TextView txt = new TextView(getContext());
                txt.setBackgroundResource(R.drawable.bg_boder_text_padding);
                txt.setGravity(Gravity.CENTER);
                row_i++;
                txt.setText("#" + hobbies.get(i+j) );

                row.addView(txt);
            }

            ll_table.addView(row);
        }
    }
    public void setFriendStatus(){
        imgFriend.setImageResource(R.mipmap.shake_hand_1);
        if(mData != null && mData.is_friend > -1){
            if(mData.is_friend == 0){//pedding
                imgFriend.setImageResource(R.mipmap.shake_hand_2);
            }else{//is friend
                imgFriend.setImageResource(R.mipmap.shake_hand_3);
            }
        }
    }

    @OnClick(R.id.ll_chat)
    public void clickChat(){
        preferenceData.setLoadGroupChat(true);
        android.support.v4.app.Fragment fragment = new DetailChatFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user_detail", mData);
        fragment.setArguments(bundle);
        startFragment(fragment,true);
    }
    @OnClick(R.id.ll_friend)
    public void clickFriend(){
        if(mData != null ){
            if(mData.is_friend == 0){//pedding
                //send accept pedding
//                imgFriend.setImageResource(R.mipmap.shake_hand_3);
//                mData.is_friend = -1;
//                callApiFriend(matchingApi.acceptRequest(mData.uid));

            }else if(mData.is_friend == 1) {//is friend
                //send cancel friend
                imgFriend.setImageResource(R.mipmap.shake_hand_1);
                mData.is_friend = -1;
                callApiFriend(matchingApi.cancelRequest(mData.uid));
            }else if(mData.is_friend == -1){//no thing
                //send make friend
                imgFriend.setImageResource(R.mipmap.shake_hand_2);
                mData.is_friend = 0;
                callApiFriend(matchingApi.sendRequest(mData.uid));
            }
        }
    }
    public void callApiFriend( Observable<ObjectDto> objectDtoObservable){
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto events) {

            }


            @Override
            public void onDataSuccess(ObjectDto events) {

            }
        });
    }

    @Override
    public void close(Object listOfObjects) {
        mDataAvt = (Intent) listOfObjects;
    }

    @Override
    public void clsee2(Object Objects) {
        mNewData = (UserItem) Objects;
    }

    private void setAvt(Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        imgAvt.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }
}
