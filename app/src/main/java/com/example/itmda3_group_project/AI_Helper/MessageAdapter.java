package com.example.itmda3_group_project.AI_Helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == Message.TYPE_USER) {
            return new UserHolder(inflater.inflate(R.layout.item_message_user, parent, false));
        }
        return new AiHolder(inflater.inflate(R.layout.item_message_ai, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message m = messages.get(position);
        if (holder instanceof UserHolder) {
            ((UserHolder) holder).tv.setText(m.getText());
        } else {
            ((AiHolder) holder).tv.setText(m.getText());
        }
    }

    @Override
    public int getItemCount() { return messages.size(); }

    static class UserHolder extends RecyclerView.ViewHolder {
        TextView tv;
        UserHolder(View v) { super(v); tv = v.findViewById(R.id.tvUserMessage); }
    }

    static class AiHolder extends RecyclerView.ViewHolder {
        TextView tv;
        AiHolder(View v) { super(v); tv = v.findViewById(R.id.tvAiMessage); }
    }
}