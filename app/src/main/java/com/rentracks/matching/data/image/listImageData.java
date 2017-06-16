package com.rentracks.matching.data.image;

import java.util.ArrayList;

/**
 * Created by HuuLoc on 6/14/17.
 */

public class listImageData {
    private static listImageData mInstance;
    ArrayList<dataDrawable> listImg = new ArrayList<>();

    public static synchronized listImageData getInstance(){
        if(mInstance == null){
            mInstance = new listImageData();
        }
        return mInstance;
    }
    public listImageData(){

    }
    public void add(dataDrawable d){
        if(checkExist(d) == false) {
            listImg.add(d);
        }
    }
    public dataDrawable getDataDrawable(int position){
        return listImg.get(position);
    }
    public dataDrawable getWithId(int id){
        for(int i =0 ; i<listImg.size(); i++){
            if(listImg.get(i).getId() == id){
                return listImg.get(i);
            }
        }
        return null;
    }
    public boolean checkExist(dataDrawable d){
        for(int i =0 ; i<listImg.size(); i++){
            if(listImg.get(i).getId() == d.getId()){
                return true;
            }
        }
        return false;
    }
}
