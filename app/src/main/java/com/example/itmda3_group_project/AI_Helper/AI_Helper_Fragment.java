package com.example.itmda3_group_project.AI_Helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AI_Helper_Fragment extends Fragment {

    private final List<Message> messages = new ArrayList<>();
    private final JSONArray history = new JSONArray();
    private MessageAdapter adapter;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private boolean waiting = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ai_helper, container, false);

        rvMessages = view.findViewById(R.id.rvMessages);
        etMessage = view.findViewById(R.id.etMessage);
        ImageButton btnSend = view.findViewById(R.id.btnSend);
        ImageButton btnBack = view.findViewById(R.id.btnBack);

        adapter = new MessageAdapter(messages);
        rvMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMessages.setAdapter(adapter);

        addMessage("Hi! I'm your AI helper. Ask me to explain a topic, quiz you, "
                + "summarise notes or build a study plan.", Message.TYPE_AI);

        // Text sent from the home screen cards (e.g. "Quiz me on: ")
        Bundle args = getArguments();
        if (args != null) {
            String prompt = args.getString("prompt", "");
            if (!prompt.isEmpty()) {
                etMessage.setText(prompt);
                etMessage.setSelection(prompt.length());
                etMessage.requestFocus();
            }
        }

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        btnSend.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {
        if (waiting) return;

        String text = etMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        addMessage(text, Message.TYPE_USER);
        etMessage.setText("");
        history.put(makeMessage("user", text));

        waiting = true;
        addMessage("Thinking...", Message.TYPE_AI);
        final int thinkingIndex = messages.size() - 1;

        AiService.ask(history, new AiService.ReplyListener() {
            @Override
            public void onSuccess(String reply) {
                requireActivity().runOnUiThread(() -> {
                    history.put(makeMessage("assistant", reply));
                    showReply(thinkingIndex, reply);
                });
            }

            @Override
            public void onError(String message) {
                requireActivity().runOnUiThread(() -> {
                    history.remove(history.length() - 1); // keep history valid
                    showReply(thinkingIndex, "Sorry, something went wrong.\n" + message);
                });
            }
        });
    }

    private void showReply(int index, String text) {
        messages.get(index).setText(text);
        adapter.notifyItemChanged(index);
        rvMessages.scrollToPosition(messages.size() - 1);
        waiting = false;
    }

    private JSONObject makeMessage(String role, String content) {
        JSONObject o = new JSONObject();
        try {
            o.put("role", role);
            o.put("content", content);
        } catch (Exception ignored) { }
        return o;
    }

    private void addMessage(String text, int type) {
        messages.add(new Message(text, type));
        adapter.notifyItemInserted(messages.size() - 1);
        rvMessages.scrollToPosition(messages.size() - 1);
    }
}
