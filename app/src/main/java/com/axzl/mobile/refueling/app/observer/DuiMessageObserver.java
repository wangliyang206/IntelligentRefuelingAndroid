package com.axzl.mobile.refueling.app.observer;

import android.os.Bundle;
import android.text.TextUtils;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;
import com.axzl.mobile.refueling.mvp.model.entity.MessageBean;
import com.axzl.mobile.refueling.mvp.ui.activity.MainActivity;
import com.blankj.utilcode.util.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

/**
 * 客户端MessageObserver, 用于处理客户端动作的消息响应.
 */
public class DuiMessageObserver implements MessageObserver {
    private final String Tag = "DuiMessageObserver";

    public interface MessageCallback {
        void onMessageAdd(MessageBean bean);

        void onMessagePollLast();

        void onState(String state);
    }

    private MessageCallback mMessageCallback;
    private boolean mIsFirstVar = true;
    private boolean mHasvar = false;
    private String[] mSubscribeKeys = new String[]{
            "sys.dialog.state",
            "context.output.text",
            "context.input.text",
            "context.widget.content",
            "context.widget.list",
            "context.widget.web",
            "context.widget.media"};

    // 注册当前更新消息
    public void regist(MessageCallback messageCallback) {
        mMessageCallback = messageCallback;
        DDS.getInstance().getAgent().subscribe(mSubscribeKeys, this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    /**
     * 模拟结果
     */
    private void result(int i) {
        MessageBean bean = new MessageBean();

        if (i == 1) {
            MessageBean a1 = new MessageBean();
            a1.setTitle("汽油90号");
            a1.setSubTitle("");
            bean.addMessageBean(a1);

            MessageBean a2 = new MessageBean();
            a2.setTitle("汽油92号");
            a2.setSubTitle("");
            bean.addMessageBean(a2);

            MessageBean a3 = new MessageBean();
            a3.setTitle("汽油95号");
            a3.setSubTitle("");
            bean.addMessageBean(a3);

            MessageBean a4 = new MessageBean();
            a4.setTitle("汽油98号");
            a4.setSubTitle("");
            bean.addMessageBean(a4);

            MessageBean a5 = new MessageBean();
            a5.setTitle("柴油0号");
            a5.setSubTitle("");
            bean.addMessageBean(a5);
        } else {
            MessageBean b1 = new MessageBean();
            b1.setTitle("100元");
            b1.setSubTitle("");
            bean.addMessageBean(b1);

            MessageBean b2 = new MessageBean();
            b2.setTitle("200元");
            b2.setSubTitle("");
            bean.addMessageBean(b2);

            MessageBean b3 = new MessageBean();
            b3.setTitle("250元");
            b3.setSubTitle("");
            bean.addMessageBean(b3);

            MessageBean b4 = new MessageBean();
            b4.setTitle("300元");
            b4.setSubTitle("");
            bean.addMessageBean(b4);

            MessageBean b5 = new MessageBean();
            b5.setTitle("350元");
            b5.setSubTitle("");
            bean.addMessageBean(b5);
        }

        bean.setCurrentPage(1);
        bean.setType(MessageBean.TYPE_WIDGET_LIST);
        bean.setItemsPerPage(5);

        if (mMessageCallback != null) {
            mMessageCallback.onMessageAdd(bean);
        }
    }

    @Override
    public void onMessage(String message, String data) {
        Timber.i(Tag + " message : " + message + " data : " + data);
        MessageBean bean = null;
        switch (message) {
            case "context.output.text":
                bean = new MessageBean();
                String txt = "";
                try {
                    JSONObject jo = new JSONObject(data);
                    txt = jo.optString("text", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 如果当前界面属于关闭状态，则需要调起界面；其它按正常流程继续。
                if (txt.contains("我在，有什么可以帮你") && !ActivityUtils.isActivityExistsInStack(MainActivity.class)) {
                    Bundle options = new Bundle();
                    options.putBoolean("isService", true);
                    options.putString("tips", txt);
                    ActivityUtils.startActivity(options, MainActivity.class);
                } else {
                    bean.setText(txt);
                    bean.setType(MessageBean.TYPE_OUTPUT);
                    if (mMessageCallback != null) {
                        mMessageCallback.onMessageAdd(bean);
                    }

                    /* 以下是特殊处理 */
                    if (txt.contains("请选择油品")) {
                        result(1);
                    } else if (txt.contains("请选择加油金额")) {
                        result(2);
                    } else if (txt.contains("操作成功，请及时扫描二维码进行支付。")) {
                        MessageBean qrCode = new MessageBean();
                        qrCode.setText(txt);
                        qrCode.setType(MessageBean.TYPE_QR_CODE);
                        if (mMessageCallback != null) {
                            mMessageCallback.onMessageAdd(qrCode);
                        }
                    }

                }
                break;
            case "context.input.text":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.has("var")) {
                        String var = jo.optString("var", "");
                        if (mIsFirstVar) {
                            mIsFirstVar = false;
                            mHasvar = true;
                            bean.setText(var);
                            bean.setType(MessageBean.TYPE_INPUT);
                            if (mMessageCallback != null) {
                                mMessageCallback.onMessageAdd(bean);
                            }
                        } else {
                            if (mMessageCallback != null) {
                                mMessageCallback.onMessagePollLast();
                            }
                            bean.setText(var);
                            bean.setType(MessageBean.TYPE_INPUT);
                            if (mMessageCallback != null) {
                                mMessageCallback.onMessageAdd(bean);
                            }
                        }
                    }
                    if (jo.has("text")) {
                        if (mHasvar) {
                            if (mMessageCallback != null) {
                                mMessageCallback.onMessagePollLast();
                            }
                            mHasvar = false;
                            mIsFirstVar = true;
                        }
                        String text = jo.optString("text", "");
                        bean.setText(text);
                        bean.setType(MessageBean.TYPE_INPUT);
                        if (mMessageCallback != null) {
                            mMessageCallback.onMessageAdd(bean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.content":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    String title = jo.optString("title", "");
                    String subTitle = jo.optString("subTitle", "");
                    String imgUrl = jo.optString("imageUrl", "");
                    bean.setTitle(title);
                    bean.setSubTitle(subTitle);
                    bean.setImgUrl(imgUrl);
                    bean.setType(MessageBean.TYPE_WIDGET_CONTENT);
                    if (mMessageCallback != null) {
                        mMessageCallback.onMessageAdd(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.list":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    JSONArray array = jo.optJSONArray("content");
                    if (array == null || array.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        String title = object.optString("title", "");
                        String subTitle = object.optString("subTitle", "");
                        MessageBean b = new MessageBean();
                        b.setTitle(title);
                        b.setSubTitle(subTitle);
                        bean.addMessageBean(b);
                    }
                    int currentPage = jo.optInt("currentPage");
                    bean.setCurrentPage(currentPage);
                    bean.setType(MessageBean.TYPE_WIDGET_LIST);

                    int itemsPerPage = jo.optInt("itemsPerPage");
                    bean.setItemsPerPage(itemsPerPage);

                    if (mMessageCallback != null) {
                        mMessageCallback.onMessageAdd(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.web":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    String url = jo.optString("url");
                    bean.setUrl(url);
                    bean.setType(MessageBean.TYPE_WIDGET_WEB);
                    if (mMessageCallback != null) {
                        mMessageCallback.onMessageAdd(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "context.widget.media":
                bean = new MessageBean();
                try {
                    JSONObject jo = new JSONObject(data);
                    JSONArray contentArray = jo.getJSONArray("content");
                    if (contentArray != null) {
                        JSONObject contentObj = (JSONObject) contentArray.get(0);
                        if (contentObj != null) {
                            String url = contentObj.getString("linkUrl");
                            if (TextUtils.isEmpty(url)) {
                                return;
                            }
                            bean.setUrl(url);
                            bean.setType(MessageBean.TYPE_WIDGET_WEB);
                            if (mMessageCallback != null) {
                                mMessageCallback.onMessageAdd(bean);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "sys.dialog.state":
                if (mMessageCallback != null) {
                    mMessageCallback.onState(data);
                }
                break;
        }
    }

}
