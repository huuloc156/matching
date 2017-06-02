package com.rentracks.matching.fragment.myaccount;

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
import android.widget.Toast;

import com.rentracks.matching.R;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.user.UserItem;
import com.rentracks.matching.fragment.BaseFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.fragment.header.ListenerClose;
import com.rentracks.matching.utils.CommonUtils;
import com.rentracks.matching.utils.LoadImageUtils;
import com.squareup.picasso.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

import static com.rentracks.matching.R.array.gender;

/**
 * Created by HuuLoc on 5/29/17.
 */

public class EditAccountFragment2 extends BaseFragment{

    UserItem mData;
    @BindView(R.id.img_fed)
    ImageView imgAvt;
    @BindView(R.id.progress_fed)
    ProgressBar progressBar;
    @BindView(R.id.edt_name_fea2)
    EditText edt_name;
    @BindView(R.id.spn_age_fea2)
    Spinner spn_age;
    @BindView(R.id.spn_gender_fea2)
    Spinner spn_gender;
    @BindView(R.id.spn_location)
    Spinner spn_location;
    @BindView(R.id.edt_descripton_fea2)
    EditText edt_description;
    ListenerClose mListener;
    public void setListenerClose(ListenerClose l){
        mListener = l;
    }

    public static EditAccountFragment2 getInstance(){
        return new EditAccountFragment2();
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
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_NORMAL;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();

        mData = (UserItem) args
                .getParcelable("user_data");
        mCustomHeaderText = getString(R.string.title_edit_account);
        return inflater.inflate(R.layout.fragment_edit_account_2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillData();
    }
    private void fillData(){

       loadAvt();

        edt_name.setText(mData.name);

        ArrayAdapter<CharSequence> adapter_age = ArrayAdapter.createFromResource(getContext(),  R.array.age_full, android.R.layout.simple_spinner_item);
        adapter_age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_age.setAdapter(adapter_age);
        if (!"".equals(mData.age)) {
            int spinnerPosition = adapter_age.getPosition(mData.age+"");
            spn_age.setSelection(spinnerPosition);
        }

        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(getContext(),  gender, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_gender.setAdapter(adapter_gender);
        if (!"".equals(mData.gender)) {
            int spinnerPosition = adapter_age.getPosition(mData.gender+"");
            spn_gender.setSelection(spinnerPosition);
        }

        /* location */
        ArrayAdapter<String> adapter_countries = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, getCountry());
        spn_location.setAdapter(adapter_countries);
        if (!"".equals(mData.location)) {
            int spinnerPosition = adapter_countries.getPosition(mData.location);
            spn_location.setSelection(spinnerPosition);
        }
        edt_description.setText(mData.description);
    }

    public static int REQUEST_GALLERY_CODE = 112;
    private Uri uri;

    @OnClick(R.id.img_fed)
    public void clickAvt(){
        CommonUtils.verifyStoragePermissions(getActivity());

        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK){
            uri = data.getData();

//            if(EasyPermissions.hasPermissions(this, Manifestnifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, getActivity());
                File file = new File(filePath);
                Log.d("", "Filename " + file.getName());
                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                setAvt(data);

                callUploadPictureApi(matchingApi.uploadFile(fileToUpload, filename), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.close(data);
                    }
                });

//            }else{
//                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
//            }
        }
    }




    protected void callEditApi() {
        mData.name = edt_name.getText().toString();
        if("".equals(mData.name)){
            showError("Name is Null");
            return;
        }
        String age_s = (String)spn_age.getSelectedItem();
        mData.age = Integer.valueOf(age_s);
        String gender_s = (String) spn_gender.getSelectedItem();
        mData.gender = 1;
        if(gender_s.equals(getResources().getStringArray(gender)[0])){
            mData.gender = 0;
        }
        mData.location = (String) spn_location.getSelectedItem();
        mData.description = edt_description.getText().toString();
        Observable<ObjectDto> objectDtoObservable = matchingApi.EditUser(mData.name, mData.gender, mData.age, mData.location, mData.hobby, mData.description);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto events) {
                Toast.makeText(getContext(), " upload fail ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDataSuccess(ObjectDto events) {
                finish_ok();
            }
        });
    }
    @OnClick(R.id.btn_ok_fea2)
    public void finish_edit(){
        callEditApi();
    }

    public void finish_ok(){
        backToRoot();
        mListener.clsee2(mData);
//        startFragment(new AccountFragment(),true);
//        selectTab(3);
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
    private void loadAvt(){
        String picUrl = CommonUtils.getFullPicUrl(getContext(), mData.getPic());
        LoadImageUtils.load(getContext(), picUrl)
                .error(R.mipmap.noimage)
                .placeholder(R.drawable.bg_circle)
//                .resizeDimen(R.dimen.avatar_user_size, R.dimen.avatar_user_size)
//                .centerInside()
                .fit()
                .into(imgAvt,new Callback() {
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
    public void showError(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public ArrayList<String> getCountry(){
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        return countries;
    }
}
