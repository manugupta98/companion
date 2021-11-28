package com.sdpd.companion.ui.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sdpd.companion.R;
import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.data.model.Message;
import com.sdpd.companion.data.model.User;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ChatRecyclerViewAdapter";
    private ArrayList<Message> messages;
    private Context context;
    private User user;

    public static final int INCOMING_MESSAGE = 0;
    public static final int OUTGOING_MESSAGE = 1;



    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderId().equals(user.getUid())){
            return OUTGOING_MESSAGE;
        }
        return INCOMING_MESSAGE;
    }


    public ChatRecyclerViewAdapter(Context context) {
        this.context = context;
        messages = new ArrayList<>();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == INCOMING_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.speech_bubble_incoming, parent, false);
            return new IncomingMessageViewHolder(view);
        } else if (viewType == OUTGOING_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.speech_bubble_outgoing, parent, false);
            return new OutgoingMessageViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final int itemType = getItemViewType(position);
        Message currentMessage = messages.get(position);

        if (itemType == INCOMING_MESSAGE) {
            IncomingMessageViewHolder viewHolder = (IncomingMessageViewHolder) holder;
            viewHolder.messageText.setText(currentMessage.getMessage());
        } else if (itemType == OUTGOING_MESSAGE) {
            OutgoingMessageViewHolder viewHolder = (OutgoingMessageViewHolder) holder;
            viewHolder.messageText.setText(currentMessage.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class IncomingMessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;

        public IncomingMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message);
        }
    }

    public class OutgoingMessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;

        public OutgoingMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message);
        }
    }

    public void setUser(User newUser){
        user = newUser;
    }

    public void setMessages(ArrayList<Message> newMessages) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return messages.size();
            }

            @Override
            public int getNewListSize() {
                return newMessages.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return messages.get(oldItemPosition).getId() ==
                        newMessages.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Message newMessage = newMessages.get(newItemPosition);
                Message oldMessage = messages.get(oldItemPosition);
                return newMessage.getId() == oldMessage.getId();
            }
        });
        messages = newMessages;
        Log.d(TAG, messages.toString());
        result.dispatchUpdatesTo(this);
    }
}
