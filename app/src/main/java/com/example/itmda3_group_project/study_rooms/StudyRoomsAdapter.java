package com.example.itmda3_group_project.study_rooms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Filter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.R;

import java.util.ArrayList;
import java.util.List;

public class StudyRoomsAdapter extends RecyclerView.Adapter<StudyRoomsAdapter.ViewHolder> {
    private List<StudySession> sessions;
    private List<StudySession> sessionFull;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(StudySession session);
    }

    public StudyRoomsAdapter(OnItemClickListener listener) {
        this.listener = listener;
        this.sessions = new ArrayList<>();
        this.sessionFull = new ArrayList<>();
    }

    public void setSessions(List<StudySession> sessions) {
        this.sessions = sessions;
        this.sessionFull = new ArrayList<>(sessions);
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

    //Searchview logic
    public Filter getFilter() {
        return studyRoomFilter;
    }

    private final Filter studyRoomFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<StudySession> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(sessionFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (StudySession session : sessionFull) {
                    if (session.getTitle().toLowerCase().contains(filterPattern) ||
                            session.getLocation().toLowerCase().contains(filterPattern)) {
                        filteredList.add(session);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sessions.clear();
            sessions.addAll((List<StudySession>) results.values);
            notifyDataSetChanged();
        }
    };

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
