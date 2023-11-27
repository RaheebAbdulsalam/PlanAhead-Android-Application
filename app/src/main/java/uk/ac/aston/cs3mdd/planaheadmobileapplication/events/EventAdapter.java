package uk.ac.aston.cs3mdd.planaheadmobileapplication.events;

import android.annotation.SuppressLint;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    Context context;
    ArrayList event_id, event_title, event_date, event_time, event_address,event_postcode,event_city, event_notes;

    public EventAdapter(Context context, ArrayList event_id, ArrayList event_title, ArrayList event_date, ArrayList event_time, ArrayList event_address,ArrayList event_postcode,ArrayList event_city, ArrayList event_notes) {
        this.context = context;
        this.event_id = event_id;
        this.event_title = event_title;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_address = event_address;
        this.event_postcode = event_postcode;
        this.event_city = event_city;
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//        holder.event_id_txt.setText(String.valueOf(event_id.get(position)));
        holder.event_title_txt.setText(String.valueOf(event_title.get(position)));
        holder.event_date_txt.setText(String.valueOf(event_date.get(position)));
        holder.event_time_txt.setText(String.valueOf(event_time.get(position)));
        holder.event_address_txt.setText(String.valueOf(event_address.get(position)));
        holder.event_postcode_txt.setText(String.valueOf(event_postcode.get(position)));
        holder.event_city_txt.setText(String.valueOf(event_city.get(position)));
        holder.event_notes_txt.setText(String.valueOf(event_notes.get(position)));

        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateEventActivity.class);
                intent.putExtra("id",String.valueOf(event_id.get(position)));
                intent.putExtra("title",String.valueOf(event_title.get(position)));
                intent.putExtra("date",String.valueOf(event_date.get(position)));
                intent.putExtra("time",String.valueOf(event_time.get(position)));
                intent.putExtra("address",String.valueOf(event_address.get(position)));
                intent.putExtra("postcode",String.valueOf(event_postcode.get(position)));
                intent.putExtra("city",String.valueOf(event_city.get(position)));
                intent.putExtra("notes",String.valueOf(event_notes.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return event_id.size();
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
    }

}