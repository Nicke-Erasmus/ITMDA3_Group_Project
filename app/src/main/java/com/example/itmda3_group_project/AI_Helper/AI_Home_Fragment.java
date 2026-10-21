package com.example.itmda3_group_project.AI_Helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.itmda3_group_project.R;

public class AI_Home_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_ai_home, container, false);

        LinearLayout containerLayout = view.findViewById(R.id.toolsContainer);

        addTool(containerLayout, "💡", R.drawable.bg_tile_green,
                "Explain a concept", "Simple, step-by-step explanations",
                "Explain this concept to me: ");
        addTool(containerLayout, "📝", R.drawable.bg_tile_pink,
                "Summarise my notes", "Turn long notes into key points",
                "Summarise these notes: ");
        addTool(containerLayout, "❓", R.drawable.bg_tile_green,
                "Quiz me", "Practice questions on any topic",
                "Quiz me on: ");

        // Study plan opens the calendar instead of the chat
        View planCard = addTool(containerLayout, "📅", R.drawable.bg_tile_pink,
                "Study plan", "Plan your revision on a calendar", "");
        planCard.setOnClickListener(v -> {
            Fragment fragment = new StudyPlanFragment();

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_Container, fragment) // your FrameLayout ID in the host activity
                    .addToBackStack(null) // so the user can press back to return
                    .commit();
        });

        view.findViewById(R.id.cardAsk).setOnClickListener(v -> openChat(""));
        view.findViewById(R.id.btnNewChat).setOnClickListener(v -> openChat(""));

        return view;
    }

    private View addTool(LinearLayout container, String emoji, int tileBg,
                         String title, String subtitle, String prompt) {
        View card = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_tool_card, container, false);

        TextView tvEmoji = card.findViewById(R.id.tvEmoji);
        tvEmoji.setText(emoji);
        tvEmoji.setBackgroundResource(tileBg);

        ((TextView) card.findViewById(R.id.tvTitle)).setText(title);
        ((TextView) card.findViewById(R.id.tvSubtitle)).setText(subtitle);

        card.setOnClickListener(v -> openChat(prompt));
        container.addView(card);
        return card;
    }

    private void openChat(String prompt) {
        Fragment fragment = new AI_Helper_Fragment();

        // Pass arguments to the fragment
        Bundle args = new Bundle();
        args.putString("prompt", prompt);
        fragment.setArguments(args);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_Container, fragment)
                .addToBackStack(null)
                .commit();
    }
}

