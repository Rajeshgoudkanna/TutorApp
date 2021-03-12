package com.example.tutorapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tutorapp.R;
import com.example.tutorapp.model.Rating;

import java.util.List;

public class CourseReviewsAdapter extends BaseAdapter {
    List<Rating> ar;
    Context cnt;

    public CourseReviewsAdapter(List<Rating> ar, Context cnt) {
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
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        LayoutInflater obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.adapter_course_reviews, null);

        TextView tv_username = (TextView) obj2.findViewById(R.id.tv_username);
        tv_username.setText(ar.get(pos).getUsername());
        TextView tv_review = (TextView) obj2.findViewById(R.id.course_review);
        tv_review.setText(ar.get(pos).getReview());

        RatingBar ratingBar=(RatingBar)obj2.findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.parseFloat(ar.get(pos).getRating()));

        return obj2;
    }
}
