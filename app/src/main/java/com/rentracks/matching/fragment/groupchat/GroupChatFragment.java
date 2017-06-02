package com.rentracks.matching.fragment.groupchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentracks.matching.R;
import com.rentracks.matching.fragment.BaseFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;

public class GroupChatFragment extends BaseFragment {

    public static GroupChatFragment getInstance() {
        return new GroupChatFragment();
    }

    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_NORMAL;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
