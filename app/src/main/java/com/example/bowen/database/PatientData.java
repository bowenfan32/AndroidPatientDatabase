package com.example.bowen.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;


public class PatientData {


    private UUID mId;
    private String mName;
    private String mAge;
    private String mGender;
    private String mScoreLeft;
    private String mScoreRight;

    private String mData;
    private String mData2;
    private String mData3;
    private String mData4;


    public PatientData() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;

    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setAge(String age) {
        mAge = age;
    }

    public String getAge() {
        return mAge;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getGender() {
        return mGender;
    }

    public void setScoreLeft(String score) {
        mScoreLeft = score;
    }

    public String getScoreLeft() {
        return mScoreLeft;
    }

    public void setScoreRight(String score) {
        mScoreRight = score;
    }

    public String getScoreRight() {
        return mScoreRight;
    }

    public void setRawData(String data) {
        mData = data;
    }

    public String getRawData() {
        return mData;
    }

    public void setRawData2(String data) {
        mData2 = data;
    }

    public String getRawData2() {
        return mData2;
    }

    public void setRawData3(String data) {
        mData3 = data;
    }

    public String getRawData3() {
        return mData3;
    }

    public void setRawData4(String data) {
        mData4 = data;
    }

    public String getRawData4() {
        return mData4;
    }

    @Override
    public String toString() {
        return mName;
    }


}


class DataContext {

    private ArrayList<PatientData> mPatients;

    private static DataContext sDataContext;
    private Context mAppContext;

    private DataContext(Context appContext) {
        mAppContext = appContext;
        mPatients = new ArrayList<PatientData>();
    }

    public static DataContext get(Context c) {
        if (sDataContext == null) {
            sDataContext = new DataContext(c.getApplicationContext());
        }
        return sDataContext;
    }

    public ArrayList<PatientData> getPatients() {
        return mPatients;
    }

    public PatientData getPatient(UUID id) {
        for (PatientData c : mPatients) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }


}
