package com.rentracks.matching.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by lenam on 6/10/16.
 */

public class SharePreferenceData {
    ////////////////////////////////////////////////////////////////////////////
    // Constant
    ////////////////////////////////////////////////////////////////////////////
    /** Default name */
    private static final String DEFAULT_NAME = "androidInvestgate";
    ////////////////////////////////////////////////////////////////////////////
    // public Constant
    ////////////////////////////////////////////////////////////////////////////
    public final String KEY_IDENTIFY_USER                      = "KEY_IDENTIFY_USER";
    public final String KEY_USER_TOKEN                      = "KEY_USER_TOKEN";
    public final String USER_FILTER                      = "USER_FILTER";
    public final String EVENT_FILTER                      = "EVENT_FILTER";
    public final String LOCATION_USER                      = "LOCATION_USER";
    public final String USER_NAME                      = "USER_NAME";
    public final String USER_NAME_SOCIAL                      = "USER_NAME_SOCIAL";
    public final String DEVICE_TOKEN                      = "DEVICE_TOKEN";
    public final String IS_SEND_DEVICE_TOKEN                      = "IS_SEND_DEVICE_TOKEN";
    public final String IS_LOAD_GROUP_CHAT                      = "IS_LOAD_GROUP_CHAT";
//    public final String USER_GENDER                      = "USER_GENDER";

    private SharedPreferences preferences;

    private static SharePreferenceData mInstance;

    public SharePreferenceData(Context context){
        //TODO
        preferences = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharePreferenceData getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharePreferenceData(context);
        }
        return mInstance;
    }

    public int getUserId(){
        return getInt(KEY_IDENTIFY_USER);
    }

    public void setUserId(int id){
       setInt(KEY_IDENTIFY_USER,id);
    }


    public String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    public void setUserToken(String userToken) {
        setString(KEY_USER_TOKEN,userToken);
    }


    public void setUserFilter(String filter){
        setString(USER_FILTER, filter);
    }
    public String getUserFilter(String defaul){
        return getString(USER_FILTER, defaul);
    }

    public void setEventFilter(String filter){
        setString(EVENT_FILTER, filter);
    }
    public String getEventFilter(String defaul){
        return getString(EVENT_FILTER, defaul);
    }

    public void setLocationUser(Double lat, Double lng){
        setString(LOCATION_USER, String.valueOf(lat) +","+ String.valueOf(lng));
    }
    public String getLocationUser(){
        return getString(LOCATION_USER);
    }

    public void setUserName(String name){
        setString(USER_NAME, name);
    }
    public String getUserName(){
        return getString(USER_NAME);
    }

    public void setUserNameSocila(String name){
        setString(USER_NAME_SOCIAL, name);
    }
    public String getUserNameSocial(){
        return getString(USER_NAME_SOCIAL);
    }


    public void saveDeviceToken(String token){
        setString(DEVICE_TOKEN, token);
    }
    public String getDeviceToken(){
        return getString(DEVICE_TOKEN);
    }

    public void setSendDeviceToken(boolean is){
        setBoolean(IS_SEND_DEVICE_TOKEN, is);
    }
    public boolean getIsSendDeviceToken(){
        return getBoolean(IS_SEND_DEVICE_TOKEN);
    }

    public void setLoadGroupChat(boolean is){
        setBoolean(IS_LOAD_GROUP_CHAT, is);
    }
    public boolean getLoadGroupChat(){
        return getBoolean(IS_LOAD_GROUP_CHAT);
    }
    ////////////
    /**
     * set string value to preference
     *
     * @param key
     * @param value
     */
    private void setString(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        
        SharedPreferences.Editor edit = preferences.edit();
        if (TextUtils.isEmpty(value)) {
            edit.remove(key);
        } else {
            edit.putString(key, value);
        }
        edit.apply();
    }
    public boolean checkKey (String key){
        return preferences.contains(key);
    }
    /**
     * get string value
     *

     * @param key
     * @return 該当なし:null
     */
    private String getString(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return preferences.getString(key, "");
    }


    /**
     * get string value
     *

     * @param key
     * @param defaultValue
     * @return
     */
    private String getString(String key, String defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return preferences.getString(key, defaultValue);
    }

    /**
     * set boolean value to preference
     *

     * @param key
     * @param bool
     */
    private void setBoolean(String key, boolean bool) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, bool);
        edit.apply();
    }

    /**
     * get boolean value
     *

     * @param key
     * @return
     */
    private boolean getBoolean(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return preferences.getBoolean(key, false);
    }
    /**
     * get boolean value
     *

     * @param key
     * @param defaultValue
     * @return
     */
    private boolean getBoolean(String key, boolean defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return preferences.getBoolean(key, defaultValue);
    }
    /**
     * save long value to preference
     *

     * @param key
     * @param l
     */
    private void setLong(String key, long l) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong(key, l);
        edit.apply();
    }

    /**
     * get long value
     *

     * @param key
     * @return
     */
    private long getLong(String key) {
        if (TextUtils.isEmpty(key)) {
            return 0L;
        }
        return preferences.getLong(key, 0L);
    }

    /**
     * save int value to preference
     *

     * @param key
     * @param integer
     */
    private void setInt(String key, int integer) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(key, integer);
        edit.apply();
    }

    /**
     * get int value
     *

     * @param key
     * @return
     */
    private int getInt(String key) {
        if (TextUtils.isEmpty(key)) {
            return 0;
        }

        return preferences.getInt(key, 0);
    }

    /**
     * set gson object

     * @param key
     * @param object
     */
    private void setGsonObject(String key, Object object){
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString(key, json);
        prefsEditor.apply();
    }

    private <T> T getGsonObject(String key, Class<T> clazz){
        
        Gson gson = new Gson();
        String json = preferences.getString(key, "");
        return gson.fromJson(json, clazz);
    }

    /**
     * remove key SharedPreferences
     * @param key
     */
    private boolean removeKey(String key){
        SharedPreferences.Editor edit = preferences.edit();
        boolean result = edit.remove(key).commit();
        edit.apply();
        return result;
    }


}
