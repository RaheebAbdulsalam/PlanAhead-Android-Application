package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;

public class EventAdapter extends ListAdapter<Event, EventAdapter.EventViewHolder> {
    private final EventClickCallback eventClickCallback;

    // An interface to define the onItemClick event listener
    public interface EventClickCallback {
        void onItemClick(Event event);
    }

    // Constructor for the EventAdapter, taking a DiffUtil.ItemCallback and an EventClickCallback
    public EventAdapter(@NonNull DiffUtil.ItemCallback<Event> diffCallback, EventClickCallback eventClickCallback) {
        super(diffCallback);
        this.eventClickCallback = eventClickCallback;
    }

    // onCreateViewHolder method inflates the layout for individual items in the RecyclerView
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return EventViewHolder.create(parent);
    }

    // onBindViewHolder method binds the data to the ViewHolder and sets the click listener for the card
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = getItem(position);
        holder.bind(currentEvent);

        // Set click listener for the card
        holder.itemView.setOnClickListener(view -> {
            if (eventClickCallback != null) {
                eventClickCallback.onItemClick(currentEvent);
            }
        });
    }

    // EventViewHolder class represents individual items in the RecyclerView
    static class EventViewHolder extends RecyclerView.ViewHolder {

        private final TextView event_title_txt, event_date_txt, event_time_txt, event_address_txt, event_postcode_txt, event_city_txt, event_notes_txt;

        // Constructor for EventViewHolder, initializes UI elements
        private EventViewHolder(@NonNull View itemView) {
            super(itemView);
            event_title_txt = itemView.findViewById(R.id.event_title_txt);
            event_date_txt = itemView.findViewById(R.id.event_date_txt);
            event_time_txt = itemView.findViewById(R.id.event_time_txt);
            event_address_txt = itemView.findViewById(R.id.event_address_txt);
            event_postcode_txt = itemView.findViewById(R.id.event_postcode_txt);
            event_city_txt = itemView.findViewById(R.id.event_city_txt);
            event_notes_txt = itemView.findViewById(R.id.event_notes_txt);
        }

        // Method to bind data to the UI elements of the ViewHolder
        private void bind(Event event) {
            event_title_txt.setText(event.getTitle());
            event_date_txt.setText("Date: " + event.getDate());
            event_time_txt.setText("Time: " + event.getTime());
            event_address_txt.setText("Address: " + event.getAddress());
            event_postcode_txt.setText(event.getPostcode());
            event_city_txt.setText(event.getCity()+ ", ");
            event_notes_txt.setText("Note: " +event.getNotes());
        }

        // Static method to create an instance of EventViewHolder
        private static EventViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_item, parent, false);
            return new EventViewHolder(view);
        }
    }

    // EventDiff class defines how to check for item identity and content equality for the DiffUtil
    public static class EventDiff extends DiffUtil.ItemCallback<Event> {

        // Method to check if two items have the same identity
        @Override
        public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getId() == newItem.getId(); // Assuming Event class has getId method
        }

        // Method to check if two items have the same content
        @Override
        public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}
