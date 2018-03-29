package com.example.malar.kremlinrussiatwiiter.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.malar.kremlinrussiatwiiter.Adapters.TweetsAdapter;
import com.example.malar.kremlinrussiatwiiter.R;
import com.example.malar.kremlinrussiatwiiter.Services.NotificationService;
import com.example.malar.kremlinrussiatwiiter.ViewVodels.MainActivityVM;

import io.reactivex.android.schedulers.AndroidSchedulers;
import twitter4j.TwitterException;

public class MainActivity extends AppCompatActivity {

    MainActivityVM viewModel;
    TweetsAdapter tweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainActivityVM.class);

        setAccountIdFromIntent();

        tweetsAdapter = new TweetsAdapter(this);

        ListView listView = (ListView) findViewById(R.id.twittersList);
        listView.setAdapter(tweetsAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (tweetsAdapter.getCount() - 1)) {

                    viewModel.loadNewTweets();
                    updateView();
                    tweetsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        updateView();
    }

    private void setAccountIdFromIntent(){
        Intent intent = getIntent();
        String accountId = intent.getStringExtra("accountId");
        if (accountId == null){
            viewModel.setAccount("KremlinRussia");
            startService(new Intent(this, NotificationService.class));
        }
        else{
            viewModel.setAccount(accountId);
        }
    }

    private void updateView(){
        try {
            viewModel.getTweets().observeOn(AndroidSchedulers.mainThread()).firstElement().subscribe(tweets ->
            {
                tweetsAdapter.setTweets(tweets);
                tweetsAdapter.notifyDataSetChanged();
            });
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
