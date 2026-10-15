package com.example.itmda3_group_project.study_rooms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.R;

import java.util.ArrayList;
import java.util.List;

public class StudyRoomsAdapter extends RecyclerView.Adapter<StudyRoomsAdapter.ViewHolder> {
    private List<StudySession> sessions = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(StudySession session);
    }

    public StudyRoomsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSessions(List<StudySession> sessions) {
        this.sessions = sessions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.study_rooms_items, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudySession session = sessions.get(position);
        holder.bind(session, listener);
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLocation, tvDate, tvTime, tvDescription;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.room_title);
            tvLocation = itemView.findViewById(R.id.room_location);
            tvDate = itemView.findViewById(R.id.room_date);
            tvTime = itemView.findViewById(R.id.room_time);
            tvDescription = itemView.findViewById(R.id.room_description);
        }

        void bind(StudySession session, OnItemClickListener listener) {
            tvTitle.setText(session.getTitle());
            tvLocation.setText(session.getLocation());
            tvDate.setText(session.getDate());
            tvTime.setText(session.getTime());
            tvDescription.setText(session.getDescription());

            itemView.setOnClickListener(v -> listener.onItemClick(session));
        }
    }
}
