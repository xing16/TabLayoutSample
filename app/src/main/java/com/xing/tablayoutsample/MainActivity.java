package com.xing.tablayoutsample;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final int TYPE_CHAT_MSG = 1;

    public static final int TYPE_BUSINESS_MSG = 2;

    public static final int TYPE_SYSTEM_MSG = 3;

    public final String[] titles = {"聊天消息", "业务消息", "系统消息"};

    private NoScrollViewPager viewPager;

    private TabLayout tabLayout;

    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        viewPager = (NoScrollViewPager) findViewById(R.id.view_pager);
    }


    private void initData() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(TabFragment.newInstance(TYPE_CHAT_MSG));
        fragmentList.add(TabFragment.newInstance(TYPE_BUSINESS_MSG));
        fragmentList.add(TabFragment.newInstance(TYPE_SYSTEM_MSG));
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.setPagers(fragmentList, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        // 需要在 setupWithViewPager 之后调用
        setIndicator(this, tabLayout, 25, 25);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabCustomView(titles, i));
        }

    }

    private View getTabCustomView(String[] titles, int position) {
        View view = View.inflate(this, R.layout.layout_tab_dot, null);
        ImageView dotImgView = (ImageView) view.findViewById(R.id.iv_indicator_dot);
        if (position == 1) {
            dotImgView.setVisibility(View.GONE);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_indicator_text);
        textView.setText(titles[position]);
//        textView.measure(0, 0);
//        int measuredWidth = textView.getMeasuredWidth();
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.leftMargin = measuredWidth - dp2Px(5);
//        dotImgView.setLayoutParams(lp);
        return view;
    }


    /*
     * 利用反射修改 indicator 长度
     */
    private void setIndicator(Context context, TabLayout tabLayout, int leftMargin, int rightMargin) {
        Class<?> clazz = tabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = clazz.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tabStrip.setAccessible(true);
        LinearLayout linearLayout = null;
        try {
            linearLayout = (LinearLayout) tabStrip.get(tabLayout);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        int left = (int) (context.getResources().getDisplayMetrics().density * leftMargin);
        int right = (int) (context.getResources().getDisplayMetrics().density * rightMargin);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childView = linearLayout.getChildAt(i);
            childView.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1);
            lp.leftMargin = left;
            lp.rightMargin = right;
            childView.setLayoutParams(lp);
            childView.invalidate();

        }

    }


    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.d(TAG, "onTabSelected: tab = " + tab.getPosition());
            View view = tab.getCustomView();
            TextView textView = (TextView) view.findViewById(R.id.tv_indicator_text);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private int dp2Px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        private String[] titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setPagers(List<Fragment> fragments, String[] titles) {
            this.fragmentList = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList == null ? null : fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
