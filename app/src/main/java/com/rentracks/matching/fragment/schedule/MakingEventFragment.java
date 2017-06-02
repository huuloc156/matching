package com.rentracks.matching.fragment.schedule;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.rentracks.matching.R;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.search.EventSearchItem;
import com.rentracks.matching.data.api.dto.search.PlaceItem;
import com.rentracks.matching.fragment.BaseFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.fragment.header.ListenerClose;
import com.rentracks.matching.utils.CommonUtils;
import com.rentracks.matching.utils.LoadImageUtils;
import com.rentracks.matching.utils.TimeUtils;
import com.squareup.picasso.Callback;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public class MakingEventFragment extends BaseFragment implements ListenerClose {

    public static MakingEventFragment getInstance() {
        return new MakingEventFragment();
    }

    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_NORMAL;
    }

    @BindView(R.id.txt_row_spinner)
    TextView txtNameSpinner;
    @BindView(R.id.txt_name_fem)
    EditText txtTitle;
    @BindView(R.id.txt_place_fem)
    TextView txtPlace;
    @BindView(R.id.txt_datetime_fem)
    TextView txtTime;
    @BindView(R.id.txt_description_fem)
    EditText txtDescription;
    @BindView(R.id.spin_row_spinner)
    Spinner spnNumPeople;
    @BindView(R.id.img_fed)
    ImageView imgPlace;
    @BindView(R.id.progress_fed)
    ProgressBar progressBar;
    PlaceItem place;
    PlaceItem newPlace;
    Date mDateTime;

    EventSearchItem mData;
    Intent mDataPicture;

    ListenerClose mListener;
    public void setListenerClose(ListenerClose l){
        mListener = l;
    }

    @Override
    public void close(Object listOfObjects) {
        newPlace = (PlaceItem) listOfObjects;
    }

    @Override
    public void clsee2(Object Objects) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mData = (EventSearchItem) args
                    .getParcelable("event_detail");
            place = (PlaceItem) args
                    .getParcelable("place_detail");
            mCustomHeaderText = "Edit Event";
        }
        if(newPlace != null){
            place = newPlace;
        }
        return inflater.inflate(R.layout.fragment_event_make, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtNameSpinner.setText("Max People: ");
        progressBar.setVisibility(View.GONE);
        if (place != null) {
            txtPlace.setText(place.name);
        }

        if (mData != null) {
            txtTitle.setText(mData.title);
            mDateTime = TimeUtils.createDate(mData.start_date + " " + mData.start_time, "yyyy-MM-dd HH:mm:ss");
            setTimeView();
//            set max people
            ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(getContext(), R.array.num_people, android.R.layout.simple_spinner_item);
            adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnNumPeople.setAdapter(adapter_gender);
            int spinnerPosition = adapter_gender.getPosition(mData.max_member + "");
            spnNumPeople.setSelection(spinnerPosition);

            txtDescription.setText(mData.description);

            String picUrl = CommonUtils.getFullPicUrl(getContext(), mData.getPic());
            LoadImageUtils.load(getContext(), picUrl)
                    .error(R.mipmap.noimage)
                    .placeholder(R.drawable.bg_circle)
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
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            // Do something with the date. This Date object contains
            // the date and time that the user has selected.
            mDateTime = date;
            setTimeView();
        }

        @Override
        public void onDateTimeCancel() {
            // Overriding onDateTimeCancel() is optional.
        }
    };

    public void setTimeView() {
        if (mDateTime != null) {
            String datetime = TimeUtils.changeDateFormat(mDateTime, "yyyy/MM/dd HH:mm");
            txtTime.setText(datetime);
        }
    }

    @OnClick(R.id.txt_datetime_fem)
    public void onClickDateTime() {
        new SlideDateTimePicker.Builder(getMainActivity().getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                .build()
                .show();
    }

    @OnClick(R.id.txt_place_fem)
    public void onClickPlace() {
        MapEventPlace fragment = (MapEventPlace) MapEventPlace.getInstance();
        fragment.setListenerClose(this);
        startFragment(fragment, true);
    }



    @OnClick(R.id.btn_next_fem)
    public void clickCreateEvent() {
        if (place == null) {
            showMessToast("Please select place!");
            return;
        }
        if (txtTitle.getText().toString().length() < 1) {
            showMessToast("Title is null");
            return;
        }
        if (mDateTime == null) {
            showMessToast("Please select date!");
            return;
        }
        int max_mem = Integer.valueOf((String) spnNumPeople.getSelectedItem());
        String start_date = TimeUtils.changeDateFormat(mDateTime, "yyyy-MM-dd");
        String start_time = TimeUtils.changeDateFormat(mDateTime, "HH:mm:ss");

        callApiTradeSummary(txtTitle.getText().toString(),
                max_mem,
                txtDescription.getText().toString(),
                start_date,
                start_time,
                place.name,
                place.formatted_address,
                place.geometry.location.lat,
                place.geometry.location.lng
        );

    }

    protected void callApiTradeSummary(String title,
                                       int max_member,
                                       String description,
                                       String start_date,
                                       String start_time,
                                       String place_name,
                                       String address,
                                       Double lat,
                                       Double lng) {
        Observable<ObjectDto<EventSearchItem>> objectDtoObservable = matchingApi.createEvent(title, max_member, description, start_date, start_time, place_name, address, lat, lng);

        if (mData != null && mData.eid != 0) {
            objectDtoObservable = matchingApi.editEvent(mData.eid, title, max_member, description, start_date, start_time, place_name, address, lat, lng);
            mData.title = title;
            mData.max_member = max_member;
            mData.description = description;
            mData.start_date = start_date;
            mData.start_time = start_time;
            mData.place_name = place_name;
            mData.address = address;
            mData.latitude = lat;
            mData.longitude = lng;
        }
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto<EventSearchItem>>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto<EventSearchItem> events) {
                showMessToast("create Error");
            }

            @Override
            public void onDataSuccess(ObjectDto<EventSearchItem> events) {
                showMessToast("create success");
                if(mData != null && mData.eid != 0){//edit
                    uploadPicEvent(mData.eid);
                }else {
                    uploadPicEvent(events.data.eid);
                }
            }
        });
    }


    /*change picture event*/

    public static int REQUEST_GALLERY_CODE = 112;


    @OnClick(R.id.img_fed)
    public void clickAvt() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            mDataPicture = data;

            setAvt(data);
        }
    }

    private void uploadPicEvent(int idEvent) {

        if (mDataPicture == null) {
            finishMakingEvent();
            return;
        }

        Uri uri = mDataPicture.getData();
//            if(EasyPermissions.hasPermissions(this, Manifestnifest.permission.READ_EXTERNAL_STORAGE)) {
        String filePath = getRealPathFromURIPath(uri, getActivity());
        File file = new File(filePath);
        Log.d("", "Filename " + file.getName());
        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        callUploadPictureApi(matchingApi.uploadPictureEvent(fileToUpload, filename, idEvent), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishMakingEvent();
            }
        });

    }

    private void setAvt(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        imgPlace.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }
    private void finishMakingEvent(){
        if(mListener != null){
            mListener.close(mData);
        }
        goBack();
    }
}
