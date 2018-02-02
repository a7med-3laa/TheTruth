package com.ahmedalaa.Honestly.model;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by ahmed on 30/01/2018.
 */
@Parcel
public class Msg {
    String msg;
    String senderID;
    String receiverID;
    String replayMsg;
    Date date;
    String id;

    public Msg() {
    }

    public Msg(String msg, String senderID, String receiverID, String id) {
        this.msg = msg;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.id = id;
        replayMsg = "";
        date = new Date();
    }

    public String getMsg() {
        return msg;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getReplayMsg() {
        return replayMsg;
    }

    public void setReplayMsg(String replayMsg) {
        this.replayMsg = replayMsg;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

}
