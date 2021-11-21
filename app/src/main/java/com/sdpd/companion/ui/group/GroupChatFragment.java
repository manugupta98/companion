package com.sdpd.companion.ui.group;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpd.companion.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatFragment extends Fragment {

    Group group;
    private Toolbar m;
    private ImageButton sendmessagebutton;
    @Nullable
    private EditText usermessageinput;
    private ScrollView scroll;
    private TextView displaytextmessage;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,GroupNameRef,GroupMessageKeyRef;
    private String currentGroupName,currentUserID, currentUserName, currentDate,currentTime;
    public GroupChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group= (Group)getArguments().getSerializable("selectedGroup");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(group.groupKey).child("Chats");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //groupName = getArguments().getString("chosenGroup");


        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Initializefields();
        sendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMessageInfoToDatabase();

                usermessageinput.setText("");
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    @Override
    public void onStart() {
        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    private void Initializefields() {
        m=(Toolbar)getView().findViewById( R.id.group_chat_bar_layout);
        ((AppCompatActivity)getActivity()).setSupportActionBar(m);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(group.groupName);
        sendmessagebutton=(ImageButton)getView().findViewById(R.id.sendmessagebutton);
        usermessageinput=(EditText)getView().findViewById(R.id.inputgroupmessage);
        displaytextmessage=(TextView)getView().findViewById(R.id.groupchattextdisplay);
        scroll=(ScrollView)getView().findViewById(R.id.myscrollview);

    }

    private void SaveMessageInfoToDatabase()
    {
        String message = usermessageinput.getText().toString();
        String messageKey = GroupNameRef.push().getKey();


        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(getContext(),"Please write message first...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calForData = Calendar.getInstance();
            SimpleDateFormat currentDataFormat = new SimpleDateFormat("MMM dd ,yyyy");
            currentDate = currentDataFormat.format(calForData.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calForTime.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey);
            GroupMessageKeyRef = GroupNameRef.child(messageKey);

            HashMap<String,Object> messageInfoMap =new HashMap<>();
            messageInfoMap.put("name","Anirudh");
            messageInfoMap.put("message",message);
            messageInfoMap.put("date",currentDate);
            messageInfoMap.put("time",currentTime);

            GroupMessageKeyRef.updateChildren(messageInfoMap);

        }
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            displaytextmessage.append(chatName + ":\n" +chatMessage +"\n" +chatDate +  ":\n" +chatTime +"\n\n\n");
            scroll.fullScroll(ScrollView.FOCUS_DOWN);

        }
    }

}