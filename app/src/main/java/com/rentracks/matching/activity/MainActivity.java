package com.rentracks.matching.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rentracks.matching.R;
import com.rentracks.matching.adapter.DrawerAdapter;
import com.rentracks.matching.data.api.ApiSubscriber;
import com.rentracks.matching.data.api.dto.ObjectDto;
import com.rentracks.matching.data.api.dto.menu.DrawerItem;
import com.rentracks.matching.data.api.dto.user.LocationData;
import com.rentracks.matching.fragment.TabFragment;
import com.rentracks.matching.fragment.events.OnSearchEvent;
import com.rentracks.matching.fragment.groupchat.GroupChatFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.fragment.header.IHeaderStateChange;
import com.rentracks.matching.fragment.myaccount.AccountFragment;
import com.rentracks.matching.fragment.schedule.ScheduleFragment;
import com.rentracks.matching.fragment.timeline.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import timber.log.Timber;

public class MainActivity extends BaseTabHostActivity implements IHeaderStateChange {
    /**
     * Custom Header View
     */
    @BindView(R.id.header)
    View mHeader;
    @BindView(R.id.searchView)
    View headerSearchView;
    @BindView(R.id.edt_search_box)
    public EditText edtHeaderSearchBox;
    @BindView(R.id.btn_header_left)
    ImageView btnHeaderLeft;
    @BindView(R.id.btn_header_delete)
    ImageView btnHeaderDeleteText;
    @BindView(R.id.btn_header_search)
    View btnHeaderSearch;
    @BindView(R.id.btn_header_right)
    ImageView btnHeaderRight;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    private IHeaderInfo mCurrentHeaderInfo;

    @BindView(R.id.navList)
     ListView mDrawerList;
    @BindView(R.id.drawer_layout)
     DrawerLayout mDrawerLayout;

    List<DrawerItem> dataList;

     DrawerAdapter mAdapter;
     ActionBarDrawerToggle mDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setupHeader();

        addDrawerItems();
        setupDrawer();

//        moveToTab(1);
        initLocation();
    }

    protected int getTabLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTabs() {
        //TODO setup your tab
        setupTab("Tab1", "TimeLine", R.mipmap.timeline, HomeFragment.class);
        setupTab("Tab2", "Schedule", R.mipmap.schedule, ScheduleFragment.class);
        setupTab("Tab3", "Group/Chat", R.mipmap.chat, GroupChatFragment.class);
        setupTab("Tab4", "My Account", R.mipmap.account, AccountFragment.class);

    }

    //////////////////////////
    ///Header/////////////////
    //////////////////////////
    private void setupHeader() {
        edtHeaderSearchBox.clearFocus();
        edtHeaderSearchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {Timber.d("search box focus " + hasFocus);
                if (!hasFocus) {
                    hideKeyBoard(edtHeaderSearchBox);
                } else {

                }
                mBus.post(new OnSearchEvent(hasFocus));
            }
        });
