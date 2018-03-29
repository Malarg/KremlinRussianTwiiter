package com.example.malar.kremlinrussiatwiiter.Models;

/**
 * Created by malar on 25.03.2018.
 */

public class Multimedia {

    public enum MultimediaType {Video, Photo};
    private String url;
    private MultimediaType type;

    public Multimedia(String _url, String _type){
        url = _url;
        switch (_type){
            case "photo": type = MultimediaType.Photo; break;
            case "video": type = MultimediaType.Video; break;
        }
    }

    public String getUrl() {
        return url;
    }
    public MultimediaType getType() {
        return type;
    }
}
