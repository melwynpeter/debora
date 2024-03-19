package com.mel.debora_v11.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mel.debora_v11.R;
import com.mel.debora_v11.adapters.AccountAdapter;
import com.mel.debora_v11.adapters.CardAdapter;
import com.mel.debora_v11.databinding.FragmentAccountBinding;
import com.mel.debora_v11.databinding.FragmentHistoryBinding;
import com.mel.debora_v11.models.Account;
import com.mel.debora_v11.models.Card;
import com.mel.debora_v11.models.History;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    PreferenceManager preferenceManager;

    List<Account> accountTextList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_account, container, false);

        binding = FragmentAccountBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getActivity());

        binding.accountRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        accountTextList.add(new Account("Account"));
        accountTextList.add(new Account("Security"));
        accountTextList.add(new Account("Privacy"));
        accountTextList.add(new Account("About"));
        accountTextList.add(new Account("Feedback"));
        accountTextList.add(new Account("Help"));
        accountTextList.add(new Account("sign out"));

        AccountAdapter accountAdapter = new AccountAdapter(accountTextList);
        binding.accountRecyclerView.setAdapter(accountAdapter);


        return binding.getRoot();
    }

//    private void loading(Boolean isLoading){
//        if(isLoading){
//            binding.progressBar.setVisibility(View.VISIBLE);
//        }
//        else{
//            binding.progressBar.setVisibility(View.INVISIBLE);
//        }
//    }

}