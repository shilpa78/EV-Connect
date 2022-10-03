package com.example.auth_app.ui.stations;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auth_app.R;
import com.google.android.gms.maps.model.Circle;

import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {
    private StationFragment stationFragment;
    private List<StationListItem> stationListItems;
    private ItemClickListener clickListener;
    public String url;

    public StationAdapter(List<StationListItem> stationListItems,StationFragment stationFragment, ItemClickListener clickListener) {
        this.stationListItems = stationListItems;
        this.clickListener = clickListener;
        this.stationFragment = stationFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.stn_name.setText(String.valueOf(stationListItems.get(position).getStation_name()));
        holder.stn_rating.setText("Rating - "+String.valueOf(stationListItems.get(position).getRatings()));
        //holder.stn_price.setText("Price - " + String.valueOf(stationListItems.get(position).getPrice()));
        holder.stn_distance.setText(String.valueOf(stationListItems.get(position).getDistance()) + " Km away");
        holder.stn_status.setText(String.valueOf(stationListItems.get(position).getStatus()));
//        InputStream inputStream = (InputStream)(stationListItems.get(position).getUrl()).;
//        Drawable drawable = Drawable.createFromStream(inputStream, null);
        holder.stn_image.setImageResource(stationListItems.get(position).getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("","In bindViewHolder ######");
                clickListener.onItemClick(stationListItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationListItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView stn_name, stn_price, stn_rating, stn_distance, stn_status;
        public CircleImageView stn_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stn_name = itemView.findViewById(R.id.station_name);
            //stn_price = itemView.findViewById(R.id.station_price);
            stn_rating = itemView.findViewById(R.id.station_rating);
            stn_distance = itemView.findViewById(R.id.station_distance);
            stn_status = itemView.findViewById(R.id.station_status);
            stn_image = itemView.findViewById(R.id.station_image);
        }
    }

    public interface ItemClickListener {
        public void onItemClick(StationListItem stationListItem);
    }
}
