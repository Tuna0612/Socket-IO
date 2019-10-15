package com.tuna.demo6.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuna.demo6.R;
import com.tuna.demo6.model.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    private Context context;
    private List<Chat> chatList;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_right, parent, false);
            return new ChatHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_left, parent, false);
            return new ChatHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        holder.tvUser.setText(chatList.get(position).name);
        holder.tvContent.setText(chatList.get(position).message);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String namea = chatList.get(position).name;
        if(namea.equals("Vương Bắc")){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvUser;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvUser = itemView.findViewById(R.id.tvUser);
        }
    }
}
