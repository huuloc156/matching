package com.rentracks.matching.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.fragment.TabFragment;
import com.rentracks.matching.listener.OnBackPressListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by apple on 6/17/16.
 */
public abstract class BaseTabHostActivity extends BaseActivity {

    protected static final int TAB_MAIN = 0;

    @BindView(R.id.main_tab_host)
    protected FragmentTabHost mTabHost;
    List<String> mTabTag = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getTabLayoutResId());
        mTabHost.setup(this, getSupportFragmentManager(), R.id.frame_tab_content);
        mTabTag.clear();
        initTabs();
        mTabHost.setCurrentTab(0);
        TabWidget tw = mTabHost.getTabWidget();
        for (int i = 0; i < tw.getTabCount(); ++i) {
            // HACK: Hook click event on tab
            tw.getChildTabViewAt(i).setOnClickListener(new TabClickListener(i));
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            tw.setDividerDrawable(R.drawable.tab_divider);
        }
        tw.setDividerDrawable(null);

        TabChangedListener tabListener = new TabChangedListener();
        mTabHost.setOnTabChangedListener(tabListener);
    }

    abstract protected int getTabLayoutResId();

    /**
     * init eachTabIcon
     */
    abstract protected void initTabs();

    protected void setupTab(String tag, String title, int drawable, Class fragment) {
        mTabTag.add(tag);
        final View tab = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tab_item, null);
        ((ImageView) tab.findViewById(R.id.tab_icon)).setImageResource(drawable);
        ((TextView) tab.findViewById(R.id.tab_text)).setText(title);
        TabHost.TabSpec spec = mTabHost.newTabSpec(tag).setIndicator(tab);
        Bundle b = new Bundle();
        b.putString("tag", tag);
        b.putString(TabFragment.FIRST_FRAGMENT, fragment.getCanonicalName());
        mTabHost.addTab(spec, TabFragment.class, b);
    }

    private void onClickCurrentTab() {
        if(isPause){
            //prevent IllegalStateException of replace fragment
            Timber.w("onClickCurrentTab prevent IllegalStateException of replace fragment");
            return;
        }
        TabFragment currentTabfragment = (TabFragment) getSupportFragmentManager().findFragmentByTag(mTabHost.getCurrentTabTag());
        if(currentTabfragment!=null) {
            currentTabfragment.backToRoot();
        }

    }
    @Override
    public void onBackPressed() {
        if(isPause){
            //prevent IllegalStateException of replace fragment
            Timber.w("onBackPressed prevent IllegalStateException of replace fragment");
            return;
        }
        // if there is a fragment and the back stack of this fragment is not empty,
        // then emulate 'onBackPressed' behaviour, because in default, it is not working
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag!=null &&frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                for (Fragment fragChild : childFm.getFragments()){
                    if (fragChild!=null &&fragChild.isVisible()){
                        if(fragChild instanceof OnBackPressListener){
                            if(!((OnBackPressListener) fragChild).onBackPress()){
                                if (childFm.getBackStackEntryCount() > 0) {
                                    childFm.popBackStack();
                                    return;
                                }
                            }else{
                                return;
                            }
                        }
                    }
                }
                break;
            }
        }
        if(mTabHost.getCurrentTab()!= TAB_MAIN){
            mTabHost.setCurrentTab(TAB_MAIN);
        }else {
            super.onBackPressed();
        }
    }

    // HACK: Class copied from TabWidget:531
    // registered with each tab indicator so we can notify tab host
    private class TabClickListener implements View.OnClickListener {

        private final int mTabIndex;

        private TabClickListener(int tabIndex) {
            mTabIndex = tabIndex;
        }

        public void onClick(View v) {

            if(isPause){
                Timber.w(" prevent IllegalStateException of replace fragment");
                //prevent click if is pause
                return;
            }
            if (mTabHost.getCurrentTab() == mTabIndex) {
                onClickCurrentTab();
            } else {
                //TODO change tab event
                TabFragment currentTabfragment = (TabFragment) getSupportFragmentManager().findFragmentByTag(mTabTag.get(mTabIndex));
                if(currentTabfragment!=null) {
                    currentTabfragment.isChangeTab = true;
                }
            }
            try {
                mTabHost.setCurrentTab(mTabIndex);
            }catch (IllegalStateException ex){
                //ignore this exception #52
                Timber.w("tab host change tab IllegalStateException");
            }
        }
    }
    private class TabChangedListener implements TabHost.OnTabChangeListener {
        @Override
        public void onTabChanged(String tabId) {

        }
    }
}