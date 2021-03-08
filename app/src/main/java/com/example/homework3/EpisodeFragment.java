package com.example.homework3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
                                    Log.e("error", new String(responseBody));
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
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://rickandmorty.fandom.com/wiki/"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
                String url = "https://rickandmorty.fandom.com/wiki/" + textView_epName.getText().toString().replace(" ","_");
                String notification = "To read more information about Episode " + textView_ep.getText().toString() + ", " +
                        "please visit: " + url +".";

                createNotificationChannel();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "CHANNEL1")
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setContentTitle("More Info")
                        .setContentText(notification)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notification))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                notificationManager.notify(1, builder.build());
            }
        });
        return view;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