//        btnHeaderSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mCurrentHeaderInfo != null && mCurrentHeaderInfo.getHeaderMode() == IHeaderInfo.HEADER_MODE_SEARCH_INACTIVE) {
//                    mBus.post(new OnSearchEvent(true));
//                    edtHeaderSearchBox.requestFocus();
//                }
//            }
//        });
        edtHeaderSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                String text_search = edtHeaderSearchBox.getText().toString();
                if(text_search.length() > 0) {
                    mCurrentHeaderInfo.SearchAction(text_search);

                }
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (mCurrentHeaderInfo != null && mCurrentHeaderInfo.getHeaderMode() == IHeaderInfo.HEADER_MODE_SEARCH) {
                        //TODO need add search action in header info
                        mCurrentHeaderInfo.onClickHeaderRightButton(null);
                    }
                    return true;
                }
                return false;
            }
        });
        edtHeaderSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    btnHeaderDeleteText.setVisibility(View.VISIBLE);
                } else {
                    btnHeaderDeleteText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void checkHeaderState() {
        String currentTabTag = mTabHost.getCurrentTabTag();
        Fragment currentTabfragment = getSupportFragmentManager().findFragmentByTag(currentTabTag);
        if (currentTabfragment != null) {
            FragmentManager childFragmentManager = currentTabfragment.getChildFragmentManager();
            if (childFragmentManager.getBackStackEntryCount() > 0 &&
                    (mCurrentHeaderInfo != null && mCurrentHeaderInfo.haveHeaderBackButton())) {
                btnHeaderLeft.setVisibility(View.VISIBLE);
                btnHeaderLeft.setImageResource(R.mipmap.back);
            } else {
                //TODO need flexible set header state
                if (mCurrentHeaderInfo != null && mCurrentHeaderInfo.getHeaderLeftButtonImageResId() != 0) {
                    btnHeaderLeft.setVisibility(View.VISIBLE);
                } else {
                    btnHeaderLeft.setVisibility(View.INVISIBLE);
                }
            }
            if(mCurrentHeaderInfo != null && mCurrentHeaderInfo.getHeaderMode() == IHeaderInfo.HEADER_MODE_SEARCH){
//                String s = mCurrentHeaderInfo.getSearchKeyword();
//                edtHeaderSearchBox.setText(s);
                headerSearchView.setVisibility(View.VISIBLE);
                edtHeaderSearchBox.setText(mCurrentHeaderInfo.getSearchKeyword());
            }
        }
    }

    @Override
    public void setHeaderInfo(IHeaderInfo headerInfo, String mCustomHeaderText) {
        mCurrentHeaderInfo = headerInfo;
        int headerMode = headerInfo.getHeaderMode();
        int headerTitleResId = headerInfo.getHeaderTitleResId();
        //TODO hide event textview in profile screen
        if (headerMode == IHeaderInfo.HEADER_MODE_NONE) {
            mHeader.setVisibility(View.GONE);
        } else {
            mHeader.setVisibility(View.VISIBLE);
//            mHeader.setBackgroundColor(getResources().getColor(R.color.bg_white));
            if (headerMode == IHeaderInfo.HEADER_MODE_SEARCH || headerMode == IHeaderInfo.HEADER_MODE_SEARCH_INACTIVE) {
                //show search view
//                headerSearchView.setVisibility(View.VISIBLE);
                if (headerMode == IHeaderInfo.HEADER_MODE_SEARCH_INACTIVE) {
                    //prevent click search editext
//                    btnHeaderSearch.setVisibility(View.VISIBLE);
//                    edtHeaderSearchBox.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.search_small, 0, 0, 0);
                } else {
//                    btnHeaderSearch.setVisibility(View.GONE);
//                    edtHeaderSearchBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
//                tvHeaderTitle.setVisibility(View.GONE);
            headerSearchView.requestFocus();
                edtHeaderSearchBox.setText(headerInfo.getSearchKeyword());
//                edtHeaderSearchBox.setHint(headerInfo.getHeaderSearchHintResId());
                if (headerMode == IHeaderInfo.HEADER_MODE_SEARCH_INACTIVE) {
                    edtHeaderSearchBox.clearFocus();
                    btnHeaderDeleteText.setVisibility(View.INVISIBLE);
                } else {

                    if (edtHeaderSearchBox.getText().toString().length() > 0) {
                        btnHeaderDeleteText.setVisibility(View.VISIBLE);
                    } else {
                        btnHeaderDeleteText.setVisibility(View.INVISIBLE);
                    }
                }
                tvHeaderTitle.setTextColor(getResources().getColor(R.color.black));
                if (tvHeaderTitle != null) {
                    if (TextUtils.isEmpty(mCustomHeaderText)) {
                        tvHeaderTitle.setText(headerInfo.getHeaderTitleResId());
                    } else {
                        tvHeaderTitle.setText(mCustomHeaderText);
                    }
                }

            } else {
                headerSearchView.setVisibility(View.GONE);
                tvHeaderTitle.setVisibility(View.VISIBLE);
                tvHeaderTitle.setTextColor(getResources().getColor(R.color.black));
                if (tvHeaderTitle != null) {
                    if (TextUtils.isEmpty(mCustomHeaderText)) {
                        tvHeaderTitle.setText(headerInfo.getHeaderTitleResId());
                    } else {
                        tvHeaderTitle.setText(mCustomHeaderText);
                    }
                }
//                headerSearchView.requestFocus();
//                edtHeaderSearchBox.clearFocus();
            }
        }

//        if(tvHeaderTitle!=null) {
//            if(TextUtils.isEmpty(mCustomHeaderText)) {
//                tvHeaderTitle.setText(headerTitleResId);
//            }else{
//                tvHeaderTitle.setText(mCustomHeaderText);
//            }
//        }
        int rightButtonResId = headerInfo.getHeaderRightButtonImageResId();
        if (rightButtonResId != 0) {
            btnHeaderRight.setImageResource(rightButtonResId);
            btnHeaderRight.setVisibility(View.VISIBLE);
        } else {
            btnHeaderRight.setVisibility(View.INVISIBLE);
        }
        int leftButtonResId = headerInfo.getHeaderLeftButtonImageResId();
        if (leftButtonResId != 0) {
            btnHeaderLeft.setImageResource(leftButtonResId);
            btnHeaderLeft.setVisibility(View.VISIBLE);
        } else {
            btnHeaderLeft.setVisibility(View.INVISIBLE);
        }
        btnHeaderDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtHeaderSearchBox.setText(null);
                mCurrentHeaderInfo.SearchAction(null);
            }
        });
        checkHeaderState();
    }

    @OnClick(R.id.btn_header_left)
    void onClickButtonHeaderLeft(View view) {
        if (mCurrentHeaderInfo != null) {
            if (mCurrentHeaderInfo.getHeaderLeftButtonImageResId() == 0) {
                //backbutton
                String currentTabTag = mTabHost.getCurrentTabTag();
                TabFragment currentTabfragment = (TabFragment) getSupportFragmentManager().findFragmentByTag(currentTabTag);
                if (currentTabfragment != null) {
                    FragmentManager childFragmentManager = currentTabfragment.getChildFragmentManager();
                    if (childFragmentManager.getBackStackEntryCount() > 0) {
                        childFragmentManager.popBackStack();
                    }
                }
            }else if(mCurrentHeaderInfo.getHeaderLeftButtonImageResId() == R.mipmap.header_setting){
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
            mCurrentHeaderInfo.onClickHeaderLeftButton(view);

        }
    }

    @OnClick(R.id.btn_header_right)
    void onClickButtonHeaderRight(View view) {
        if (mCurrentHeaderInfo != null) {
            mCurrentHeaderInfo.onClickHeaderRightButton(view);
            if( mCurrentHeaderInfo.getHeaderMode() == IHeaderInfo.HEADER_MODE_SEARCH) {
                mCurrentHeaderInfo.saveSearchKeyword(edtHeaderSearchBox.getText().toString());
            }
        }
    }
    public void moveToTab(int index) {
        mTabHost.setCurrentTab(index);
    }




    private void addDrawerItems() {
        dataList = new ArrayList<DrawerItem>();
        for(String name : getResources().getStringArray(R.array.Setting_name)) {
            dataList.add(new DrawerItem(name, 0));
        }

        mAdapter = new DrawerAdapter(this, R.layout.row_drawer_item, dataList);

        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0 ) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });

        View head = (View) getLayoutInflater().inflate(R.layout. header_drawer_menu, null);
        mDrawerList.addHeaderView(head);

    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.nav_home, R.string.nav_movies) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle("test");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* get location*/
    // GPSTracker class
    GPSTracker gps;
    public void initLocation(){
        // Create class object
        gps = new GPSTracker(MainActivity.this);
        getLocation();
    }
    public LocationData getLocation(){
        // Check if GPS enabled
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            sharePreferenceData.setLocationUser(latitude, longitude);
            callApiEvent(latitude, longitude);
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            return new LocationData(latitude, longitude);
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
        return null;
    }

    protected void callApiEvent(final double lat, final double longi) {

        Observable<ObjectDto> objectDtoObservable = matchingApi.updateLocation(lat, longi);
        androidSubcribe(objectDtoObservable, new ApiSubscriber<ObjectDto>(this, true) {
            @Override
            protected void onDataError(ObjectDto events) {

            }

            @Override
            public void onDataSuccess(ObjectDto events) {

            }
        });
    }

}
