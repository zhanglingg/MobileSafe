package com.lin.mobilesafe.domain;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class BlackNameBean {

    private int autoId;
    private String phoneNum;
    private String mode;
    private String remark;

    public BlackNameBean() {
    }

    public int getAutoId() {
        return autoId;
    }

    public void setAutoId(int autoId) {
        this.autoId = autoId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "phoneNum:" + phoneNum + "  mode:" + mode;
    }
}
