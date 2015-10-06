package com.example.bowen.database;

import android.support.v4.app.Fragment;

import java.util.UUID;


public class DataActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        UUID pId = (UUID)getIntent()
                .getSerializableExtra(DataFragment.EXTRA_PATIENT_ID);
        return DataFragment.newInstance(pId);
    }


}
