package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Aaron Ayalew on 11/27/2018.
 */

public class MemeAdapter extends ArrayAdapter {
    String[] ids;
    String[] images;
    String[] dates;
    String[] provider_ids;
    String[] provider_names;
    String[] provider_images;
    String[] texts;
    Context ctx;
    Activity app;

    MemeAdapter(Context context,String[] ids, String[] images, String[] dates, String[] provider_ids, String[] provider_names, String[] provider_images, String[] texts, Activity app) {
        super(context,R.layout.meme_view,R.id.txtText,texts);
        Log.d("Aaron", "NewsAdapter Initialized");
        this.ids = ids;
        this.images = images;
        this.dates = dates;
        this.provider_ids = provider_ids;
        this.provider_names = provider_names;
        this.provider_images = provider_images;
        this.texts = texts;
        this.ctx = context;
        this.app = app;
    }
    static class ViewHolder {
        ImageView logo;
        TextView prov_name;
        ImageView meme;
        TextView time;
        TextView desc;
        TextView belTitle;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = app.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.meme_view, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.logo = (ImageView) convertView.findViewById(R.id.profPic);
            holder.prov_name = (TextView) convertView.findViewById(R.id.txtProvTit);
            holder.meme = (ImageView) convertView.findViewById(R.id.imgMeme);
            holder.time = (TextView) convertView.findViewById(R.id.txtMemeTime);
            holder.desc = (TextView) convertView.findViewById(R.id.txtText);
            holder.belTitle = (TextView) convertView.findViewById(R.id.txtBelowTitle);
            convertView.setTag(holder);
            Log.d("Logger", "convertView is null");
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.prov_name.setText(provider_names[position]);
        holder.desc.setText(texts[position]);
        holder.time.setText(dates[position]);
        holder.belTitle.setText(provider_names[position] + ": ");
        /*DWImage downloader1 = new DWImage(holder.logo);
        File cachDir = ctx.getCacheDir();
        if (new File(cachDir.getAbsolutePath() + "/" + provider_images[position]).exists()) {
            Bitmap bm = BitmapFactory.decodeFile(cachDir.getAbsolutePath() + "/" + provider_images[position]);
            holder.logo.setImageBitmap(bm);
        } else {
            downloader1.execute(new TikConst().getURL() + "img/prov/" + provider_images[position], provider_images[position]);
        }
        if (new File(cachDir.getAbsolutePath() + "/" + images[position]).exists()) {
            Bitmap bm = BitmapFactory.decodeFile(cachDir.getAbsolutePath() + "/" + images[position]);
            holder.meme.setImageBitmap(bm);
        } else {
            DWImage downloader2 = new DWImage(holder.meme);
            downloader2.execute(new TikConst().getURL() + "img/memes/" + images[position], images[position]);

        }*/
        Picasso.with(app).load(new TikConst().getURL() + "img/memes/" + images[position]).into(holder.meme);
        Picasso.with(app).load(new TikConst().getURL() + "img/prov/" + provider_images[position]).into(holder.logo);
        return convertView;
    }

    /*private class DWImage extends AsyncTask<String,Void,Bitmap> {
        ImageView bmimage;
        public DWImage(ImageView imgv) {
            this.bmimage = imgv;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            String urldisplay = strings[0];
            String imgName = strings[1];
            Bitmap bmp = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
                File file = new File(ctx.getCacheDir().getAbsolutePath() + "/" + imgName );
                FileOutputStream stream = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG,100,stream);
                stream.flush();
                stream.close();
            } catch (Exception ex) {
                Log.d("Error",ex.toString());
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bmimage.setImageBitmap(bitmap);
        }
    }*/
}
