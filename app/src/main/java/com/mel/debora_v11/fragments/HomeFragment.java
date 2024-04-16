package com.mel.debora_v11.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mel.debora_v11.R;
import com.mel.debora_v11.adapters.CardAdapter;
import com.mel.debora_v11.databinding.FragmentHomeBinding;
import com.mel.debora_v11.models.Card;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        preferenceManager = new PreferenceManager(getActivity());

        // get greeting and Display TextView
        binding.greetingTextView.setText(getGreeting());

        // set Username Display TextView
        binding.usernameDisplayTextView.setText(preferenceManager.getString(Constants.KEY_USERNAME));
        binding.recentRecyclerView.setHasFixedSize(true);
        binding.recentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<Card> cardTextList = new ArrayList<>();
        cardTextList.add(new Card("Card 1"));
        cardTextList.add(new Card("Card 2"));
        cardTextList.add(new Card("Card 3"));
        cardTextList.add(new Card("Card 4"));
        cardTextList.add(new Card("Card 5"));

        CardAdapter cardAdapter = new CardAdapter(cardTextList);
        binding.recentRecyclerView.setAdapter(cardAdapter);



        binding.activitiesRecyclerView.setHasFixedSize(true);
        binding.activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<Card> cardTextList2 = new ArrayList<>();
        cardTextList2.add(new Card("generate some text"));
        cardTextList2.add(new Card("generate an email"));
        cardTextList2.add(new Card("set an alarm"));
        cardTextList2.add(new Card("set a timer"));
        cardTextList2.add(new Card("play a video from youtube"));

        CardAdapter cardAdapter2 = new CardAdapter(cardTextList2);
        binding.activitiesRecyclerView.setAdapter(cardAdapter2);
        return binding.getRoot();

    }

    private String getGreeting(){
        String greeting = "";
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            greeting = "Good Morning!";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greeting = "Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greeting = "Good Evening";
        }
        else{
            greeting = "Good Evening";
        }
        return greeting;
    }
}