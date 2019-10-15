package com.tuna.demo6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tuna.demo6.adapter.ChatAdapter;
import com.tuna.demo6.model.Chat;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcView;
    private EditText edtSend;
    private ImageButton btnSend;

    private List<Chat> chatList;
    private ChatAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh");
        } catch (URISyntaxException e) {
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mSocket.on("new message", onNewMessage);
        mSocket.connect();
        mSocket.emit("add user", "Vương Bắc");
        mSocket.on("login", onLogin);

        edtSend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i==4){
                    attemptSend();
                }
                return true;
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });
    }

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    int numUsers;
                    try {
                        numUsers = data.getInt("numUsers");
                        Toast.makeText(MainActivity.this, "Có " + numUsers + " trong phòng chat",
                                Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        return;
                    }

                }
            });

        }
    };
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;

                    try {
                        username = data.getString("username");
                        message = data.getString("message");

                        Chat chat=new Chat();
                        chat.setMessage(message);
                        chat.setName(username);
                        chatList.add(chat);
                        adapter.notifyDataSetChanged();
                        rcView.smoothScrollToPosition(chatList.size() -1);
                    } catch (JSONException e) {
                        return;
                    }


                }
            });
        }
    };

    private void initView() {
        rcView=findViewById(R.id.rcView);
        edtSend=findViewById(R.id.edtSend);
        btnSend = findViewById(R.id.btnSend);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        chatList = new ArrayList<>();
        adapter=new ChatAdapter(getApplicationContext(),chatList);
        rcView.setAdapter(adapter);
        rcView.setLayoutManager(linearLayoutManager);
    }

    private void attemptSend() {
        String message = edtSend.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        edtSend.setText("");
        Chat chat=new Chat();
        chat.name = "Vương Bắc";
        chat.message=message;
        chatList.add(chat);
        adapter.notifyDataSetChanged();
        rcView.smoothScrollToPosition(chatList.size()-1);
        mSocket.emit("new message", message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
