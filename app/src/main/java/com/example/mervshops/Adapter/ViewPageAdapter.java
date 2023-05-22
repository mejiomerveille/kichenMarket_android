package com.example.mervshops.Adapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.namespace.R;
public class ViewPageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    public ViewPageAdapter(Context context){
        this.context=context;
    }
    private int images[]={
            R.drawable.capture,
            R.drawable.twit,
            R.drawable.afr
    };
    private String titles[]={
            "Learn",
            "Create",
            "Enjoy"
    };
    private String desc[]={
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Incidunt rem asperiores harum fuga voluptatem quisquam id quibusdam cum et, minus necessitatibus iusto numquam libero, esse repudiandae ipsa perspiciatis porro aspernatur.\n",
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Incidunt rem asperiores harum fuga voluptatem quisquam id quibusdam cum et, minus necessitatibus iusto numquam libero, esse repudiandae ipsa perspiciatis porro aspernatur.\n",
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Incidunt rem asperiores harum fuga voluptatem quisquam id quibusdam cum et, minus necessitatibus iusto numquam libero, esse repudiandae ipsa perspiciatis porro aspernatur.\n"
    };
    @Override
    public int getCount(){
        return titles.length;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(LinearLayout)object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container,int position){
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.view_page,container,false);
        //initview
        ImageView imageView=v.findViewById(R.id.imageView);
        TextView textTitle=v.findViewById(R.id.textTitleViewPage);
        TextView textSubTitle=v.findViewById(R.id.textSubTitleViewPage);
        imageView.setImageResource(images[position]);
        textTitle.setText(titles[position]);
        textSubTitle.setText(desc[position]);

        container.addView(v);
        return v;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container,int position, @NonNull Object object){
        container.removeView((LinearLayout)object);
    }
}
