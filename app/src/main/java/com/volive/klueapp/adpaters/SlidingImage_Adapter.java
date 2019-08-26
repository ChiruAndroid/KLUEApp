package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.models.Home_banner_model;
import com.volive.klueapp.R;

import java.util.ArrayList;


public class SlidingImage_Adapter extends PagerAdapter {


    ViewPager viewPager;
    private ArrayList<Home_banner_model> IMAGES;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImage_Adapter(Context context, ArrayList<Home_banner_model> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.banner_slide_view, view, false);

        final ImageView image = (ImageView) imageLayout.findViewById(R.id.banner_img);
        if (IMAGES.get(position) != null) {
            Glide.with(context)
                    .load(IMAGES.get(position).getBanner_image())
                    .into(image);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(IMAGES.get(position).getBanner_link()));
                Intent browserChooserIntent = Intent.createChooser(browserIntent, context.getString(R.string.choose_browser));
                context.startActivity(browserChooserIntent);

            }
        });

        viewPager = (ViewPager) view;
        viewPager.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}