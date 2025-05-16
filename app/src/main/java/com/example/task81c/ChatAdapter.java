package com.example.task81c;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;

        UserViewHolder(View view) {
            super(view);
            textMessage = view.findViewById(R.id.textUserMessage);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;

        BotViewHolder(View view) {
            super(view);
            textMessage = view.findViewById(R.id.textBotMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isUser() ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_bot, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).textMessage.setText(message.getMessage());
        } else if (holder instanceof BotViewHolder) {
            String botMessage = message.getMessage();
            if (botMessage != null && !botMessage.trim().isEmpty()) {
                ((BotViewHolder) holder).textMessage.setText(botMessage);
            } else {
                ((BotViewHolder) holder).textMessage.setText("");
            }
        }
    }
}
