package uk.ac.aston.cs3mdd.planaheadmobileapplication.places;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.ac.aston.cs3mdd.planaheadmobileapplication.R;


public class PlaceListAdapter extends
        RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    private List<MyPlace> mPlaceList;
    private final LayoutInflater mInflater;

    public PlaceListAdapter(Context context,
                            List<MyPlace> placeList) {
        mInflater = LayoutInflater.from(context);
        this.mPlaceList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.place_item,
                parent, false);
        return new PlaceViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        MyPlace myPlace = mPlaceList.get(position);
        holder.place = myPlace;
        String name = myPlace.getName();
        holder.placeNameView.setText(name);
        String icon = myPlace.getIcon();
        Picasso.get().load(icon).into(holder.placeIcon);
    }

    @Override
    public int getItemCount() {
        return this.mPlaceList.size();
    }

    public void updateData(List<MyPlace> list) {
        this.mPlaceList = list;
        notifyDataSetChanged();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        public final TextView placeNameView;
        public final ImageView placeIcon;
        final PlaceListAdapter mAdapter;
        public MyPlace place;

        public PlaceViewHolder(@NonNull View itemView, PlaceListAdapter adapter) {
            super(itemView);
            placeNameView = itemView.findViewById(R.id.placename);
            this.placeIcon = itemView.findViewById(R.id.placeicon);
            this.mAdapter = adapter;
        }
    }
}

