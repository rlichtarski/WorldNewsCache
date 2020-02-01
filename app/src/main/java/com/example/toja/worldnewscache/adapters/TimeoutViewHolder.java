package com.example.toja.worldnewscache.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toja.worldnewscache.R;

public class TimeoutViewHolder extends RecyclerView.ViewHolder {

    TextView networkTimeoutTextView;

    public TimeoutViewHolder(@NonNull View itemView) {
        super(itemView);

        networkTimeoutTextView = itemView.findViewById(R.id.network_timeout_text_view);
    }
}
