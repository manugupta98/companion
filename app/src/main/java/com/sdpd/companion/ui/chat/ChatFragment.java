package com.sdpd.companion.ui.chat;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdpd.companion.R;
import com.sdpd.companion.viewmodels.AnalyticsViewModel;
import com.sdpd.companion.viewmodels.ChatViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    ChatViewModel chatViewModel;
    AnalyticsViewModel analyticsViewModel;

    ChatRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    TextView sendMessageTextView;
    ImageButton sendButton;

    ActionBar toolbar;

    public ChatFragment() {
        super(R.layout.fragment_chat);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        analyticsViewModel = new ViewModelProvider(getActivity()).get(AnalyticsViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        analyticsViewModel.stopTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        sendMessageTextView = view.findViewById(R.id.send_message_text);
        sendButton = view.findViewById(R.id.send_button);

        recyclerView = view.findViewById(R.id.chats_recycler_view);
        adapter = new ChatRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String groupId = ChatFragmentArgs.fromBundle(getArguments()).getGroupId();
        String groupName = ChatFragmentArgs.fromBundle(getArguments()).getGroupName();

        chatViewModel.setGroupId(groupId);
        analyticsViewModel.setDetails("study", groupName);
        analyticsViewModel.startTracking();

        attachSendButtonListener();
        observeUser();
        observeMessages();
        return view;
    }

    private void attachSendButtonListener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = sendMessageTextView.getText().toString();
                if (!messageText.equals("")) {
                    chatViewModel.sendMessage(messageText);
                }
                sendMessageTextView.setText("");
            }
        });
    }

    private void observeUser() {
        chatViewModel.getUser().observeForever(newUser -> {
            adapter.setUser(newUser);
        });
    }

    private void observeMessages() {
        chatViewModel.getMessages().observeForever(newMessages -> {
            adapter.setMessages(newMessages);
//            int newPosition = adapter.getItemCount() - 1;
//            recyclerView.smoothScrollToPosition(newPosition);
        });
    }

}
