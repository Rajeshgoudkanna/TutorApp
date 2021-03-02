package com.example.tutorapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.tutorapp.Activities.CourseDetailsActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends BaseAdapter {
    List<Course> ar;
    List<Course> courseDataPojo;
    Context cnt;

    String session;
    DatabaseReference RootRef, getfav;
    SharedPreferences sharedPreferences;

    public SearchAdapter(List<Course> datapojo, Context cnt) {
        this.courseDataPojo = datapojo;
        this.cnt = cnt;
        this.ar = new ArrayList<Course>();
        this.ar.addAll(datapojo);
    }


    @Override
    public int getCount() {
        return  ar.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private DatabaseReference ProductsRef;

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        LayoutInflater obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.adapter_course, null);
        sharedPreferences = obj2.getContext().getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "");
        ImageView image_view = (ImageView) obj2.findViewById(R.id.image_view);
        Glide.with(cnt).load(ar.get(pos).getImage()).into(image_view);

        TextView tv_cname = (TextView) obj2.findViewById(R.id.tv_cname);
        tv_cname.setText(ar.get(pos).getCname());

        CardView cvParent = (CardView) obj2.findViewById(R.id.cvParent);
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
        final Boolean[] clicked = {true};
        final ImageView img_fav = (ImageView) obj2.findViewById(R.id.img_fav);
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

        getfav = FirebaseDatabase.getInstance().getReference();


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
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(cnt, "Add to favourite courses successfully.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(cnt, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });
                } else {
                    Toast.makeText(cnt, "This course" + id + " already exists.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ar.clear();
        if (charText.length() == 0) {
            ar.addAll(courseDataPojo);
        } else {
            for (Course wp : courseDataPojo) {
                if (wp.getCname().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    ar.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
