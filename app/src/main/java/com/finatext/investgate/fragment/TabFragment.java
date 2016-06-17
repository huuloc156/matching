package com.finatext.investgate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finatext.investgate.R;
import com.finatext.investgate.fragment.events.PushNotificationEvent;
import com.squareup.otto.Subscribe;

import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class TabFragment extends BaseFragment {

    public static final String FIRST_FRAGMENT = "firstFragment";
    public boolean isChangeTab;
    public TabFragment() {
        // Required empty public constructor
    }

    public static TabFragment createInstance(Class fragment){
        TabFragment homeFragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(
                FIRST_FRAGMENT, fragment.getCanonicalName());
        homeFragment.setArguments(args);
        return homeFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Timber.tag("kitvs").i("====onCreateView tabFragment");
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState == null && getChildFragmentManager().getBackStackEntryCount() == 0){
            if(isFirstCreate) {
                addNewsEventsScreen();
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void addNewsEventsScreen() {
        if(getArguments()!=null &&  getArguments().containsKey(FIRST_FRAGMENT)) {
            Fragment fragment = Fragment.instantiate(getActivity(), getArguments().getString(FIRST_FRAGMENT));
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
            fragmentTransaction.replace(R.id.frame_tab_content2, fragment);
            fragmentTransaction.commit();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager childFragmentManager = getChildFragmentManager();
        if(childFragmentManager!=null) {
            List<Fragment> fragments = childFragmentManager.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if(fragment!= null && fragment.isVisible()) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        }
    }
    @Subscribe
    public void handleNotificationEvent(PushNotificationEvent event){
        //handle notification
        if(isVisible()){
            switch (event.type){
                //TODO handle notification
                case COMMENT:
                    break;
                case FOLLOWED:
                    break;
                default:
                    break;
            }
        }
    }

}
