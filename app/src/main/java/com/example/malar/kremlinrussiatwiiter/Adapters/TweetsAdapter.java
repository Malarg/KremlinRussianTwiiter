package com.example.malar.kremlinrussiatwiiter.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.malar.kremlinrussiatwiiter.Models.Multimedia;
import com.example.malar.kremlinrussiatwiiter.Models.PhotosProvider;
import com.example.malar.kremlinrussiatwiiter.Models.Tweet;
import com.example.malar.kremlinrussiatwiiter.R;
import com.example.malar.kremlinrussiatwiiter.Activities.TweetActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malar on 23.03.2018.
 */

public class TweetsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<Tweet> tweets;

    public TweetsAdapter (Context ctx){
        context = ctx;
        tweets = new ArrayList<>();
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Object getItem(int position) {
        return tweets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = inflater.inflate(R.layout.tweet_layout, parent, false);
        }

        Tweet tweet = tweets.get(position);

        setViewData(tweet, view);
        PhotosProvider.getInstance().loadPhotos(tweet, view, R.id.photosListView);

        LinearLayout videosListView = view.findViewById(R.id.videosListView);

        List<String> videoURLs = getMultimediasURL(filterMultimediaByType(tweet.getMultimediaList(), Multimedia.MultimediaType.Video));

        for (String url:videoURLs) {
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            VideoView videoView = new VideoView(context);
            videoView.setLayoutParams(params);
            Uri uri = Uri.parse(url);
            videoView.setVideoURI(uri);
            videosListView.addView(videoView);
        }

        CardView cardView = (CardView)view.findViewById(R.id.tweetCardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), TweetActivity.class);
                intent.putExtra("id", tweet.getId());
                parent.getContext().startActivity(intent);
            }
        });
        return view;
    }

    private void setViewData(Tweet tweet, View view){
        ((TextView)view.findViewById(R.id.userNameTextView)).setText(tweet.getUserName());
        ((TextView)view.findViewById(R.id.dateTextView)).setText(tweet.getSendingDate());
        ((TextView)view.findViewById(R.id.tweetTextTextView)).setText(tweet.getTweetText());
    }

    public void setTweets(List<Tweet> _tweets){
        tweets = _tweets;
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
