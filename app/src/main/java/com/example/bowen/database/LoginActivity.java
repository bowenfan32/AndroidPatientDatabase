package com.example.bowen.database;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

/**
 * Created by bowen on 11/09/2015.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "ASDF";

    private EditText userName, userPwd;
    private ImageView buttonPwd; // to switch password visibility
    private boolean isPwdShow = false; // default as non-visible

    public static final String EXTRA_LOGIN =
            "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    public void init() {
        userName = (EditText) findViewById(R.id.username);
        userPwd = (EditText) findViewById(R.id.password);
        buttonPwd = (ImageView) findViewById(R.id.button_pwd);
        buttonPwd.setOnClickListener(this);
        findViewById(R.id.button_ok_user).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_pwd:// to switch password visibility
                if (isPwdShow) {// if visible, switch to non visible
                    buttonPwd.setImageResource(R.drawable.login_pwd_hide);
                    userPwd.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {// if non visible, switch to visible
                    buttonPwd.setImageResource(R.drawable.login_pwd_show);
                    userPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                isPwdShow = !isPwdShow;
                break;

            case R.id.button_ok_user: // user login
                new LoginTask().execute();
                break;
            default:
                break;
        }

    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        String userLoginName = userName.getText().toString();
        String userPassword = userPwd.getText().toString();
        String result = "";

        @Override
        protected String doInBackground(String... arg0) {

            try {
                // connect to php script in the server
//                String link = "http://14.201.9.164:8080/AndroidGetPassword.php";
//                String link = "http://10.130.42.83:8080/AndroidGetPassword.php";
                String link ="http://" + FileReader.readFromFile() + "/AndroidGetPassword.php";
                URL url = new URL(link);
                String urlParams = "loginName=" +  URLEncoder.encode(userLoginName);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", "" + Integer.toString(urlParams.getBytes().length));
                conn.setDoInput(true);
                conn.setDoOutput(true);


                // post data to php script
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(urlParams);
                writer.flush();
                writer.close();
                conn.connect();

                // read data from the server
                InputStream in = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    JSONArray ja = new JSONArray(inputLine);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        result = jo.getString("password");
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String a){
            // check whether the password matches from the database
            if (result.equals(userPassword)) { // if match start the next activity
                Intent i = new Intent(LoginActivity.this, DataListActivity.class);
                i.putExtra(LoginActivity.EXTRA_LOGIN, userLoginName); // pass the variable to next activity
                startActivity(i);
                LoginActivity.this.finish(); // will no longer come back to this activity when back button pressed
            } else {
                Toast.makeText(LoginActivity.this, "Invalid username or password",Toast.LENGTH_SHORT ).show();
            }
        }
    }





}
