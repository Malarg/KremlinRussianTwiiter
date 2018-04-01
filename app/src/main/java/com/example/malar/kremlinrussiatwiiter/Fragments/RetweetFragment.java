package com.example.malar.kremlinrussiatwiiter.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RetweetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RetweetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RetweetFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Status status;

    public RetweetFragment() {
        // Required empty public constructor
    }

    public void setStatus(Status _status){
        status = _status;
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("accountId",status.getUser().getScreenName());
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
