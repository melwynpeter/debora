package com.mel.debora_v11.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.debora_v11.R;
import com.mel.debora_v11.models.GeneralQA;
import com.mel.debora_v11.models.GeneratedText;

import java.util.List;

public class GeneralQAAdapter extends RecyclerView.Adapter<GeneralQAAdapter.GeneralQAViewHolder>{
    private Context context;
    private List<GeneralQA> generalQAList;
    private String userId;
    public GeneralQAAdapter(List<GeneralQA> generalQAList, String userId){
        this.generalQAList = generalQAList;
        this.userId = userId;
    }
    public GeneralQAAdapter(List<GeneralQA> generalQAList, String userId, Context context){
        this.generalQAList = generalQAList;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public GeneralQAAdapter.GeneralQAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_general_qa, parent, false);
        return new GeneralQAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralQAAdapter.GeneralQAViewHolder holder, int position) {
        GeneralQA generalQA = generalQAList.get(position);

        holder.generalQATextView.setText(generalQA.generalQAPrompt);
        holder.generalQADateTimeTextView.setText(generalQA.dateTime);
    }

    @Override
    public int getItemCount() {
        return generalQAList.size();
    }

    public class GeneralQAViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView generalQATextView;
        public TextView generalQADateTimeTextView;

        public GeneralQAViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            generalQATextView = itemView.findViewById(R.id.general_qa);
            generalQADateTimeTextView = itemView.findViewById(R.id.textDateTime);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            GeneralQA generalQA = generalQAList.get(position);
            String actualGeneralQAResponse = generalQA.generalQAResponse;
            showBottomDialog(context, actualGeneralQAResponse);


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
