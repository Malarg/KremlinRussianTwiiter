package com.example.malar.kremlinrussiatwiiter.Models;

import android.content.res.Resources;

import com.example.malar.kremlinrussiatwiiter.R;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by malar on 28.03.2018.
 */

public class TwitterProvider {
    private static TwitterProvider instance;
    private Twitter twitter;
    private int tweetsPerPage = 20;

    public static Twitter getInstance() {
        if (instance == null)
            instance = new TwitterProvider();
        return instance.twitter;
    }

    private TwitterProvider(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("hbJWpcn3dlwSpFPog3C6kRobw")
                .setOAuthConsumerSecret("X9NNWWLXk2GfWTWn8vRq1L40A7rFQYv0EUmBPp6PmBqmPRQRQZ")
                .setOAuthAccessToken("3963791477-OY3c3BLDujy8Y24BggXks8DdJb8JoHIGZg63bgk")
                .setOAuthAccessTokenSecret("kvNPsMGBcMqyQNMK49tV5cb0uSP6FcHG4c85byEvSmKrF");
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        twitter = twitterFactory.getInstance();
    }
}
