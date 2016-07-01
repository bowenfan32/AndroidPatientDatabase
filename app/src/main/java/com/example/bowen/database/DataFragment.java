package com.example.bowen.database;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;


public class DataFragment extends Fragment {
    private static final String TAG = "ASDF";

    public static final String EXTRA_PATIENT_ID =
            "patient_id";

    private TextView mPatientComments;
    private PatientData mData;
    private TextView mPatientName;
    private TextView mPatientAge;
    private TextView mPatientGender;
    private TextView mPatientScoreLeft;
    private TextView mPatientScoreRight;
    private TextView mPatientTestTime;

    private TextView t1, i1, m1, r1, p1, tt1, ii1, mm1, rr1, pp1; // row two textviews
    private TextView t2, i2, m2, r2, p2, tt2, ii2, mm2, rr2, pp2; // row three textviews
    private TextView t3, i3, m3, r3, p3, tt3, ii3, mm3, rr3, pp3; // row four textviews

    ArrayList<String> tableList = new ArrayList<String>();
    ArrayList<String> tableList2 = new ArrayList<String>();
    ArrayList<String> tableList3 = new ArrayList<String>();
    ArrayList<String> tableList4 = new ArrayList<String>();

    private PatientData mPatientData;

    public static DataFragment newInstance(UUID pId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PATIENT_ID, pId);
        DataFragment fragment = new DataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID patientId = (UUID) getArguments().getSerializable(EXTRA_PATIENT_ID);
        mPatientData = DataContext.get(getActivity()).getPatient(patientId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data, parent, false);

        mPatientName = (TextView) v.findViewById(R.id.patient_name);
        mPatientAge = (TextView) v.findViewById(R.id.patient_age);
        mPatientGender = (TextView) v.findViewById(R.id.patient_gender);
        mPatientScoreLeft = (TextView) v.findViewById(R.id.patient_score_left);
        mPatientScoreRight = (TextView) v.findViewById(R.id.patient_score_right);
        mPatientTestTime = (TextView) v.findViewById(R.id.patient_testtime);
        mPatientComments = (EditText) v.findViewById(R.id.commenBody);


        t1 = (TextView) v.findViewById(R.id.t1);
        t2 = (TextView) v.findViewById(R.id.t2);
        t3 = (TextView) v.findViewById(R.id.t3);
        i1 = (TextView) v.findViewById(R.id.i1);
        i2 = (TextView) v.findViewById(R.id.i2);
        i3 = (TextView) v.findViewById(R.id.i3);
        m1 = (TextView) v.findViewById(R.id.m1);
        m2 = (TextView) v.findViewById(R.id.m2);
        m3 = (TextView) v.findViewById(R.id.m3);
        r1 = (TextView) v.findViewById(R.id.r1);
        r2 = (TextView) v.findViewById(R.id.r2);
        r3 = (TextView) v.findViewById(R.id.r3);
        p1 = (TextView) v.findViewById(R.id.p1);
        p2 = (TextView) v.findViewById(R.id.p2);
        p3 = (TextView) v.findViewById(R.id.p3);

        tt1 = (TextView) v.findViewById(R.id.tt1);
        tt2 = (TextView) v.findViewById(R.id.tt2);
        tt3 = (TextView) v.findViewById(R.id.tt3);
        ii1 = (TextView) v.findViewById(R.id.ii1);
        ii2 = (TextView) v.findViewById(R.id.ii2);
        ii3 = (TextView) v.findViewById(R.id.ii3);
        mm1 = (TextView) v.findViewById(R.id.mm1);
        mm2 = (TextView) v.findViewById(R.id.mm2);
        mm3 = (TextView) v.findViewById(R.id.mm3);
        rr1 = (TextView) v.findViewById(R.id.rr1);
        rr2 = (TextView) v.findViewById(R.id.rr2);
        rr3 = (TextView) v.findViewById(R.id.rr3);
        pp1 = (TextView) v.findViewById(R.id.pp1);
        pp2 = (TextView) v.findViewById(R.id.pp2);
        pp3 = (TextView) v.findViewById(R.id.pp3);

        mPatientName.append(mPatientData.getName());
        mPatientAge.append("Age: " + mPatientData.getAge());
        mPatientGender.append("Gender: " + mPatientData.getGender());
        mPatientScoreLeft.append("Left Hand Score: " + mPatientData.getScoreLeft());
        mPatientScoreRight.append("Right Hand Score: " + mPatientData.getScoreRight());
        mPatientTestTime.append("Test Date: " + mPatientData.getTime().toString());

        mPatientComments.setText(mPatientData.getComments());
        mPatientComments.setFocusable(false);

//        mPatientComments.setOnEditorActionListener(new EditText.OnEditorActionListener(){
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//                        actionId == EditorInfo.IME_ACTION_DONE ||
//                        event.getAction() == KeyEvent.ACTION_DOWN &&
//                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    if (!event.isShiftPressed()) {
//                        // the user is done typing.
//
//                        return true; // consume.
//                    }
//                }
//                return false; // pass on to other listeners.
//            }
//        });

        showTable();

