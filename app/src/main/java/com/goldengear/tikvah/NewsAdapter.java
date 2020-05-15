package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Aaron Ayalew on 10/20/2018.
 */

public class NewsAdapter extends ArrayAdapter {
    List<Article> articles;
    Context ctx;
    Activity app;

    NewsAdapter(Context context, List<Article> articles,Activity app, String[] titles) {
        super(context,R.layout.article,R.id.artTitle,titles);
        Log.d("Aaron", "NewsAdapter Initialized");
        this.articles = articles;
        this.ctx = context;
        this.app = app;
    }
    static class ViewHolder {
        TextView title;
        TextView desc;
        ViewPager imageviewers;
        TextView time;
        ImageButton expand;
        boolean isExpanded;

    }
    static class ViewHolderNoImg {
        TextView title;
        TextView desc;
        TextView time;
        ImageButton expand;
        boolean isExpanded;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = app.getLayoutInflater();
        Log.d("Aaron", "GetView Called");
        final Article currArticle = articles.get(position);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.article,parent,false);
            final ViewHolder holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.artTitle);
            holder.desc = (TextView) convertView.findViewById(R.id.artDesc);
            holder.time = (TextView) convertView.findViewById(R.id.artTime);
            holder.expand = (ImageButton) convertView.findViewById(R.id.btnExpand);
            holder.imageviewers = (ViewPager) convertView.findViewById(R.id.vpArticleImages);
            if(currArticle.getExpanded()) {
                holder.isExpanded = true;
            } else {
                holder.isExpanded = false;
            }
            convertView.setTag(holder);
            Log.d("Logger", "convertView is null");

        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        if (currArticle.getExpanded()) {
            holder.desc.setVisibility(View.VISIBLE);
            holder.expand.setImageResource(R.drawable.collapse);
        } else {
           holder.desc.setVisibility(View.GONE);
           holder.expand.setImageResource(R.drawable.expand);
        }
        holder.title.setText(currArticle.getTitle());
        holder.desc.setText(currArticle.getContent());
        //TODO:Set Date and Time here
        holder.expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!currArticle.getExpanded()) {
                        holder.desc.setVisibility(View.VISIBLE);
                        holder.isExpanded = true;
                        currArticle.setExpanded(true);
                        holder.expand.setImageResource(R.drawable.collapse);
                    } else {
                        holder.desc.setVisibility(View.GONE);
                        holder.isExpanded = false;
                        currArticle.setExpanded(false);
                        holder.expand.setImageResource(R.drawable.expand);
                    }
                }
            });
        Log.d("Aaron", "Finished get View returning row");
        if(currArticle.getPictures() != null){
            ArticleImageAdapter adapter = new ArticleImageAdapter(ctx, currArticle.getPictures());
            holder.imageviewers.setAdapter(adapter);
            holder.imageviewers.setVisibility(View.VISIBLE);
        } else {
            holder.imageviewers.setVisibility(View.GONE);
        }

        return convertView;

    }
    private class DWImage extends AsyncTask<String,Void,Bitmap> {
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
    }
    private class PageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }
}

