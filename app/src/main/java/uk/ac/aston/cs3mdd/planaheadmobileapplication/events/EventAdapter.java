package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;

// Adapter class for RecyclerView to display events
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Event> events;

    public EventAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.events_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Event currentEvent = events.get(position);

        // Bind the data to the ViewHolder
        holder.bind(currentEvent);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateEventActivity.class);
                intent.putExtra("id", currentEvent.getId());
                intent.putExtra("title", currentEvent.getTitle());
                intent.putExtra("date", currentEvent.getDate());
                intent.putExtra("time", currentEvent.getTime());
                intent.putExtra("address", currentEvent.getAddress());
                intent.putExtra("postcode", currentEvent.getPostcode());
                intent.putExtra("city", currentEvent.getCity());
                intent.putExtra("notes", currentEvent.getNotes());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    // Update to handle setting a list of events
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView event_title_txt, event_date_txt, event_time_txt, event_address_txt, event_postcode_txt, event_city_txt, event_notes_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            event_title_txt = itemView.findViewById(R.id.event_title_txt);
            event_date_txt = itemView.findViewById(R.id.event_date_txt);
            event_time_txt = itemView.findViewById(R.id.event_time_txt);
            event_address_txt = itemView.findViewById(R.id.event_address_txt);
            event_postcode_txt = itemView.findViewById(R.id.event_postcode_txt);
            event_city_txt = itemView.findViewById(R.id.event_city_txt);
            event_notes_txt = itemView.findViewById(R.id.event_notes_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }

        // Add a bind method to set data to the views
        public void bind(Event event) {
            event_title_txt.setText(String.valueOf(event.getTitle()));
            event_date_txt.setText("Date: " + String.valueOf(event.getDate()));
            event_time_txt.setText("Time: " + String.valueOf(event.getTime()));
            event_address_txt.setText("Address: " + String.valueOf(event.getAddress()));
            event_postcode_txt.setText("Postcode: " + String.valueOf(event.getPostcode()));
            event_city_txt.setText("City: " + String.valueOf(event.getCity()));
            event_notes_txt.setText("Additional Note: " + String.valueOf(event.getNotes()));
        }
    }
}
