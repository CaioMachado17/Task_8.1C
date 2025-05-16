package com.example.task81c;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatActivity extends AppCompatActivity {

    private ChatAdapter adapter;
    private List<ChatMessage> messages = new ArrayList<>();
    private EditText inputMessage;
    private RecyclerView recyclerChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String username = getIntent().getStringExtra("username");
        TextView textWelcome = findViewById(R.id.textWelcome);
        textWelcome.setText("Welcome, " + username + "!");

        inputMessage = findViewById(R.id.inputMessage);
        ImageButton buttonSend = findViewById(R.id.buttonSend);
        recyclerChat = findViewById(R.id.recyclerChat);

        adapter = new ChatAdapter(messages);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setAdapter(adapter);

        buttonSend.setOnClickListener(v -> {
            String userMsg = inputMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(userMsg)) {
                sendMessage(userMsg);
                inputMessage.setText("");
            }
        });
    }

    private void sendMessage(String userMessage) {
        messages.add(new ChatMessage(userMessage, true));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerChat.scrollToPosition(messages.size() - 1);

        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8080/chat");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String body = "userMessage=" + userMessage;
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(body);
                writer.flush();

                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                runOnUiThread(() -> {
                    messages.add(new ChatMessage(response.toString().trim(), false));
                    adapter.notifyItemInserted(messages.size() - 1);
                    recyclerChat.scrollToPosition(messages.size() - 1);
                });

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
