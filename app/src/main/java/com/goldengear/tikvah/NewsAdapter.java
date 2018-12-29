package com.goldengear.tikvah;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Aaron Ayalew on 10/20/2018.
 */

public class NewsAdapter extends ArrayAdapter {
    String[] titles;
    String[] descriptions;
    String[] images;
    String[] IDs;
    String[] times;
    String[] contents;
    String []isExpandeds;
    Context ctx;
    Activity app;

    NewsAdapter(Context context,String[] ids, String[] tits, String[] desc, String[] imgs, String[] conts, String[] times, String[] isExps, Activity app) {
        super(context,R.layout.article,R.id.artTitle,tits);
        Log.d("Aaron", "NewsAdapter Initialized");
        this.titles = tits;
        this.descriptions = desc;
        this.images = imgs;
        this.IDs = ids;
        this.contents = conts;
        this.times = times;
        this.isExpandeds = isExps;
        this.ctx = context;
        this.app = app;
    }
    static class ViewHolder {
        TextView title;
        TextView desc;
        ImageView img;
        TextView time;
        FloatingActionButton expand;
        boolean isExpanded;

    }
    static class ViewHolderNoImg {
        TextView title;
        TextView desc;
        TextView time;
        FloatingActionButton expand;
        boolean isExpanded;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = app.getLayoutInflater();
        Log.d("Aaron", "GetView Called");
        if(!images[position].contains("no_image")) {
            if (convertView == null || convertView.getTag().getClass() == ViewHolderNoImg.class) {
                convertView = inflater.inflate(R.layout.article, parent, false);
                final ViewHolder holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.artTitle);
                holder.desc = (TextView) convertView.findViewById(R.id.artDesc);
                holder.img = (ImageView) convertView.findViewById(R.id.artImg);
                holder.time = (TextView) convertView.findViewById(R.id.artTime);
                holder.expand = (FloatingActionButton) convertView.findViewById(R.id.btnExpand);
                if(isExpandeds[position].contains("false")) {
                    holder.isExpanded = false;
                } else {
                    holder.isExpanded = true;
                }
                convertView.setTag(holder);
                Log.d("Logger", "convertView is null");
            }
            final ViewHolder holder = (ViewHolder) convertView.getTag();
            if (isExpandeds[position].contains("true")) {
                holder.desc.setMaxLines(100);
                Thread runn = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        holder.expand.setImageResource(R.drawable.ic_collapse);
                    }
                });
                runn.run();
            } else {
                holder.desc.setMaxLines(3);
                Thread runn = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        holder.expand.setImageResource(R.drawable.ic_expand);
                    }
                });
                runn.run();
            }
            holder.title.setText(titles[position]);
            holder.desc.setText(descriptions[position]);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int year = Integer.valueOf(times[position].substring(0, 4));
                    Log.d("Year", String.valueOf(year));
                    int month = Integer.valueOf(times[position].substring(5, 7));
                    Log.d("Month", String.valueOf(month));
                    int day = Integer.valueOf(times[position].substring(8, 10));
                    Log.d("Day", String.valueOf(day));
                    int hour = Integer.valueOf(times[position].substring(11, 13));
                    Log.d("Hour", String.valueOf(hour));
                    int min = Integer.valueOf(times[position].substring(14, 16));
                    Log.d("Min", String.valueOf(min));
                    int sec = Integer.valueOf(times[position].substring(17, 19));
                    Date date = new Date(year - 1900, month - 1, day, hour, min, sec);
                    Date today = Calendar.getInstance().getTime();
                    String tod;
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    tod = formatter.format(today);
                    Log.d("Today", "Today is: " + tod);
                    Log.d("Today", "Date is:" + formatter.format(date));
                    if (date.getYear() == today.getYear() && date.getMonth() == today.getMonth() && today.getDay() == date.getDay()) {
                        Log.d("Date", "it is today");
                        String minStr;
                        if(String.valueOf(min).length() == 1) {
                            minStr = "0" + min;
                        } else {
                            minStr = String.valueOf(min);
                        }
                        String hourstr = "";
                        String timestr = "";
                        String amorpm = "";
                        if (hour > 12) {
                            amorpm = "PM";
                            hourstr = String.valueOf(hour - 12);
                        } else if(hour == 12) {
                            amorpm = "PM";
                            hourstr = String.valueOf(hour);
                        } else if(hour == 0) {
                            amorpm = "AM";
                            hourstr = "12";
                        } else if(hour < 12) {
                            amorpm = "AM";
                            hourstr = String.valueOf(hour);
                        }
                        timestr = hourstr + ":" + minStr + " " + amorpm;
                        holder.time.setText(timestr);
                    } else {
                        String monthstr = "";
                        switch (month) {
                            case 1:
                                monthstr = "January";
                                break;
                            case 2:
                                monthstr = "February";
                                break;
                            case 3:
                                monthstr = "March";
                                break;
                            case 4:
                                monthstr = "April";
                                break;
                            case 5:
                                monthstr = "May";
                                break;
                            case 6:
                                monthstr = "June";
                                break;
                            case 7:
                                monthstr = "July";
                                break;
                            case 8:
                                monthstr = "August";
                                break;
                            case 9:
                                monthstr = "September";
                                break;
                            case 10:
                                monthstr = "October";
                                break;
                            case 11:
                                monthstr = "November";
                                break;
                            case 12:
                                monthstr = "December";
                                break;

                        }
                        holder.time.setText(monthstr + " " + day + "," + " " + year);

                    }
                    Log.d("Day", String.valueOf(day));

                }
            });

            thread.run();
            if (!images[position].contains("no_image")) {
                File cachDir = ctx.getCacheDir();
                if (new File(cachDir.getAbsolutePath() + "/" + images[position]).exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(cachDir.getAbsolutePath() + "/" + images[position]);
                    holder.img.setImageBitmap(bm);
                } else {
                    DWImage downloader = new DWImage(holder.img);
                    downloader.execute(new TikConst().getURL() + "/img/" + images[position], images[position]);
                }
            } else {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        holder.img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.sucks));
                    }
                });
                thread1.run();
            }
            holder.expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpandeds[position].contains("false")) {
                        holder.desc.setMaxLines(100);
                        holder.isExpanded = true;
                        isExpandeds[position] = "true";
                        Thread changer = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                holder.expand.setImageResource(R.drawable.ic_collapse);
                            }
                        });
                        changer.run();
                    } else {
                        holder.desc.setMaxLines(3);
                        holder.isExpanded = false;
                        isExpandeds[position] = "false";
                        Thread changer = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                holder.expand.setImageResource(R.drawable.ic_collapse);
                            }
                        });
                        changer.run();
                    }
                }
            });

            Log.d("Aaron", "Finished get View returning row");
        } else {
            if (convertView == null || convertView.getTag().getClass() == ViewHolder.class) {
                convertView = inflater.inflate(R.layout.arttice_no_image, parent, false);
                final ViewHolderNoImg holder = new ViewHolderNoImg();
                holder.title = (TextView) convertView.findViewById(R.id.artTitleNoImg);
                holder.desc = (TextView) convertView.findViewById(R.id.artDescNoImg);
                holder.time = (TextView) convertView.findViewById(R.id.artTimeNoImg);
                holder.expand = (FloatingActionButton) convertView.findViewById(R.id.btnExpandNoImg);
                holder.isExpanded = false;
                convertView.setTag(holder);
                Log.d("Logger", "convertView is null");
            }
            final ViewHolderNoImg holder = (ViewHolderNoImg) convertView.getTag();
            if (isExpandeds[position].contains("true")) {
                holder.desc.setMaxLines(100);
                Thread runn = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        holder.expand.setImageResource(R.drawable.ic_collapse);
                    }
                });
                runn.run();
            } else {
                holder.desc.setMaxLines(6);
                Thread runn = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        holder.expand.setImageResource(R.drawable.ic_expand);
                    }
                });
                runn.run();
            }
            holder.title.setText(titles[position]);
            holder.desc.setText(descriptions[position]);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int year = Integer.valueOf(times[position].substring(0, 4));
                        Log.d("Year", String.valueOf(year));
                        int month = Integer.valueOf(times[position].substring(5, 7));
                        Log.d("Month", String.valueOf(month));
                        int day = Integer.valueOf(times[position].substring(8, 10));
                        Log.d("Day", String.valueOf(day));
                        int hour = Integer.valueOf(times[position].substring(11, 13));
                        Log.d("Hour", String.valueOf(hour));
                        int min = Integer.valueOf(times[position].substring(14, 16));
                        Log.d("Min", String.valueOf(min));
                        int sec = Integer.valueOf(times[position].substring(17, 19));
                        Date date = new Date(year - 1900, month - 1, day, hour, min, sec);
                        Date today = Calendar.getInstance().getTime();
                        String tod;
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        tod = formatter.format(today);
                        Log.d("Today", "Today is: " + tod);
                        Log.d("Today", "Date is:" + formatter.format(date));
                        if (date.getYear() == today.getYear() && date.getMonth() == today.getMonth() && today.getDay() == date.getDay()) {
                            Log.d("Date", "it is today");
                            String minStr;
                            if (String.valueOf(min).length() == 1) {
                                minStr = "0" + min;
                            } else {
                                minStr = String.valueOf(min);
                            }
                            String hourstr = "";
                            String timestr = "";
                            String amorpm = "";
                            if (hour > 12) {
                                amorpm = "PM";
                                hourstr = String.valueOf(hour - 12);
                            } else if (hour == 12) {
                                amorpm = "PM";
                                hourstr = String.valueOf(hour);
                            } else if (hour == 0) {
                                amorpm = "AM";
                                hourstr = "12";
                            } else if (hour < 12) {
                                amorpm = "AM";
                                hourstr = String.valueOf(hour);
                            }
                            timestr = hourstr + ":" + minStr + " " + amorpm;
                            holder.time.setText(timestr);
                        } else {
                            String monthstr = "";
                            switch (month) {
                                case 1:
                                    monthstr = "January";
                                    break;
                                case 2:
                                    monthstr = "February";
                                    break;
                                case 3:
                                    monthstr = "March";
                                    break;
                                case 4:
                                    monthstr = "April";
                                    break;
                                case 5:
                                    monthstr = "May";
                                    break;
                                case 6:
                                    monthstr = "June";
                                    break;
                                case 7:
                                    monthstr = "July";
                                    break;
                                case 8:
                                    monthstr = "August";
                                    break;
                                case 9:
                                    monthstr = "September";
                                    break;
                                case 10:
                                    monthstr = "October";
                                    break;
                                case 11:
                                    monthstr = "November";
                                    break;
                                case 12:
                                    monthstr = "Dec";
                                    break;

                            }
                            holder.time.setText(monthstr + " " + day + "," + " " + year);
                        }
                    } catch(Exception ex) {

                    }


                }
            });
            thread.run();
            holder.expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpandeds[position].contains("false")) {
                        holder.desc.setMaxLines(100);
                        holder.isExpanded = true;
                        isExpandeds[position] = "true";
                        Thread changer = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                holder.expand.setImageResource(R.drawable.ic_collapse);
                            }
                        });
                        changer.run();
                    } else {
                        holder.desc.setMaxLines(3);
                        holder.isExpanded = false;
                        isExpandeds[position] = "false";
                        Thread changer = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                holder.expand.setImageResource(R.drawable.ic_collapse);
                            }
                        });
                        changer.run();
                    }
                }
            });
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

