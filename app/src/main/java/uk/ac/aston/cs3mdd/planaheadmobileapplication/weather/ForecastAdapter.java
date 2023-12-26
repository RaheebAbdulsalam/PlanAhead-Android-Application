package uk.ac.aston.cs3mdd.planaheadmobileapplication.weather;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;

// An Adapter for populating a RecyclerView with weather forecast data.
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    // List to hold forecast data
    private List<WeatherResponse> forecastData;

    //Sets the forecast data for the adapter and notifies observers about the data change.
    public void setForecastData(List<WeatherResponse> forecastData) {
        this.forecastData = forecastData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        if (forecastData != null && position < forecastData.size()) {
            WeatherResponse forecastItem = forecastData.get(position);
            holder.bind(forecastItem);
        }
    }

    //Returns the total number of items in the data set
    @Override
    public int getItemCount() {
        if (forecastData != null) {
            return forecastData.size();
        } else {
            return 0;
        }
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView temperatureTextView;
        private TextView descriptionTextView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }

        public void bind(WeatherResponse forecastItem) {
            // Extract timestamp and format date
            long timestamp = forecastItem.getDt() * 1000L;
            String date = new SimpleDateFormat("EEE, MMM d HH:mm", Locale.getDefault()).format(new Date(timestamp));
            dateTextView.setText(date);

            // Display temperature and weather description
            temperatureTextView.setText(String.format(Locale.getDefault(), "%.0f °C", forecastItem.getMain().getTemp() - 273.15));
            descriptionTextView.setText(forecastItem.getWeather().get(0).getDescription());
        }
    }

}

