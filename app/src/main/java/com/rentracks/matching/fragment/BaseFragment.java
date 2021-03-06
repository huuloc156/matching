package com.rentracks.matching.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.rentracks.matching.MainApplication;
import com.rentracks.matching.R;
import com.rentracks.matching.activity.BaseActivity;
import com.rentracks.matching.activity.MainActivity;
import com.rentracks.matching.data.SharePreferenceData;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.MatchingApi;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.dj.AppComponent;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.fragment.header.IHeaderStateChange;
import com.rentracks.matching.listener.OnBackPressListener;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by lenam on 6/10/16.
 */

public abstract class BaseFragment extends Fragment implements OnBackPressListener,IHeaderInfo {

    @Inject
    protected SharePreferenceData preferenceData;
    @Inject
    protected MatchingApi matchingApi;
    protected int mContainerId = R.id.frame_tab_content2;
    @Inject
    Bus mBus;
    protected boolean isFirstCreate = true;
    protected String mCustomHeaderText;
    boolean isPause;
    BaseActivity baseActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent appComponent = ((MainApplication) getActivity().getApplicationContext()).getAppComponent();
        appComponent.inject(this);
        mBus.register(this);


    }

    protected void listenMessage(String messData){

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity)context;
    }

    @Override
    public void onDestroy() {
        mBus.unregister(this);
        super.onDestroy();
    }

    protected <T> void androidSubcribe(Observable<T> observable, Subscriber<T> subscriber) {
        if (baseActivity != null) {
            baseActivity.androidSubcribe(observable, subscriber);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.tag("kitvs").i("-----onActivityCreated " + this.getClass().getSimpleName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.tag("kitvs").i("<<--onDetach " + this.getClass().getSimpleName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.tag("kitvs").i("<<++++onSaveInstanceState " + this.getClass().getSimpleName());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Timber.tag("kitvs").i(">>++++onViewStateRestored " + this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
        Timber.tag("kitvs").i(">>++++onResume " + this.getClass().getSimpleName());
//        mBus.register(this);
        checkHeader();

    }

    protected void checkHeader() {
        FragmentActivity activity = getActivity();
        if (isVisible() && activity instanceof IHeaderStateChange) {
            //TODO check header
            ((IHeaderStateChange) activity).checkHeaderState();
            if (getParentFragment() instanceof TabFragment) {
                ((IHeaderStateChange) activity).setHeaderInfo(this, mCustomHeaderText);
            }
        }
    }

    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_SEARCH;
    }

    @Override
    public int getHeaderTitleResId() {
        return R.string.app_name;
    }

    @Override
    public int getHeaderRightButtonImageResId() {
        return 0;
    }

    @Override
    public int getHeaderLeftButtonImageResId() {
        return 0;
    }

    @Override
    public void onClickHeaderLeftButton(View view) {

    }

    @Override
    public void onClickHeaderRightButton(View view) {
    }
    @Override
    public boolean haveHeaderBackButton() {
        return true;
    }

    @Override
    public int getHeaderSearchHintResId() {
        return R.string.search_hint_default;
    }

    @Override
    public String getSearchKeyword() {
        return null;
    }

    @Override
    public String saveSearchKeyword(String s) {
        return null;
    }

    @Override
    public void SearchAction(String s) {

    }


    public void goBack() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onPause() {
        isPause = true;
        Timber.tag("kitvs").i("----onPause " + this.getClass().getSimpleName());
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            //TODO to reload data, need restore instance.
            isFirstCreate = true;
        }
        Timber.tag("kitvs").i("----onViewCreated " + this.getClass().getSimpleName() + " save bundle " + savedInstanceState);
        if (getRootFragment() instanceof TabFragment && !(this instanceof TabFragment)) {
            if (((TabFragment) getRootFragment()).isChangeTab) {
                view.clearAnimation();
                ((TabFragment) getRootFragment()).isChangeTab = false;
            }
        }

        view.post(new Runnable() {
            @Override
            public void run() {
                checkHeader();
            }
        });

    }

    @Override
    public void onDestroyView() {
        isFirstCreate = false;
        super.onDestroyView();
    }

    protected BaseFragment getRootFragment() {
        Fragment fragment = getParentFragment();
        while (fragment != null && fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        if (fragment == null) {
            fragment = this;
        }
        return (BaseFragment) fragment;
    }

    public void showProgressDialog() {
        baseActivity.showProgressDialog();
    }

    public void closeDialog() {
        baseActivity.closeDialog();
    }

    public void startFragment(Fragment fragment, boolean isAddToBackStack) {
        startFragment(fragment, mContainerId, isAddToBackStack);
    }

    public void startFragment(Fragment fragment, int containerId, boolean isAddToBackStack) {
        if (isPause) {
            //prevent IllegalStateException of replace fragment
            Timber.w("startFragment prevent IllegalStateException of replace fragment");
            return;
        }
        if (fragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            bundle.putInt("containerId", mContainerId);
            fragment.setArguments(bundle);
        }
        FragmentTransaction ft = getRootFragment().getChildFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit);
        ft.replace(containerId, fragment);

//        ft.setCustomAnimations(R.anim.widget_push_left_in,R.anim.widget_push_right_in,R.anim.widget_push_left_out,R.anim.widget_push_right_out);
        if (isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        //call start activity from root because nest fragment not receive onActivityResult
        if (this instanceof TabFragment) {
            super.startActivityForResult(intent, requestCode);
        } else {
            getRootFragment().startActivityForResult(intent, requestCode);
        }
    }

    public void backToRoot() {
        if (isPause) {
            //prevent IllegalStateException of replace fragment
            Timber.w("backToRoot prevent IllegalStateException of replace fragment");
            return;
        }
        FragmentManager supportFragmentManager = getRootFragment().getChildFragmentManager();
        int stackEntryCount = supportFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < stackEntryCount; i++) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
//        while (stackEntryCount >0) {
//            supportFragmentManager.popBackStackImmediate();
//        }
    }

    @Override
    public boolean onBackPress() {
        return false;
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }


    public void selectTab(int tab){
        getMainActivity().moveToTab(tab);
    }
    public void showMessToast(String mess){
        Toast.makeText(getContext(), mess, Toast.LENGTH_LONG).show();
    }

    protected String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    protected void callUploadPictureApi(Observable<ObjectDto> objectDtoObservable, final View.OnClickListener listener) {

        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto>(this.getActivity(), true) {
            @Override
            protected void onDataError(ObjectDto events) {
                Toast.makeText(getContext(), " upload fail ", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onDataSuccess(ObjectDto events) {
                listener.onClick(null);
            }
        });
    }

    public void showError(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public void showpopupStatus(final boolean status, String content, final View.OnClickListener onClickPositiveButton) {
        getMainActivity().showpopupStatus(status, content, onClickPositiveButton);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
                return true;
            } else {
//                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}
