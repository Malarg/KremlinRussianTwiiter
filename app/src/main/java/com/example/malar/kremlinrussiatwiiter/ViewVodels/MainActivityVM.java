package com.example.malar.kremlinrussiatwiiter.ViewVodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.example.malar.kremlinrussiatwiiter.Models.Multimedia;
import com.example.malar.kremlinrussiatwiiter.Models.Tweet;
import com.example.malar.kremlinrussiatwiiter.Models.TwitterProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import twitter4j.ExtendedMediaEntity;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by malar on 23.03.2018.
 */

public class MainActivityVM extends AndroidViewModel {
    private String twitterAccount = "KremlinRussia";
    private final int tweetsPerPage = 20;
    private int currentPage = 1;
    private Observable<List<Tweet>> tweets;

    public MainActivityVM(Application application){
        super(application);
    }

    public io.reactivex.Observable<List<Tweet>> getTweets() throws TwitterException {
        if (tweets == null) {
            tweets = io.reactivex.Observable
                    .fromArray(new ArrayList<Tweet>())
                    .observeOn(Schedulers.newThread())
                    .map(s -> {
                        List<Tweet> loadedTweets = loadTweets(currentPage);
                        for (Tweet tweet : loadedTweets){
                            s.add(tweet);
                        }
                        return s;
                    });
        }
        return tweets;
    }

    public io.reactivex.Observable<List<Tweet>> loadNewTweets(){
        Observable<List<Tweet>> newTweets = Observable
                .fromArray(new ArrayList<Tweet>())
                .observeOn(Schedulers.newThread())
                .map((List<Tweet> tweets1) ->loadTweets(currentPage));
        tweets = Observable.concat(tweets, newTweets);
        currentPage++;
        return tweets;

    }

    public void setAccount (String account) { twitterAccount = account;}

    private List<Tweet> loadTweets(int page) throws TwitterException {
        List<Tweet> gotTweets = new ArrayList<Tweet>();
        List<Status> statuses = TwitterProvider.getInstance().getUserTimeline(twitterAccount, new Paging(page, tweetsPerPage));
        for (Status status: statuses) {
            List<Multimedia> mediaEntities = mapUrlMedias(status.getExtendedMediaEntities());
            Tweet tweet = new Tweet(status.getId(), status.getUser().getName(), status.getCreatedAt().toString(), status.getText(), mediaEntities);
            gotTweets.add(tweet);
        }
        return gotTweets;
    }

    private List<Multimedia> mapUrlMedias(ExtendedMediaEntity[] mediaEntities){ //это можно было сделать красиво через лямбды, но поднимать минимальную версию до 7.0 нет желания
        List<Multimedia> mappedMultimedia = new ArrayList<>();
        for (ExtendedMediaEntity entity : mediaEntities) {
            Multimedia multimedia = new Multimedia(entity.getMediaURL(), entity.getType());
            mappedMultimedia.add(multimedia);
        }
        return mappedMultimedia;
    }
}
