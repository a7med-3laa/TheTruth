package com.ahmedalaa.thetruth.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ahmed on 30/01/2018.
 */

public class Msg {
    String msg;
    String senderID;
    String reciverID;
    String reciverName;
    String reciverPic;
    String replayMsg;
    int react;
    String date;

    public Msg() {
    }

    public Msg(String msg, String senderID, String reciverID, String reciverName, String reciverPic, String replayMsg, int react) {
        this.msg = msg;
        this.senderID = senderID;
        this.reciverID = reciverID;
        this.reciverName = reciverName;
        this.reciverPic = reciverPic;
        this.replayMsg = replayMsg;
        this.react = react;
        this.date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public String getMsg() {
        return msg;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReciverID() {
        return reciverID;
    }

    public String getReplayMsg() {
        return replayMsg;
    }

    public int getReact() {
        return react;
    }

    public String getDate() {
        return date;
    }

    public String getReciverName() {
        return reciverName;
    }

    public String getReciverPic() {
        return reciverPic;
    }
}
