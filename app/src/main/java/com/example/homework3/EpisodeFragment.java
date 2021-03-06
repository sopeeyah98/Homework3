package com.example.homework3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class EpisodeFragment extends Fragment {
    private AsyncHttpClient client = new AsyncHttpClient();
    private SharedViewModel viewModel;
    private TextView textView_ep;
    private TextView textView_epName;
    private TextView textView_epDate;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode, container, false);
        textView_ep = view.findViewById(R.id.textView_episode);
        textView_epName = view.findViewById(R.id.textView_epName);
        textView_epDate = view.findViewById(R.id.textView_epDate);
        imageView = view.findViewById(R.id.imageView);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        button = view.findViewById(R.id.button_moreInfo);

        client.addHeader("Accept","application/json");
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getEpisode().observe(getViewLifecycleOwner(), new Observer<Episode>() {
            @Override
            public void onChanged(Episode episode) {
                if (episode != null){
                    textView_ep.setText("Episode " + episode.getEp());
                    textView_epName.setText("Title: " + episode.getName());
                    textView_epDate.setText("Air Date: " + episode.getAir_date());
                    try {
                        JSONArray urls = new JSONArray(episode.getCharacters());
                        for (int i = 0; i < urls.length() && i < 3; i++){
                            final int finalI = i;
                            client.get(urls.getString(i), new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    Log.d("success", new String(responseBody));
                                    try{
                                        JSONObject character = new JSONObject(new String(responseBody));
                                        if (finalI == 0)
                                            Picasso.get().load(character.getString("image")).into(imageView);
                                        else if (finalI == 1)
                                            Picasso.get().load(character.getString("image")).into(imageView2);
                                        else
                                            Picasso.get().load(character.getString("image")).into(imageView3);
                                    } catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notification = "To read more information about Episode " + textView_ep.getText().toString() + ", " +
                        "please visit: https://rickandmorty.fandom.com/wiki/" + textView_epName.getText().toString() +".";

            }
        });

        return view;
    }
}
