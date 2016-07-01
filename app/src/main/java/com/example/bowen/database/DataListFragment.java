package com.example.bowen.database;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DataListFragment extends ListFragment {

    private static final String TAG = "ASDF";

    private ArrayList<PatientData> mPatients;
    private String loginName;

    public static DataListFragment newInstance(String loginName) {
        // obtain the username variable from previous activity
        Bundle args = new Bundle();
        args.putSerializable(LoginActivity.EXTRA_LOGIN, loginName);
        DataListFragment fragment = new DataListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginName = getArguments().getString(LoginActivity.EXTRA_LOGIN);
        new FetchItemsTask().execute();

        // get patient lists
        mPatients = DataContext.get(getActivity()).getPatients();
        ArrayAdapter<PatientData> adapter =
                new ArrayAdapter<PatientData>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        mPatients);
        setListAdapter(adapter);
    }

    private class FetchItemsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {

            try {
                String link ="http://" + FileReader.readFromFile() + "/LeapMotion/AndroidGetList.php";
                URL url = new URL(link);
                String urlParams = "loginName=" +  URLEncoder.encode(loginName);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String next;
                while ((next = bufferedReader.readLine()) != null) {
                    JSONArray ja = new JSONArray(next);
                    // write data to the ArrayList
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        PatientData data = new PatientData();
                        data.setName(jo.getString("PatientName"));
                        data.setAge(jo.getString("age"));
                        data.setGender(jo.getString("gender"));
                        data.setScoreLeft(jo.getString("score1"));
                        data.setScoreRight(jo.getString("score2"));
                        data.setComment(jo.getString("comments"));
                        data.setTime(jo.get("testtime"));

                        data.setRawData(jo.getString("rawdata"));
                        data.setRawData2(jo.getString("rawdata2"));
                        data.setRawData3(jo.getString("rawdata3"));
                        data.setRawData4(jo.getString("rawdata4"));

                        mPatients.add(data);
                        Log.d(TAG, next);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        // Get the patient data from the adapter
        PatientData c =(PatientData)getListAdapter().getItem(position);
        // Start DataActivity
        Intent i = new Intent(getActivity(), DataActivity.class);
        i.putExtra(DataFragment.EXTRA_PATIENT_ID, c.getId());
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPatients.clear(); // clears ArrayList upon exit
    }
}
