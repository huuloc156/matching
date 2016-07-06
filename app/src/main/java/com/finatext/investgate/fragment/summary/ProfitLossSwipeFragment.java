package com.finatext.investgate.fragment.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.finatext.investgate.R;
import com.finatext.investgate.fragment.AbstractSwipeFragment;
import com.finatext.investgate.fragment.BaseFragment;

/**
 * Created by apple on 6/20/16.
 */
public class ProfitLossSwipeFragment extends AbstractSwipeFragment {
    @Override
    protected void initPagerFragment() {
        addFragment("実現損益", new ProfitLossTabCurrentFragment());
        addFragment("年間の累計損益", new ProfitLossTabYearFragment());
    }




}
