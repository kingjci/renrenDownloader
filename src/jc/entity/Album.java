package jc.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ½ð³É on 2015/7/26.
 */
public class Album {

    private String cover;
    private String albumName;
    private String albumId;
    private String ownerId;
    private int sourceControl;
    private int photoCount;
    private int type;
    private List<Photo> photos;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getSourceControl() {
        return sourceControl;
    }

    public void setSourceControl(int sourceControl) {
        this.sourceControl = sourceControl;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
