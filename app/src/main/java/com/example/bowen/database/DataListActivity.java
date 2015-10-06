package com.example.bowen.database;

import android.support.v4.app.Fragment;


public class DataListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        String loginName = getIntent().getStringExtra(LoginActivity.EXTRA_LOGIN);
        getActionBar();
        return DataListFragment.newInstance(loginName);

    }


}
