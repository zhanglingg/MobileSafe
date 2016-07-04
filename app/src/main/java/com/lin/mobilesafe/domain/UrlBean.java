package com.lin.mobilesafe.domain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lin on 2016/7/4 0004.
 */
public class UrlBean {
    private String url;         // apk下载路径
    private String versionCode;    // 版本号
    private String desc;

    public UrlBean(String url, String versionCode, String desc) {
        this.url = url;
        this.versionCode = versionCode;
        this.desc = desc;
    }

    public UrlBean() {
    }

    public String getUrl() {
        return url;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 解析从服务器获取的json数据
     * @param jsonString
     */
    public void parseJson(StringBuilder jsonString) {
        try {

            // 把json字符串封装成json、对象
            JSONObject jsonObject = new JSONObject(jsonString+"");

            versionCode = jsonObject.getString("version");
            url = jsonObject.getString("url");
            desc = jsonObject.getString("desc");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
