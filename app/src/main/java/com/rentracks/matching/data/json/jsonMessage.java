package com.rentracks.matching.data.json;

import com.rentracks.matching.data.api.dto.chat.ChatItem;
import com.rentracks.matching.data.api.dto.chat.GroupItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HuuLoc on 6/14/17.
 */

public class jsonMessage {
    ChatItem chatItem;
    GroupItem groupItem;
    private  static JSONObject mJson;
    private static jsonMessage mInstance;

    public static synchronized jsonMessage getInstance(String jsonString) throws JSONException {
        if(mInstance == null){
            mInstance = new jsonMessage(jsonString);
        }else{
            mInstance.setJson(jsonString);
        }
        return mInstance;
    }

    public static synchronized jsonMessage getInstance(JSONObject jsonObject ){
        if(mInstance == null){
            mInstance = new jsonMessage(jsonObject);
        }else{
            mInstance.setJson(jsonObject);
        }
        return mInstance;
    }

    public jsonMessage(String jsonString) throws JSONException {
        mJson = new JSONObject(jsonString);
    }

    public void setJson(String jsonString) throws JSONException {
        mJson = new JSONObject(jsonString);
    }

    public void setJson(JSONObject jsonObject){
        mJson = jsonObject;
    }
    public jsonMessage(JSONObject jsonObject ){
        mJson = jsonObject;
    }

    public ChatItem getChatItem() throws JSONException {

        JSONObject jMessData = mJson.getJSONObject("data");
        chatItem = new ChatItem();
        chatItem.created_at = jMessData.getString("created_at");
        chatItem.message = jMessData.getString("message");
        chatItem.uid = jMessData.getInt("uid");
        if(jMessData.has("group_id")) {
            chatItem.group_id = jMessData.getInt("group_id");
        }else{
            chatItem.group_id = -1;
        }
        chatItem.status = jMessData.getInt("status");
        return chatItem;
    }
    public GroupItem getGroupItem() throws JSONException {
        JSONObject jMessData = mJson.getJSONObject("data");
        groupItem = new GroupItem();
        groupItem.last_name = jMessData.getString("name");
        groupItem.last_mess = jMessData.getString("message");
        if(jMessData.has("group_id")) {
            groupItem.group_id = jMessData.getInt("group_id");
        }else{
            groupItem.group_id = -1;
        }
        return  groupItem;
    }
}
