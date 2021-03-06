package com.example.homework3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

public class CharacterFragment extends Fragment {
    private SharedViewModel viewModel;
    private ImageView imageView;
    private TextView textView_name;
    private TextView textView_status;
    private TextView textView_species;
    private TextView textView_gender;
    private TextView textView_origin;
    private TextView textView_location;
    private TextView textView_episodes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_character, container, false);

        imageView = v.findViewById(R.id.imageView_character);
        textView_name = v.findViewById(R.id.textView_locationName);
        textView_status = v.findViewById(R.id.textView_status);
        textView_species = v.findViewById(R.id.textView_species);
        textView_gender = v.findViewById(R.id.textView_gender);
        textView_origin = v.findViewById(R.id.textView_origin);
        textView_location = v.findViewById(R.id.textView_location);
        textView_episodes = v.findViewById(R.id.textView_episodes);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getCharacter().observe(getViewLifecycleOwner(), new Observer<Character>() {
            @Override
            public void onChanged(Character character) {
                if (character != null){
                    Picasso.get().load(character.getImage_url()).into(imageView);
                    textView_name.setText(character.getName());
                    textView_status.setText("Status: " + character.getStatus());
                    textView_species.setText("Species: " + character.getSpecies());
                    textView_gender.setText("Gender: " + character.getGender());
                    textView_origin.setText("Origin: " + character.getOrigin());
                    textView_location.setText("Location: " + character.getLocation());
                    textView_episodes.setText("Episodes: " + character.getEpisodes().toString().replace("[","").replace("]",""));
                }
            }
        });
        return v;
    }
}
