package com.goldengear.tikvah;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class ArticleImageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private String[] images;
    public ArticleImageAdapter(Context ctx, String[] images) {
        this.context = ctx;
        this.images = images;
        Log.d("NewsGetter", "Adapter Initialized!");
    }
    public int getCount() {
        return images.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Log.d("NewsGetter", "Instantiate Item called!");
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.article_image,null,true);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imgArticle);
        ImageLoader loader = FeedGetter.getInstance(context.getApplicationContext()).getImageLoader();
        loader.get(images[position], new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Log.d("NewsGetter","ImageGot, setting imageview");
                imageView.setImageBitmap(response.getBitmap());
                try {
                    imageView.setMaxHeight(response.getBitmap().getHeight());
                } catch (NullPointerException ex){

                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.drawable.noimagearticle);
            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

}
