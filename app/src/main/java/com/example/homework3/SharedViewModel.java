package com.example.homework3;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Character> character = new MutableLiveData<>();
    private MutableLiveData<List<Location>> locations = new MutableLiveData<>();
    private MutableLiveData<Episode> episode = new MutableLiveData<>();


    public MutableLiveData<Character> getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character.setValue(character);
    }

    public MutableLiveData<List<Location>> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations.setValue(locations);
    }

    public MutableLiveData<Episode> getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode.setValue(episode);
    }
}
