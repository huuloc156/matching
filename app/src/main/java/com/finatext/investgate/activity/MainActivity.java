package com.finatext.investgate.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.finatext.investgate.R;
import com.finatext.investgate.fragment.TabFragment;
import com.finatext.investgate.fragment.analysis.AssetAnalysisHomeFragment;
import com.finatext.investgate.fragment.events.OnSearchEvent;
import com.finatext.investgate.fragment.header.IHeaderInfo;
import com.finatext.investgate.fragment.header.IHeaderStateChange;
import com.finatext.investgate.fragment.mypage.MyPageHomeFragment;
import com.finatext.investgate.fragment.news.NewsHomeFragment;
import com.finatext.investgate.fragment.position.ListPositionHomeFragment;
import com.finatext.investgate.fragment.summary.SummaryHomeFragment;

import butterknife.BindView;
import butterknife.OnClick;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupHeader();
    }

    protected int getTabLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTabs() {
        //TODO setup your tab
        setupTab("Tab1", "手数料分析", R.mipmap.ic_launcher, SummaryHomeFragment.class);
        setupTab("Tab2", "資産分析", R.mipmap.ic_launcher, AssetAnalysisHomeFragment.class);
        setupTab("Tab3", "ポジション一覧", R.mipmap.ic_launcher, ListPositionHomeFragment.class);
        setupTab("Tab4", "情報", R.mipmap.ic_launcher, NewsHomeFragment.class);
        setupTab("Tab5", "マイページ", R.mipmap.ic_launcher, MyPageHomeFragment.class);
    }

    //////////////////////////
    ///Header/////////////////
    //////////////////////////
    private void setupHeader() {
        edtHeaderSearchBox.clearFocus();
        edtHeaderSearchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Timber.d("search box focus " + hasFocus);
                if (!hasFocus) {
                    hideKeyBoard(edtHeaderSearchBox);
                } else {

                }
                mBus.post(new OnSearchEvent(hasFocus));
            }
        });
        btnHeaderSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentHeaderInfo != null && mCurrentHeaderInfo.getHeaderMode() == IHeaderInfo.HEADER_MODE_SEARCH_INACTIVE) {
                    mBus.post(new OnSearchEvent(true));
                    edtHeaderSearchBox.requestFocus();
                }
            }
        });
        edtHeaderSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
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
                btnHeaderLeft.setImageResource(R.mipmap.header_back_b);
            } else {
                //TODO need flexible set header state
                if (mCurrentHeaderInfo != null && mCurrentHeaderInfo.getHeaderLeftButtonImageResId() != 0) {
                    btnHeaderLeft.setVisibility(View.VISIBLE);
                } else {
                    btnHeaderLeft.setVisibility(View.INVISIBLE);
                }
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
            mHeader.setBackgroundColor(getResources().getColor(R.color.bg_white));
            if (headerMode == IHeaderInfo.HEADER_MODE_SEARCH || headerMode == IHeaderInfo.HEADER_MODE_SEARCH_INACTIVE) {
                //show search view
                headerSearchView.setVisibility(View.VISIBLE);
                if (headerMode == IHeaderInfo.HEADER_MODE_SEARCH_INACTIVE) {
                    //prevent click search editext
//                    btnHeaderSearch.setVisibility(View.VISIBLE);
                    edtHeaderSearchBox.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.search_small, 0, 0, 0);
                } else {
//                    btnHeaderSearch.setVisibility(View.GONE);
                    edtHeaderSearchBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                tvHeaderTitle.setVisibility(View.GONE);
//                headerSearchView.requestFocus();
                edtHeaderSearchBox.setText(headerInfo.getSearchKeyword());
                edtHeaderSearchBox.setHint(headerInfo.getHeaderSearchHintResId());
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
                headerSearchView.requestFocus();
                edtHeaderSearchBox.clearFocus();
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
            }
            mCurrentHeaderInfo.onClickHeaderLeftButton(view);
        }
    }

    @OnClick(R.id.btn_header_right)
    void onClickButtonHeaderRight(View view) {
        if (mCurrentHeaderInfo != null) {
            mCurrentHeaderInfo.onClickHeaderRightButton(view);
        }
    }
}
