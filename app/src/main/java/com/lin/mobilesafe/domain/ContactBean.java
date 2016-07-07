package com.lin.mobilesafe.domain;

/**
 * Created by Administrator on 2016/7/7 0007.
 */
public class ContactBean {

    private String displayName;
    private String phoneNum;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "姓名：" + displayName + "  电话号码：" + phoneNum;
    }
}
