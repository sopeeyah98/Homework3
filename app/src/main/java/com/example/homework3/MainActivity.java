package com.example.homework3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static String api_url = "https://rickandmortyapi.com/api/";
    private SharedViewModel viewModel;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        characterFragment();

        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        client.addHeader("Accept","application/json");

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    characterFragment();
                } else if (tab.getPosition() == 1){
                    locationFragment();
                } else {
                    episodeFragment();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    characterFragment();
                } else if (tab.getPosition() == 2){
                    episodeFragment();
                }
            }
        });
    }

    public void characterFragment(){
        client.get(api_url+"character", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    int random_int = rand.nextInt(jsonObject.getJSONObject("info").getInt("count"));
                    client.get(api_url + "character/" + Integer.toString(random_int), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                JSONObject jsonCharacter = new JSONObject(new String (responseBody));
                                JSONArray jsonEpisode = jsonCharacter.getJSONArray("episode");
                                List<Integer> episode = new ArrayList<>();
                                for (int i = 0; i < jsonEpisode.length(); i++){
                                    String url = jsonEpisode.getString(i);
                                    int index_ep = url.lastIndexOf("/");
                                    episode.add(Integer.parseInt(url.substring(index_ep + 1)));
                                }
                                Character character = new Character(jsonCharacter.getString("name"),
                                        jsonCharacter.getString("status"),
                                        jsonCharacter.getString("species"),
                                        jsonCharacter.getString("gender"),
                                        jsonCharacter.getJSONObject("origin").getString("name"),
                                        jsonCharacter.getJSONObject("location").getString("name"),
                                        jsonCharacter.getString("image"),
                                        episode);
                                viewModel.setCharacter(character);
                                loadFragment(new CharacterFragment(), R.id.fragmentContainerView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.e("error", new String(responseBody));
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("error", new String(responseBody));
            }
        });
    }

    public void locationFragment(){
        final List<Location> locations = new ArrayList<>();
        client.get(api_url + "location", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject currentObj = jsonArray.getJSONObject(i);
                        Location location = new Location(currentObj.getString("name"),
                                currentObj.getString("type"),
                                currentObj.getString("dimension"));
                        locations.add(location);
                    }
                    viewModel.setLocations(locations);
                    loadFragment(new LocationFragment(), R.id.fragmentContainerView);
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

    public void episodeFragment(){
        client.get(api_url + "episode", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    int rand_int = rand.nextInt(jsonObject.getJSONObject("info").getInt("count"));
                    client.get(api_url + "episode/" + Integer.toString(rand_int), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try{
                                JSONObject episodeObj = new JSONObject(new String(responseBody));
                                JSONArray characters_url = episodeObj.getJSONArray("characters");
                                loadFragment(new EpisodeFragment(), R.id.fragmentContainerView);
                                Episode episode = new Episode(episodeObj.getString("episode"),
                                        episodeObj.getString("name"),
                                        episodeObj.getString("air_date"),
                                        characters_url.toString());
                                viewModel.setEpisode(episode);
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.e("error", new String(responseBody));
                        }
                    });
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

    public void loadFragment(Fragment fragment, int id){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
}