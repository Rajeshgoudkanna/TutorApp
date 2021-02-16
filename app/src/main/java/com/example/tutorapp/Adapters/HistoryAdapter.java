package com.example.tutorapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tutorapp.R;
import com.example.tutorapp.model.Course;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    List<Course> ar;
    Context cnt;

    public HistoryAdapter(List<Course> ar, Context cnt) {
        this.ar = ar;
        this.cnt = cnt;
    }


    public int getCount() {
        return ar.size();
    }


    public Object getItem(int i) {
        return i;
    }


    public long getItemId(int i) {
        return i;
    }


    public View getView(final int pos, View view, ViewGroup viewGroup) {
        LayoutInflater obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.adapter_history, null);

        ImageView image_view=(ImageView)obj2.findViewById(R.id.image_view);
        Glide.with(cnt).load(ar.get(pos).getImage()).into(image_view);

        TextView tv_cname = (TextView) obj2.findViewById(R.id.tv_cname);
        tv_cname.setText(ar.get(pos).getCname());
        TextView tv_category = (TextView) obj2.findViewById(R.id.tv_category);
        tv_category.setText(ar.get(pos).getCcategory());

        RatingBar tv_rating=(RatingBar)obj2.findViewById(R.id.tv_rating);

        return obj2;
    }
}
