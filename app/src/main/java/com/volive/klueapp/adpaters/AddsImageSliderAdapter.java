package com.volive.klueapp.adpaters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.models.Ads_Model;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class AddsImageSliderAdapter extends PagerAdapter {

    boolean isInfinite;
    private ArrayList<Ads_Model> IMAGES;
    private LayoutInflater inflater;
    private Context context;

    public AddsImageSliderAdapter(Context context, ArrayList<Ads_Model> IMAGES, boolean isInfinite) {
        this.context = context;
        this.IMAGES = IMAGES;
        this.isInfinite = isInfinite;
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
        View imageLayout = inflater.inflate(R.layout.ads_slide_view, view, false);

        ImageView image = (ImageView) imageLayout.findViewById(R.id.ads_img);

        Glide.with(context)
                .load(IMAGES.get(position).getImage())
                .into(image);
        ViewPager viewPager = (ViewPager) view;
        viewPager.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}

