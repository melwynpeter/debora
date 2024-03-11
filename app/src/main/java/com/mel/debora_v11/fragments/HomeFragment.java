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

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());


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
        cardTextList2.add(new Card("Activity Card 1"));
        cardTextList2.add(new Card("Activity Card 2"));
        cardTextList2.add(new Card("Activity Card 3"));
        cardTextList2.add(new Card("Activity Card 4"));
        cardTextList2.add(new Card("Activity Card 5"));

        CardAdapter cardAdapter2 = new CardAdapter(cardTextList2);
        binding.activitiesRecyclerView.setAdapter(cardAdapter2);
        return binding.getRoot();

    }
}