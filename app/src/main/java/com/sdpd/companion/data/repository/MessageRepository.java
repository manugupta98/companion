package com.sdpd.companion.data.repository;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.data.model.Message;
import com.sdpd.companion.data.model.User;
import com.sdpd.companion.data.remote.FirebaseAuthSource;
import com.sdpd.companion.data.remote.FirebaseMessageSource;
import com.sdpd.companion.data.remote.FirebaseUserSource;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MessageRepository {

    private static final String TAG = "MessageRepository";
    FirebaseAuthSource firebaseAuthSource;
    FirebaseMessageSource firebaseMessageSource;
    UserRepository userRepository;

    @Inject
    public MessageRepository(FirebaseAuthSource firebaseAuthSource, FirebaseMessageSource firebaseMessageSource, UserRepository userRepository) {
        this.firebaseAuthSource = firebaseAuthSource;
        this.firebaseMessageSource = firebaseMessageSource;
        this.userRepository = userRepository;
//        Log.d(TAG, "messages send");
//
//        firebaseMessageSource.sendMessage("-MpV68BbDx9dByuhyyMm", "Hi, How are you?");
//        firebaseMessageSource.sendMessage("-MpV68BbDx9dByuhyyMm", "Kitna time bacha hai?");
//        firebaseMessageSource.sendMessage("-MpV68BbDx9dByuhyyMm", "Kaisa lag raha hai?");
    }

    public void sendMessage(String groupId, String messageText) {
        firebaseMessageSource.sendMessage(groupId, messageText);
    }

    private Message getMessageFromSnapshot(DataSnapshot snapshot) {
        Map<String, Object> values = (Map<String, Object>) snapshot.getValue();
        return new Message(
                snapshot.getKey(),
                (String) values.get("senderId"),
                (String) values.get("senderName"),
                (String) values.get("message"),
                (long) values.get("timestamp")
        );
    }

    public Flowable<ArrayList<Message>> getMessages(String groupId, int limit){
        return firebaseMessageSource.getMessages(groupId, limit).map(snapshot -> {
            ArrayList<Message> messages = new ArrayList<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                messages.add(getMessageFromSnapshot(child));
            }
            return messages;
        });
    }

    public Single<Message> getMessageFromId(String groupId, String messageId) {
        return firebaseMessageSource.getMessageById(groupId, messageId).map(this::getMessageFromSnapshot);
    }
}
