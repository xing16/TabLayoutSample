package com.xing.tablayoutsample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TabFragment extends Fragment {

    private int type;
    private View view;
    private Button button;


    public TabFragment() {
    }

    public static TabFragment newInstance(int type) {
        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab, container, false);
        initView();
        initData();
        return view;
    }

    private void initView() {
        button = (Button) view.findViewById(R.id.btn_hide);

    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int type = bundle.getInt("type");
            button.setText("type = " + type);
        }

    }

}
