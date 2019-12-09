package com.odelan.yang.rawaste.Activity.Chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odelan.yang.rawaste.Activity.BaseActivity;
import com.odelan.yang.rawaste.Adapter.MessageAdapter;
import com.odelan.yang.rawaste.Model.Chat;
import com.odelan.yang.rawaste.Model.Contact;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.txt_name) TextView txt_name;
    @BindView(R.id.edit_message) EditText edit_message;

    private User user;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    MessageAdapter adapter;
    List<Chat> chats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        user = getIntent().getExtras().getParcelable(Constants.INTENT_USER);
        setActivity();
    }

    void setActivity() {
        txt_name.setText(user.firstname + " " + user.lastname);
        edit_message.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (!edit_message.getText().toString().isEmpty()) {
                    sendMessage(edit_message.getText().toString());
                }
                return true;
            }
            return false;
        });
        adapter = new MessageAdapter(this, user, chats);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);


        String room = BaseActivity.getChatId(AppData.user, user);
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHAT).child(room).limitToLast(100).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    chats.add(chat);
                    adapter.notifyDataSetChanged();
                    recycler_view.scrollToPosition(chats.size() - 1);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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

        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CONTACT).child(AppData.user.ID).child(user.ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Contact old = dataSnapshot.getValue(Contact.class);
                    old.unread_count = 0;
                    old.user = user;
                    FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CONTACT).child(AppData.user.ID).child(user.ID).setValue(old);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    void sendMessage(String message) {
        /**Add chat*/
        final Chat chat = new Chat();
        chat.sender = AppData.user.ID;
        chat.message = message;
        chat.type = Constants.FIREBASE_TEXT;
        chat.timestamp = System.currentTimeMillis() / 1000;

        String room = BaseActivity.getChatId(AppData.user, user);

        FirebaseApp.initializeApp(this);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        chat.id = reference.child(Constants.FIREBASE_CHAT).child(room).push().getKey();
        reference.child(Constants.FIREBASE_CHAT).child(room).child(chat.id).setValue(chat);

        /***Add Contact*/

        reference.child(Constants.FIREBASE_CONTACT).child(user.ID).child(AppData.user.ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Contact old = dataSnapshot.getValue(Contact.class);
                    old.unread_count++;
                    old.user = AppData.user;
                    old.message = chat.message;
                    old.timestamp = chat.timestamp;
                    old.type = chat.type;

                    reference.child(Constants.FIREBASE_CONTACT).child(user.ID).child(AppData.user.ID).setValue(old);
                } else {
                    Contact contact = new Contact();
                    contact.message = chat.message;
                    contact.timestamp = chat.timestamp;
                    contact.type = chat.type;
                    contact.user = AppData.user;
                    contact.unread_count = 1;

                    reference.child(Constants.FIREBASE_CONTACT).child(user.ID).child(AppData.user.ID).setValue(contact);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast(databaseError.getMessage());
            }
        });

        Contact contact = new Contact();
        contact.message = chat.message;
        contact.timestamp = chat.timestamp;
        contact.type = chat.type;
        contact.user = user;
        contact.unread_count = 0;

        reference.child(Constants.FIREBASE_CONTACT).child(AppData.user.ID).child(user.ID).setValue(contact);
        edit_message.setText("");
    }

    @OnClick(R.id.btn_back) void onClickBack() { finish(); }
}
