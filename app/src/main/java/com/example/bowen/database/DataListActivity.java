package com.example.bowen.database;

import android.support.v4.app.Fragment;
import android.widget.Toast;


public class DataListActivity extends SingleFragmentActivity {
    private long exitTime = 0;

    @Override
    protected Fragment createFragment() {
        String loginName = getIntent().getStringExtra(LoginActivity.EXTRA_LOGIN);
        getActionBar();
        return DataListFragment.newInstance(loginName);
    }

    // double click back button to exit
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(DataListActivity.this, "Press again to exit", Toast.LENGTH_SHORT)
                    .show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}
