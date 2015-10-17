package jc.entity;

/**
 * Created by ½ð³É on 2015/7/26.
 */
public class PhotoAtExt {

    int control;
    long createTime;
    boolean parPublic;
    int privacyGroupId;
    String owner;
    String ugcGID;
    //boolean public;
    String parUgcGID;
    long parCreateTime;

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isParPublic() {
        return parPublic;
    }

    public void setParPublic(boolean parPublic) {
        this.parPublic = parPublic;
    }

    public int getPrivacyGroupId() {
        return privacyGroupId;
    }

    public void setPrivacyGroupId(int privacyGroupId) {
        this.privacyGroupId = privacyGroupId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUgcGID() {
        return ugcGID;
    }

    public void setUgcGID(String ugcGID) {
        this.ugcGID = ugcGID;
    }

    public String getParUgcGID() {
        return parUgcGID;
    }

    public void setParUgcGID(String parUgcGID) {
        this.parUgcGID = parUgcGID;
    }

    public long getParCreateTime() {
        return parCreateTime;
    }

    public void setParCreateTime(long parCreateTime) {
        this.parCreateTime = parCreateTime;
    }
}
