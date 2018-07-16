package com.example.malar.kremlinrussiatwiiter.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.malar.kremlinrussiatwiiter.Activities.MainActivity;
import com.example.malar.kremlinrussiatwiiter.Models.TwitterProvider;
import com.example.malar.kremlinrussiatwiiter.R;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import twitter4j.Status;


public class RetweetFragment extends Fragment {

    private Status status;

    public RetweetFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RetweetFragment newInstance(Status _status) {
        RetweetFragment fragment = new RetweetFragment();
        Bundle args = new Bundle();
        args.putLong("statusId", _status.getId()); //тут я бы все-таки передавал уйму параметров для экономии трафика, но, поскольку это не идет в продакшн, и так сойдет
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long statusId = getArguments().getLong("statusId");
        loadStatus(statusId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retweet, container, false);
        view.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("accountId",status.getUser().getScreenName());
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void loadStatus(long id){
        io.reactivex.Observable
                .just("")
                .observeOn(Schedulers.newThread())
                .map(s -> TwitterProvider.getInstance().showStatus(id))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status1 -> status = status1,s -> {},() -> onStatusLoaded());
    }

    private void onStatusLoaded() {
        ((TextView)getView().findViewById(R.id.retweetedUserNicknameTextView)).setText(status.getUser().getName());
        ((TextView)getView().findViewById(R.id.retweetTextView)).setText(status.getText());
        ImageView imageView = getView().findViewById(R.id.userAvatarImageView);

        Picasso
                .get()
                .load(status.getUser().getProfileImageURL())
                .into(imageView);
    }
}
