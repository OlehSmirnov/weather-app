package com.olegsmirnov.weatherapp.others;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olegsmirnov.weatherapp.R;
import com.olegsmirnov.weatherapp.data.WeatherContract;
import com.olegsmirnov.weatherapp.data.WeatherDbHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private OnItemClickListener onItemClickListener;
    private List<WeatherItem> mList = new ArrayList<>();

    public WeatherAdapter(List<WeatherItem> mList) {
        this.mList = new ArrayList<>(mList);
    }

    @Override
    public WeatherAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvDate.setText(mList.get(position).getDate());
        new DownloadImageTask(holder.ivIcon).execute("http://openweathermap.org/img/w/" + mList.get(position).getIconIdentifier() + ".png");;
        holder.tvTemperature.setText(mList.get(position).getTemperature()  + "°");
        holder.tvPressure.setText("pressure:" + mList.get(position).getPressure() + "hpa");
        holder.tvHumidity.setText("humidity:" + mList.get(position).getHumidity() + "%");
        holder.tvWindSpeed.setText(mList.get(position).getWindSpeed() + "m/s");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate;
        private ImageView ivIcon;
        private TextView tvTemperature;
        private TextView tvPressure;
        private TextView tvHumidity;
        private TextView tvWindSpeed;

        MyViewHolder(View view) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.RV_item_date);
            ivIcon = (ImageView) view.findViewById(R.id.RV_item_icon);
            tvTemperature = (TextView) view.findViewById(R.id.RV_item_temperature);
            tvPressure = (TextView) view.findViewById(R.id.RV_item_pressure);
            tvHumidity = (TextView) view.findViewById(R.id.RV_item_humidity);
            tvWindSpeed = (TextView) view.findViewById(R.id.RV_item_windSpeed);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            if (bmImage != null) {
                String urldisplay = urls[0];
                Bitmap mIcon = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mIcon;
            }
            else return null;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
