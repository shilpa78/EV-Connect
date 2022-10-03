package com.example.auth_app.ui.shops;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auth_app.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    private ShopFragment shopFragment;
    private List<ShopListItem> shopListItems;
    private ItemClickListener clickListener;

    public ShopAdapter(List<ShopListItem> shopListItems,ShopFragment shopFragment, ItemClickListener clickListener) {
        this.shopListItems = shopListItems;
        this.clickListener = clickListener;
        this.shopFragment = shopFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_list_item, parent, false);
        return new ViewHolder(v);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.shop_name.setText(String.valueOf(shopListItems.get(position).getShop_name()));
        holder.shop_rating.setText("Rating - "+String.valueOf(shopListItems.get(position).getRatings()));
        //holder.stn_price.setText("Price - " + String.valueOf(stationListItems.get(position).getPrice()));
        holder.shop_distance.setText(String.valueOf(shopListItems.get(position).getDistance()) + " Km away");
        holder.shop_status.setText(String.valueOf(shopListItems.get(position).getStatus()));
//        holder.shop_img.setImageBitmap(getBitmapFromURL(ShopFragment.url));
//        Picasso.get().load(ShopFragment.url).into(holder.stn_img);
        holder.shop_img.setImageResource(shopListItems.get(position).getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("","In bindViewHolder ######");
                clickListener.onItemClick(shopListItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopListItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView shop_name, shop_price, shop_rating, shop_distance, shop_status;
        public String url;
        public CircleImageView shop_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shop_name = itemView.findViewById(R.id.shop_name);
            //stn_price = itemView.findViewById(R.id.shop_price);
            shop_rating = itemView.findViewById(R.id.shop_rating);
            shop_distance = itemView.findViewById(R.id.shop_distance);
            shop_status = itemView.findViewById(R.id.shop_status);
            shop_img = itemView.findViewById(R.id.shop_image);
        }
    }

    public interface ItemClickListener {
        public void onItemClick(ShopListItem shopListItem);
    }
}
