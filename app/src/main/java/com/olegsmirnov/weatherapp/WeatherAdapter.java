package com.olegsmirnov.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private List<WeatherItem> mList;

    WeatherAdapter(List<WeatherItem> mList) {
        this.mList = new ArrayList<>(mList);
    }

    @Override
    public WeatherAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.date.setText(mList.get(position).getDate());
        new DownloadImageTask(holder.icon).execute("http://openweathermap.org/img/w/" + mList.get(position).getIconIdentifier() + ".png");
        String Sign =  "";
        if (mList.get(position).getTemperature() > 0) Sign = "+";
        holder.temperature.setText(Sign + mList.get(position).getTemperature()  + "°C");
        holder.pressure.setText("pressure:" + mList.get(position).getPressure() + "hpa");
        holder.humidity.setText("humidity:" + mList.get(position).getHumidity() + "%");
        holder.windSpeed.setText(mList.get(position).getWindSpeed() + "m/s");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private ImageView icon;
        private TextView temperature;
        private TextView pressure;
        private TextView humidity;
        private TextView windSpeed;

        MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.RV_item_date);
            icon = (ImageView) view.findViewById(R.id.RV_item_icon);
            temperature = (TextView) view.findViewById(R.id.RV_item_temperature);
            pressure = (TextView) view.findViewById(R.id.RV_item_pressure);
            humidity = (TextView) view.findViewById(R.id.RV_item_humidity);
            windSpeed = (TextView) view.findViewById(R.id.RV_item_windSpeed);
        }
    }
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
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

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
