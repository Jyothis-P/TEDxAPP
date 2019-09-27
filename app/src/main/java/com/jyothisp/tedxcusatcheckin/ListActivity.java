package com.jyothisp.tedxcusatcheckin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListActivity extends AppCompatActivity {

    private String ACTION = "checkin";
    private FirestoreRecyclerAdapter<Attendee, AttendeeViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
//        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ACTION = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE).getString("action", "checkin");

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        Query query = rootRef.collection("attendees")
                .whereEqualTo(ACTION, true)
                .orderBy("seat", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Attendee> options = new FirestoreRecyclerOptions.Builder<Attendee>()
                .setQuery(query, Attendee.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Attendee, AttendeeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AttendeeViewHolder attendeeViewHolder, int position, @NonNull Attendee productModel) {
                attendeeViewHolder.setProductName(productModel);
            }

            @NonNull
            @Override
            public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                return new AttendeeViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }


    /* access modifiers changed from: protected */
    @Override
    public void onResume() {
        super.onResume();
        ACTION = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE).getString("action", "checkin");
        Log.e("ASD", ACTION);
    }


    private class AttendeeViewHolder extends RecyclerView.ViewHolder {
        private View view;

        AttendeeViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setProductName(Attendee attendee) {
            TextView textViewPhone = view.findViewById(R.id.text_view_phone);
            textViewPhone.setText(attendee.getPhone());
            TextView textView = view.findViewById(R.id.text_view);
            textView.setText(attendee.getName());
            TextView textViewSeat = view.findViewById(R.id.text_view_seat);
            textViewSeat.setText(attendee.getSeat());
            final String phone = attendee.getPhone();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String posted_by = phone;

                    String uri = "tel:" + posted_by.trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
