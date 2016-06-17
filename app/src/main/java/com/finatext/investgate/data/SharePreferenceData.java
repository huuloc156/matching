package com.finatext.investgate.data;

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

    SharedPreferences preferences;
    public SharePreferenceData(Context context){
        //TODO
        preferences = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
    }

    public String getUserId(){
        return getString(KEY_IDENTIFY_USER);
    }

    public void setUserId(String id){
       setString(KEY_IDENTIFY_USER,id);
    }


    public String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    public void setUserToken(String userToken) {
        setString(KEY_IDENTIFY_USER,userToken);
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
        return preferences.getString(key, null);
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
