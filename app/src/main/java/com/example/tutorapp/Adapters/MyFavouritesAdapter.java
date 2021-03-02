package com.example.tutorapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.tutorapp.Activities.CourseDetailsActivity;
import com.example.tutorapp.R;
import com.example.tutorapp.model.Course;

import java.util.List;

public class MyFavouritesAdapter extends BaseAdapter {
    List<Course> ar;
    Context cnt;

    public MyFavouritesAdapter(List<Course> ar, Context cnt) {
        this.ar = ar;
        this.cnt = cnt;
    }

    @Override
    public int getCount() {
        return ar.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        LayoutInflater obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.adapter_my_favourite, null);
        ImageView image_view=(ImageView)obj2.findViewById(R.id.image_view);
        Glide.with(cnt).load(ar.get(pos).getImage()).into(image_view);
        TextView tv_cname = (TextView)obj2.findViewById(R.id.tv_cname);
        tv_cname.setText(ar.get(pos).getCname());
        CardView cvParent=(CardView)obj2.findViewById(R.id.cvParent);
        cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(cnt, CourseDetailsActivity.class);
                intent.putExtra("Cname", ar.get(pos).getCname().toString());
                intent.putExtra("Cprice", ar.get(pos).getCprice().toString());
                intent.putExtra("Cdescription", ar.get(pos).getCdescription().toString());
                intent.putExtra("Ctype", ar.get(pos).getCtype().toString());
//                intent.putExtra("Cstatus", ar.get(pos).getCstatus().toString());
                intent.putExtra("Ccategory", ar.get(pos).getCcategory().toString());
                intent.putExtra("Cimage", ar.get(pos).getImage().toString());
                intent.putExtra("pid", ar.get(pos).getPid().toString());
                intent.putExtra("posted_by", ar.get(pos).getPid().toString());
                intent.putExtra("CstartDate", ar.get(pos).getCstartDate().toString());
                intent.putExtra("CendDate", ar.get(pos).getCendDate().toString());
                cnt.startActivity(intent);
            }
        });
        return obj2;
    }
}