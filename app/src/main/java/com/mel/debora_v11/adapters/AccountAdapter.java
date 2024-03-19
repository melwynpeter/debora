package com.mel.debora_v11.adapters;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.startActivity;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mel.debora_v11.R;
import com.mel.debora_v11.activities.SignInActivity;
import com.mel.debora_v11.fragments.AccountFragment;
import com.mel.debora_v11.models.Account;
import com.mel.debora_v11.models.Card;
import com.mel.debora_v11.utilities.Constants;
import com.mel.debora_v11.utilities.PreferenceManager;

import java.util.HashMap;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private Context context;
    private Activity activity;
    private PreferenceManager preferenceManager;
    private List<Account> accountList;

    public AccountAdapter(Context context, Activity activity, PreferenceManager preferenceManager, List<Account> accountList){
        this.context = context;
        this.activity = activity;
        this.preferenceManager = preferenceManager;
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

    public class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView accountText;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            accountText = itemView.findViewById(R.id.accountName);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            if(position == (accountList.size() - 1)){
                signOut();
            }
        }

        private void signOut(){
            Toast.makeText(context, "signing out", Toast.LENGTH_SHORT).show();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference =
                    db.collection(Constants.KEY_COLLECTION_USERS).document(
                            preferenceManager.getString(Constants.KEY_USER_ID)
                    );
            HashMap<String, Object> updates = new HashMap<>();
            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            preferenceManager.clear();
                            Intent intent = new Intent(context, SignInActivity.class);
                            context.startActivity(intent);
                            activity.finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Unable to sign out", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}

