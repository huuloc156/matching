package com.rentracks.matching.fragment.timeline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.search.EventSearchItem;
import com.rentracks.matching.data.api.dto.search.PlaceItem;
import com.rentracks.matching.data.api.dto.search.c_geometry;
import com.rentracks.matching.fragment.BaseFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.fragment.header.ListenerClose;
import com.rentracks.matching.fragment.schedule.MakingEventFragment;
import com.rentracks.matching.utils.CommonUtils;
import com.rentracks.matching.utils.LoadImageUtils;
import com.squareup.picasso.Callback;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

public class EventDetailFragment extends BaseFragment implements ListenerClose {

    @Override
    public int getHeaderRightButtonImageResId() {
        if(mData != null && mData.is_host == 1) {
            return R.mipmap.menu_1;
        }
        return 0;
    }

    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_NORMAL;
    }

    @BindView(R.id.txt_title_fed)
            TextView txtTitle;
    @BindView(R.id.txt_join_fed)
            TextView txtJoin;
    @BindView(R.id.txt_like_fed)
            TextView txtLike;
    @BindView(R.id.txt_name_fed)
            TextView txtName;
    @BindView(R.id.txt_time_fed)
            TextView txtTime;
    @BindView(R.id.txt_num_people_fed)
            TextView txtNumPeople;
    @BindView(R.id.txt_description_fed)
            TextView txtDescription;
    @BindView(R.id.img_fed)
    ImageView imgPlace;
    @BindView(R.id.img_join_fed)
    ImageView imgJoin;
    @BindView(R.id.img_like_fed)
    ImageView imgLike;
    @BindView(R.id.progress_fed)
    ProgressBar progressBar;

    EventSearchItem mData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        mData = (EventSearchItem) args
                .getParcelable("event_detail");
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mData != null){
            txtTitle.setText(mData.title);
            txtName.setText(mData.place_name);
            txtTime.setText(mData.start_date +" "+ mData.start_time);
            txtNumPeople.setText("[Now]"+mData.join_member.size() + " people / [Max]" + mData.max_member+ " people");
            txtDescription.setText(mData.description);
            if(mData.is_join != 0){
                imgJoin.setImageResource(R.mipmap.star_yellow_l);
            }
            if(mData.is_like != 0){
                imgLike.setImageResource(R.mipmap.liked_l);
            }

            loadPicEvent();
        }
    }


    @Override
    public void onClickHeaderRightButton(View view) {
        super.onClickHeaderRightButton(view);
        PlaceItem place = new PlaceItem();
        place.name = mData.place_name;

        c_geometry geo = new c_geometry(mData.latitude, mData.longitude);
        place.geometry = geo;

        MakingEventFragment fragment = new MakingEventFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("event_detail", mData);
        bundle.putParcelable("place_detail", place);
        fragment.setListenerClose(this);
        fragment.setArguments(bundle);
        startFragment(fragment,true);
    }

    @OnClick(R.id.ll_join)
    public void clickJoin(){
        if(mData.is_join != 0){//joined
            /* do un-join */
            callApiEvent(0, mData.eid, 0);
            mData.is_join = 0;
        }else{
            /* do join */
            callApiEvent(0, mData.eid, 1);
            mData.is_join = 1;
        }
        changeJoinImage(mData.is_join);
    }

    @OnClick(R.id.ll_like)
    public void clickLike(){
        if(mData.is_like != 0){
            callApiEvent(1, mData.eid, 0);
            mData.is_like = 0;
        }else{
            callApiEvent(1, mData.eid, 1);
            mData.is_like = 1;
        }
        changeLikeImage(mData.is_like);
    }

    protected void callApiEvent(final int action, final int eid, final int data) {

        Observable<ObjectDto<Integer>> objectDtoObservable = matchingApi.joinEvent(eid);
        switch(action){
            case 0: //join
                if(data == 0){//un-join
                    objectDtoObservable = matchingApi.un_joinEvent(eid);
                }
                break;
            case 1: //like
                if(data == 0){//un-like
                    objectDtoObservable = matchingApi.un_likeEvent(eid);
                }else{
                    objectDtoObservable = matchingApi.likeEvent(eid);
                }
                break;
            default:
                break;
        }


        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<Integer>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto<Integer> events) {

            }

            @Override
            public void onDataSuccess(ObjectDto<Integer> events) {
                if(events.data > 0){
                    /*success*/
                    if(action == 0) {
                        changeJoinImage(data);
                    }else if(action == 1) {
                        changeLikeImage(data);
                    }
                }
            }
        });
    }
    public void changeJoinImage(int states){
        if(mData.is_join != states) {
            mData.is_join = states;
        }
        if(states != 0){
            imgJoin.setImageResource(R.mipmap.star_yellow_l);
        }else{
            imgJoin.setImageResource(R.mipmap.star_gray_l);
        }
    }
    public void changeLikeImage(int states){
        if(mData.is_like != states){
            mData.is_like = states;
        }
        if(states != 0){
            imgLike.setImageResource(R.mipmap.liked_l);
        }else{
            imgLike.setImageResource(R.mipmap.like_l);
        }
    }

    private void loadPicEvent(){
        String picUrl = CommonUtils.getFullPicUrl(getContext(), mData.getPic());
        LoadImageUtils.load(getContext(), picUrl)
                .error(R.mipmap.noimage)
                .placeholder(R.drawable.bg_circle)
//                    .resizeDimen(R.dimen.avatar_user_size, R.dimen.avatar_user_size)
//                    .centerInside()
                .fit()
                .into(imgPlace,new Callback() {
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

    @Override
    public void close(Object listOfObjects) {

        mData = (EventSearchItem) listOfObjects;

        Observable<ObjectDto<EventSearchItem>> objectDtoObservable = matchingApi.getEventPic(mData.eid);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<EventSearchItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto<EventSearchItem> events) {

            }

            @Override
            public void onDataSuccess(ObjectDto<EventSearchItem> events) {
                if(events != null){
                    mData.pic = events.data.pic;
                    loadPicEvent();
                }
            }
        });
    }

    @Override
    public void clsee2(Object Objects) {

    }
}
