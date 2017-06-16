package com.rentracks.matching.fragment.timeline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.rentracks.matching.R;
import com.rentracks.matching.fragment.BaseFragment;
import com.rentracks.matching.fragment.header.IHeaderInfo;
import com.rentracks.matching.listener.ListenerClose;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class FilterSearchFragment extends BaseFragment {

    @BindView(R.id.ln_filter_search)
    LinearLayout main_ll;
    String tabId = "user";
    ListenerClose mListener;

    @Override
    public int getHeaderMode() {
        return IHeaderInfo.HEADER_MODE_NORMAL;
    }

//    @Override
//    public String getSearchKeyword() {
//        return "test";
//    }

    public static Fragment getInstance(){
        return new FilterSearchFragment();
    }

    public void setListenerClose(ListenerClose l){
        mListener = l;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        tabId = args.getString("tab_filter");

        return inflater.inflate(R.layout.fragment_filter_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addRow();
    }

    public void addRow() {
        main_ll.removeAllViews();

        if(tabId.equals("user")) {
            String filter = preferenceData.getUserFilter("20#Both#18#30");
            List<String> element = Arrays.asList(filter.split("#"));
            if(element.size() < 4){
                element = Arrays.asList("20#Female#18#30".split("#"));
            }

            View distance = createView("Distance", R.array.distance, element.get(0));
            main_ll.addView(distance);

            View gender = createView("Gender", R.array.gender_full, element.get(1));
            main_ll.addView(gender);

            View age = createView_2_spinner("Age", R.array.age, R.array.age, element.get(2),element.get(3));
            main_ll.addView(age);


        }else{
            String filter = preferenceData.getEventFilter("10#");
            List<String> element = Arrays.asList(filter.split("#"));

            View distance = createView("Distance", R.array.distance, element.get(0));
            main_ll.addView(distance);
        }
    }



    public View createView(String name, int idArr, String compareValue){

        LinearLayout gender = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.row_spinner, null);
        TextView name_gender = (TextView) gender.findViewById(R.id.txt_row_spinner);
        Spinner spin_gender = (Spinner) gender.findViewById(R.id.spin_row_spinner);

        name_gender.setText(name);

        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(getContext(), idArr, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_gender.setAdapter(adapter_gender);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter_gender.getPosition(compareValue);
            spin_gender.setSelection(spinnerPosition);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        gender.setLayoutParams(layoutParams);


        return gender;
    }
    public View createView_2_spinner(String name, int idArr, int intArr2, String compareValue1, String comparevalue2){

        LinearLayout age = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.row_two_spinner, null);
        Spinner age_from = (Spinner) age.findViewById(R.id.spin_row_spinner_1);
        Spinner age_to = (Spinner) age.findViewById(R.id.spin_row_spinner_2);
        TextView name_gender = (TextView) age.findViewById(R.id.txt_row_spinner_2);


        name_gender.setText(name);

        ArrayAdapter<CharSequence> adapter_gender_from = ArrayAdapter.createFromResource(getContext(), idArr, android.R.layout.simple_spinner_item);
        adapter_gender_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age_from.setAdapter(adapter_gender_from);
        if (!compareValue1.equals(null)) {
            int spinnerPosition = adapter_gender_from.getPosition(compareValue1);
            age_from.setSelection(spinnerPosition);
        }

        ArrayAdapter<CharSequence> adapter_gender_to = ArrayAdapter.createFromResource(getContext(), intArr2, android.R.layout.simple_spinner_item);
        adapter_gender_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age_to.setAdapter(adapter_gender_to);
        if (!compareValue1.equals(null)) {
            int spinnerPosition = adapter_gender_to.getPosition(comparevalue2);
            age_to.setSelection(spinnerPosition);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        age.setLayoutParams(layoutParams);

        return age;
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        String filter = "";
        if(main_ll.getChildCount() > 0){
            for(int j = 0; j < main_ll.getChildCount(); j++) {
                View v_ll = main_ll.getChildAt(j);
                if (v_ll instanceof LinearLayout) {
                    for (int i = 0; i < ((LinearLayout) v_ll).getChildCount(); i++) {
                        View v = ((LinearLayout) v_ll).getChildAt(i);
                        if (v instanceof Spinner) {
                            Spinner p = (Spinner) v;
                            filter += p.getSelectedItem().toString() + "#";
                        }
                    }
                }
            }
        }
        boolean isChange = false;
        if(!filter.equals("")){
            if(tabId.equals("user")) {
                String filter_pre = preferenceData.getUserFilter("");
                if(filter_pre.equals(filter) == false){
                    isChange = true;
                }
                preferenceData.setUserFilter(filter);
            }else{
                String filter_pre = preferenceData.getEventFilter("");
                if(filter_pre.equals(filter) == false){
                    isChange = true;
                }
                preferenceData.setEventFilter(filter);
            }
        }
        mListener.close(isChange);
    }
}
