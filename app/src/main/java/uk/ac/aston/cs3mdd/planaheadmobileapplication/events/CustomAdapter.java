package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList event_id, event_title, event_date, event_time, event_location, event_notes;

    public CustomAdapter(Context context, ArrayList event_id, ArrayList event_title, ArrayList event_date, ArrayList event_time, ArrayList event_location, ArrayList event_notes) {
        this.context = context;
        this.event_id = event_id;
        this.event_title = event_title;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_location = event_location;
        this.event_notes = event_notes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.events_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.event_id_txt.setText(String.valueOf(event_id.get(position)));
        holder.event_title_txt.setText(String.valueOf(event_title.get(position)));
        holder.event_date_txt.setText(String.valueOf(event_date.get(position)));
        holder.event_time_txt.setText(String.valueOf(event_time.get(position)));
        holder.event_location_txt.setText(String.valueOf(event_location.get(position)));
        holder.event_notes_txt.setText(String.valueOf(event_notes.get(position)));
    }

    @Override
    public int getItemCount() {
        return event_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView event_id_txt, event_title_txt, event_date_txt, event_time_txt, event_location_txt, event_notes_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            event_id_txt = itemView.findViewById(R.id.event_id_txt);
            event_title_txt = itemView.findViewById(R.id.event_title_txt);
            event_date_txt = itemView.findViewById(R.id.event_date_txt);
            event_time_txt = itemView.findViewById(R.id.event_time_txt);
            event_location_txt = itemView.findViewById(R.id.event_location_txt);
            event_notes_txt = itemView.findViewById(R.id.event_notes_txt);
        }
    }
}