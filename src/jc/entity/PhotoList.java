package jc.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ½ð³É on 2015/7/26.
 */
public class PhotoList {

    int code;
    List<Photo> photoList = new ArrayList<Photo>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}
