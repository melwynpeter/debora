package com.mel.debora_v11.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.debora_v11.R;
import com.mel.debora_v11.activities.ChatActivity;
import com.mel.debora_v11.databinding.ItemContainerHistoryBinding;
import com.mel.debora_v11.models.GeneratedText;
import com.mel.debora_v11.models.History;
import com.mel.debora_v11.utilities.Constants;

import java.util.List;

public class GeneratedTextsAdapter extends RecyclerView.Adapter<GeneratedTextsAdapter.GeneratedTextsViewHolder>{
    private Context context;
    private List<GeneratedText> generatedTextsList;
    private String userId;
    public GeneratedTextsAdapter(List<GeneratedText> generatedTextsList, String userId){
        this.generatedTextsList = generatedTextsList;
        this.userId = userId;
    }
    public GeneratedTextsAdapter(List<GeneratedText> generatedTextsList, String userId, Context context){
        this.generatedTextsList = generatedTextsList;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public GeneratedTextsAdapter.GeneratedTextsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_generated_text, parent, false);
        return new GeneratedTextsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneratedTextsAdapter.GeneratedTextsViewHolder holder, int position) {
        GeneratedText generatedText = generatedTextsList.get(position);

        holder.generatedTextsTextView.setText(generatedText.generatedTextPrompt);
        holder.generatedTextsDateTimeTextView.setText(generatedText.dateTime);
    }

    @Override
    public int getItemCount() {
        return generatedTextsList.size();
    }

    public class GeneratedTextsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView generatedTextsTextView;
        public TextView generatedTextsDateTimeTextView;

        public GeneratedTextsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            generatedTextsTextView = itemView.findViewById(R.id.generated_text);
            generatedTextsDateTimeTextView = itemView.findViewById(R.id.textDateTime);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            GeneratedText generatedText = generatedTextsList.get(position);
            String actualGeneratedText = generatedText.generatedText;
            showBottomDialog(context, actualGeneratedText);


        }

        private void showBottomDialog(Context context, String taskString){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bottomsheet_layout);

            ImageView closeDialogButton = dialog.findViewById(R.id.closeDialogButton);

            closeDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            TextView textView = dialog.findViewById(R.id.taskText);
            // set text
            if(taskString != ""){
                textView.setText(taskString);
            }

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
    }
}
