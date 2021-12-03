package com.sdpd.companion.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.sdpd.companion.data.model.Message;
import com.sdpd.companion.data.model.User;
import com.sdpd.companion.data.remote.FirebaseMessageSource;
import com.sdpd.companion.data.remote.FirebaseUserSource;
import com.sdpd.companion.data.repository.MessageRepository;
import com.sdpd.companion.data.repository.UserRepository;
import com.sdpd.companion.ui.chat.ChatFragmentArgs;
import com.sdpd.companion.ui.solostudy.TimerFragmentArgs;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatViewModel extends ViewModel {

    private static final String TAG = "ChatViewModel";

    MessageRepository messageRepository;
    UserRepository userRepository;

    public MutableLiveData<ArrayList<Message>> getMessages() {
        return messages;
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    private MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<String> groupId = new MutableLiveData<>();

    @Inject
    public ChatViewModel(SavedStateHandle handle, MessageRepository messageRepository, UserRepository userRepository){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        observeUser();
        observeMessages();
    }

    private void observeMessages() {
        groupId.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (groupId.getValue() == null){
                    return;
                }
                messageRepository.getMessages(groupId.getValue()).subscribe(newMessages -> {
                    messages.setValue(newMessages);
                }, error -> {
                    messages.setValue(new ArrayList<>());
                });
            }
        });
    }

    private void observeUser() {
        userRepository.getCurrentUser().subscribe(newUser -> {
            user.setValue(newUser);
        });
    }

    public void setGroupId(String groupId) {
        this.groupId.setValue(groupId);
    }

    public void sendMessage(String messageText) {
        messageRepository.sendMessage(groupId.getValue(), messageText);
    }
}
