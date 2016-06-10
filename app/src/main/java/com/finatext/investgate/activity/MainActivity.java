package com.finatext.investgate.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.finatext.investgate.R;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_main)
    TextView tvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMain.setText(sharePreferenceData.getTest());
    }
}
