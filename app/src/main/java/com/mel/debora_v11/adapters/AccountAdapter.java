package com.mel.debora_v11.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.debora_v11.R;
import com.mel.debora_v11.models.Account;
import com.mel.debora_v11.models.Card;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<Account> accountList;

    public AccountAdapter(List<Account> accountList){
        this.accountList = accountList;
    }
    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        holder.accountText.setText(accountList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder{

        private TextView accountText;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);

            accountText = itemView.findViewById(R.id.accountName);
        }
    }
}

