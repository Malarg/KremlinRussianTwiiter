package com.example.malar.kremlinrussiatwiiter.Models;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malar on 28.03.2018.
 */

public class PhotosProvider {
    private static PhotosProvider instance;

    public static PhotosProvider getInstance() {
        if (instance == null)
            instance = new PhotosProvider();
        return instance;
    }

    private PhotosProvider(){

    }

    public void loadPhotos(Tweet tweet, View view, int photosLayoutId){
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        List<String> photosListViewUrls = getMultimediasURL(filterMultimediaByType(tweet.getMultimediaList(), Multimedia.MultimediaType.Photo));
        //List<String> photosListViewUrls = tweet.getMultimediaList().stream()
        //        .filter(multimedia -> multimedia.getType() == Multimedia.MultimediaType.Photo)
        //        .map(multimedia -> multimedia.getUrl());
        LinearLayout photosListView = (LinearLayout) view.findViewById(photosLayoutId);
        photosListView.removeAllViews();
        for (String url : photosListViewUrls){
            ImageView imageView = new ImageView(photosListView.getContext());
            params.bottomMargin = 8;
            imageView.setLayoutParams(params);
            photosListView.addView(imageView);
            Picasso.get()
                    .load(url)
                    .into(imageView);
        }
        photosListView.setLayoutParams(params);
    }

    private List<Multimedia> filterMultimediaByType(List<Multimedia> multimedias, Multimedia.MultimediaType type){
        List<Multimedia> filteredMultimedias = new ArrayList<Multimedia>();
        for (Multimedia multimedia: multimedias) {
            if (multimedia.getType() == type){
                filteredMultimedias.add(multimedia);
            }
        }
        return filteredMultimedias;
    }

    private List<String> getMultimediasURL(List<Multimedia> multimedias){
        List<String> urls = new ArrayList<>();
        for (Multimedia multimedia: multimedias) {
            urls.add(multimedia.getUrl());
        }
        return urls;
    }
}