        return v;
    }

    public void showTable() {
        // the if statement checks if some rawData are missing, if so, do not calculate ROM
        if (mPatientData.getRawData().length() > 5 || mPatientData.getRawData3().length() > 5) {
            parseRawData(mPatientData.getRawData(), tableList);
            parseRawData(mPatientData.getRawData2(), tableList2);
            parseRawData(mPatientData.getRawData3(), tableList3);
            parseRawData(mPatientData.getRawData4(), tableList4);

            // calculates the ROM of each joint and put each of them into table cells
            t1.setText("" + (Double.parseDouble(tableList2.get(0)) - Double.parseDouble(tableList.get(0))));
            i1.setText("" + (Double.parseDouble(tableList2.get(1)) - Double.parseDouble(tableList.get(1))));
            m1.setText("" + (Double.parseDouble(tableList2.get(2)) - Double.parseDouble(tableList.get(2))));
            r1.setText("" + (Double.parseDouble(tableList2.get(3)) - Double.parseDouble(tableList.get(3))));
            p1.setText("" + (Double.parseDouble(tableList2.get(4)) - Double.parseDouble(tableList.get(4))));

            t2.setText("" + (Double.parseDouble(tableList2.get(5)) - Double.parseDouble(tableList.get(5))));
            i2.setText("" + (Double.parseDouble(tableList2.get(6)) - Double.parseDouble(tableList.get(6))));
            m2.setText("" + (Double.parseDouble(tableList2.get(7)) - Double.parseDouble(tableList.get(7))));
            r2.setText("" + (Double.parseDouble(tableList2.get(8)) - Double.parseDouble(tableList.get(8))));
            p2.setText("" + (Double.parseDouble(tableList2.get(9)) - Double.parseDouble(tableList.get(9))));

            t3.setText("N/A");
            i3.setText("" + (Double.parseDouble(tableList2.get(11)) - Double.parseDouble(tableList.get(11))));
            m3.setText("" + (Double.parseDouble(tableList2.get(12)) - Double.parseDouble(tableList.get(12))));
            r3.setText("" + (Double.parseDouble(tableList2.get(13)) - Double.parseDouble(tableList.get(13))));
            p3.setText("" + (Double.parseDouble(tableList2.get(14)) - Double.parseDouble(tableList.get(14))));


            tt1.setText("" + (Double.parseDouble(tableList4.get(0)) - Double.parseDouble(tableList3.get(0))));
            ii1.setText("" + (Double.parseDouble(tableList4.get(1)) - Double.parseDouble(tableList3.get(1))));
            mm1.setText("" + (Double.parseDouble(tableList4.get(2)) - Double.parseDouble(tableList3.get(2))));
            rr1.setText("" + (Double.parseDouble(tableList4.get(3)) - Double.parseDouble(tableList3.get(3))));
            pp1.setText("" + (Double.parseDouble(tableList4.get(4)) - Double.parseDouble(tableList3.get(4))));

            tt2.setText("" + (Double.parseDouble(tableList4.get(5)) - Double.parseDouble(tableList3.get(5))));
            ii2.setText("" + (Double.parseDouble(tableList4.get(6)) - Double.parseDouble(tableList3.get(6))));
            mm2.setText("" + (Double.parseDouble(tableList4.get(7)) - Double.parseDouble(tableList3.get(7))));
            rr2.setText("" + (Double.parseDouble(tableList4.get(8)) - Double.parseDouble(tableList3.get(8))));
            pp2.setText("" + (Double.parseDouble(tableList4.get(9)) - Double.parseDouble(tableList3.get(9))));

            tt3.setText("N/A");
            ii3.setText("" + (Double.parseDouble(tableList4.get(11)) - Double.parseDouble(tableList3.get(11))));
            mm3.setText("" + (Double.parseDouble(tableList4.get(12)) - Double.parseDouble(tableList3.get(12))));
            rr3.setText("" + (Double.parseDouble(tableList4.get(13)) - Double.parseDouble(tableList3.get(13))));
            pp3.setText("" + (Double.parseDouble(tableList4.get(14)) - Double.parseDouble(tableList3.get(14))));

        }
    }


    public void parseRawData(String data, ArrayList table) {
        try {
            JSONArray ja = new JSONArray(data);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                table.add(jo.getString("Thumb").substring(0, jo.getString("Thumb").length() - 1));
                table.add(jo.getString("Index").substring(0, jo.getString("Index").length() - 1));
                table.add(jo.getString("Middle").substring(0, jo.getString("Middle").length() - 1));
                table.add(jo.getString("Ring").substring(0, jo.getString("Ring").length() - 1));
                table.add(jo.getString("Pinky").substring(0, jo.getString("Pinky").length() - 1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit_comment:
                mPatientComments.setClickable(true);
                mPatientComments.setCursorVisible(true);
                mPatientComments.setFocusableInTouchMode(true);
                mPatientComments.setInputType(InputType.TYPE_CLASS_TEXT);
                mPatientComments.requestFocus();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // send comments to database
    private class PostItemsTask extends AsyncTask<String, Void, String> {
        String comments = mPatientComments.getText().toString();

        @Override
        protected String doInBackground(String... arg0) {

            try {
                String link = "http://" + FileReader.readFromFile() + "/LeapMotion/AndroidPostComments.php";
                URL url = new URL(link);
                String urlParams = "comments=" + URLEncoder.encode(comments) + "&patientName=" + URLEncoder.encode(mPatientData.getName());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.d(TAG, "connect");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", "" + Integer.toString(urlParams.getBytes().length));
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(urlParams);
                writer.flush();
                writer.close();
                conn.connect();

                InputStream in = conn.getInputStream();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        new PostItemsTask().execute();
        String comments = mPatientComments.getText().toString();
        mPatientComments.setText(comments);
    }
}
