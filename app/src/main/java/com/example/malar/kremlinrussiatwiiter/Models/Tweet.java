package com.example.malar.kremlinrussiatwiiter.Models;

import java.util.List;

/**
 * Created by malar on 23.03.2018.
 */

public class Tweet {
    private long id;
    private String userName;
    private String sendingDate;
    private String tweetText;
    private List<Multimedia> multimediaList;

    public Tweet() {
    }

    public Tweet(long _id, String _userName, String _sendingDate, String _tweetText, List<Multimedia> _multimediaList){
        id = _id;
        userName = _userName;
        sendingDate = _sendingDate;
        tweetText = _tweetText;
        multimediaList = _multimediaList;

    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getSendingDate() {
        return sendingDate;
    }

    public String getTweetText() {
        return tweetText;
    }

    public List<Multimedia> getMultimediaList() {
        return multimediaList;
    }
}
