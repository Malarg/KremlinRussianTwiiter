package com.example.malar.kremlinrussiatwiiter.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.malar.kremlinrussiatwiiter.Models.Multimedia;
import com.example.malar.kremlinrussiatwiiter.Models.PhotosProvider;
import com.example.malar.kremlinrussiatwiiter.Models.Tweet;
import com.example.malar.kremlinrussiatwiiter.Models.TwitterProvider;
import com.example.malar.kremlinrussiatwiiter.R;
import com.example.malar.kremlinrussiatwiiter.Fragments.RetweetFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import twitter4j.ExtendedMediaEntity;
import twitter4j.Status;
import twitter4j.TwitterException;

public class TweetActivity extends AppCompatActivity{
    private Status status;
    private List<Status> retweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        loadStatus(id);

    }

    private void setViewData() throws TwitterException {
        ((TextView)findViewById(R.id.userNickNameTextView)).setText(status.getUser().getName());
        ((TextView)findViewById(R.id.dateTextView)).setText(status.getCreatedAt().toString());
        ((TextView)findViewById(R.id.tweetTextTextView)).setText(status.getText());
        io.reactivex.Observable
                .just("")
                .observeOn(Schedulers.newThread())
                .map(s -> TwitterProvider.getInstance().getRetweets(status.getId()))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> retweets = s, s -> {}, () -> onGotRetweets());
    }

    private void onGotRetweets(){
        LinearLayout linearLayout = findViewById(R.id.retweetsLinearLayout);
        int number = 0;
        for (Status retweet : retweets) {
            getFragmentManager().beginTransaction().add(linearLayout.getId(), RetweetFragment.newInstance(retweet), "Number: " + number).commit();
        }
    }

    private void onStatusLoaded() throws TwitterException {
        setViewData();
        Tweet tweet = new Tweet(status.getId(), status.getUser().getName(), status.getCreatedAt().toString(),status.getText(), mapUrlMedias(status.getExtendedMediaEntities()));
        PhotosProvider.getInstance().loadPhotos(tweet, findViewById(R.id.tweetRootLayout), R.id.photosLinearLayout);
    }

    private void loadStatus(long id){
        io.reactivex.Observable
                .just("")
                .observeOn(Schedulers.newThread())
                .map(s -> TwitterProvider.getInstance().showStatus(id))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status1 -> status = status1,s -> {},() -> onStatusLoaded());
    }

    private List<Multimedia> mapUrlMedias(ExtendedMediaEntity[] mediaEntities){
        List<Multimedia> mappedMultimedia = new ArrayList<>();
        for (ExtendedMediaEntity entity : mediaEntities) {
            Multimedia multimedia = new Multimedia(entity.getMediaURLHttps(), entity.getType());
            mappedMultimedia.add(multimedia);
        }
        return mappedMultimedia;
    }
}
