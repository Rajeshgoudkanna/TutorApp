package com.example.tutorapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.tutorapp.Activities.CourseDetailsActivity;
import com.example.tutorapp.Activities.CourseReviewsActivity;
import com.example.tutorapp.Activities.TutorViewCoursesActivity;
import com.example.tutorapp.Activities.Utils;
import com.example.tutorapp.R;
import com.example.tutorapp.model.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class CourseAdapter extends BaseAdapter {
    List<Course> ar;
    List<Float> rat;
    Context cnt;
    String session;
    DatabaseReference RootRef, getfav;
    SharedPreferences sharedPreferences;
    String user_role;
    Float rate = 0f;

    public CourseAdapter(Context cnt, List<Course> ar, String session) {
        this.ar = ar;
        this.cnt = cnt;
        this.session = session;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        LayoutInflater obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.adapter_course, null);
        sharedPreferences = obj2.getContext().getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "");
        user_role = sharedPreferences.getString("user_role", "");

        ImageView image_view = (ImageView) obj2.findViewById(R.id.image_view);
        Glide.with(cnt).load(ar.get(pos).getImage()).into(image_view);

        TextView tv_cname = (TextView) obj2.findViewById(R.id.tv_cname);
        tv_cname.setText(ar.get(pos).getCname());
        TextView tv_category = (TextView) obj2.findViewById(R.id.tv_category);
        tv_category.setText(ar.get(pos).getCcategory());

        TextView tv_course_id = (TextView) obj2.findViewById(R.id.tv_course_id);
        tv_course_id.setText(ar.get(pos).getPid());
        RatingBar tv_rating = (RatingBar) obj2.findViewById(R.id.tv_rating);
//        Query query=FirebaseDatabase.getInstance().getReference("Course Reviews").child(ar.get(pos).getPid());
//
//        query.addListenerForSingleValueEvent(valueEventListener);



        tv_rating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == motionEvent.ACTION_UP) {

                    Intent intent = new Intent(cnt, CourseReviewsActivity.class);
                    intent.putExtra("pid", ar.get(pos).getPid());
                    cnt.startActivity(intent);
                }
                return true;
            }
        });


        CardView cvParent = (CardView) obj2.findViewById(R.id.cvParent);
        cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_role.equals("Tutor")) {

                    String selected_item = ((TextView) view.findViewById(R.id.tv_course_id)).getText().toString();
                    Intent intent = new Intent(cnt, TutorViewCoursesActivity.class);
                    intent.putExtra("selected", selected_item);
                    cnt.startActivity(intent);

                } else {
                    Intent intent = new Intent(cnt, CourseDetailsActivity.class);
                    intent.putExtra("Cname", ar.get(pos).getCname());
//                    intent.putExtra("Cstatus",ar.get(pos).getCstatus());
                    intent.putExtra("Cprice", ar.get(pos).getCprice());
                    intent.putExtra("Cdescription", ar.get(pos).getCdescription());
                    intent.putExtra("Ctype", ar.get(pos).getCtype());
                    intent.putExtra("Ccategory", ar.get(pos).getCcategory());
                    intent.putExtra("CstartDate", ar.get(pos).getCstartDate());
                    intent.putExtra("CendDate", ar.get(pos).getCendDate());
                    intent.putExtra("Cimage", ar.get(pos).getCimage());
                    intent.putExtra("pid", ar.get(pos).getPid());
                    cnt.startActivity(intent);
                }
            }
        });

        final Boolean[] clicked = {true};
        final ImageView img_fav=(ImageView)obj2.findViewById(R.id.img_fav);
        RootRef = FirebaseDatabase.getInstance().getReference().child("Favorite Courses").child(session);
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(ar.get(pos).getPid())) {
                    img_fav.setImageResource(R.drawable.ic_heart);
                    clicked[0] = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getfav =  FirebaseDatabase.getInstance().getReference();


        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clicked[0]) {
                    img_fav.setImageResource(R.drawable.ic_heart);
                    Log.i("fav", "course" + ar.get(pos).getPid() + ar.get(pos).getImage() + ar.get(pos).getCdescription() + ar.get(pos).getCprice() + ar.get(pos).getCtype());
                    favList(ar.get(pos).getPid().toString(), ar.get(pos).getImage().toString(), ar.get(pos).getCname().toString()
                            , ar.get(pos).getCdescription().toString(), ar.get(pos).getCprice().toString(), ar.get(pos).getCtype().toString(),
                            ar.get(pos).getCcategory().toString(), ar.get(pos).getCstartDate(), ar.get(pos).getCendDate().toString());


                    clicked[0] = false;
                } else {
                    getfav.child("Favorite Courses").child(session).child(ar.get(pos).getPid()).removeValue();
                    Toast.makeText(cnt, "Remove from your favourite successfully.", Toast.LENGTH_SHORT).show();
                    clicked[0] = true;
                    img_fav.setImageResource(R.drawable.ic_fav);

                }

            }
        });
        return obj2;
    }
//    ValueEventListener valueEventListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            if (dataSnapshot.exists()) {
//                int count =0;
//                Float rateChange = 0f;
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Rating rating = snapshot.getValue(Rating.class);
//                    rateChange +=Float.parseFloat(rating.getRating());
//                    count+=1;
//                }
//                rate = rateChange/count;
//                rat.add(rate);
//            }
//            else {
//                rat.add(0f);
//                Toast.makeText(cnt, "No data Found", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };

    private DatabaseReference ProductsRef;

    public void favList(final String id, final String image, final String name, final String description
            , final String price, final String type, final String catergory, final String startdate, final String enddate) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Favorite Courses").child(session).child(id).exists())) {
                    HashMap<String, Object> productMap = new HashMap<>();
                    productMap.put("pid", id);
                    productMap.put("Cimage", image);
                    productMap.put("Cname", name);
                    productMap.put("Cdescription", description);
                    productMap.put("Cprice", price);
                    productMap.put("Ctype", type);
//                    productMap.put("Cstatus", status);
                    productMap.put("Ccategory", catergory);
                    productMap.put("CstartDate", startdate);
                    productMap.put("CendDate", enddate);

                    RootRef.child("Favorite Courses").child(session).child(id).updateChildren(productMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(cnt, "Add to favourite courses successfully.", Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        Toast.makeText(cnt, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });
                }
                else
                {
                    Toast.makeText(cnt, "This course" + id + " already exists.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}