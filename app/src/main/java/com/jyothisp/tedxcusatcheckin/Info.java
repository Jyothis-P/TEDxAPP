package com.jyothisp.tedxcusatcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Info extends AppCompatActivity {
    private static final String TAG = "InfoActivity";
    private String ACTION = "checkin";
    Button checkInButton;

    /* renamed from: db */
    FirebaseFirestore db;
    TextView fnameView;
    ImageView foodIconView;
    TextView foodView;
    TextView lnameView;
    TextView seatView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_info);
        db = FirebaseFirestore.getInstance();
        initView();
        final String id = getIntent().getStringExtra("qr");
        getFirestoreValues(id);
        this.checkInButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Info.this.checkInButton.setText("Checking in...");
                Info.this.checkIn(id);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        ACTION = getApplicationContext().getSharedPreferences("MyPref", 0).getString("action", "checkin");
    }

    /* access modifiers changed from: private */
    public void checkIn(String id) {
        Log.e(TAG, this.ACTION);
        db.collection("attendees").document(id).update(ACTION, (Object) Boolean.valueOf(true), new Object[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void aVoid) {
                Info.this.checkInButton.setText("Check In");
                String str = "Checked In";
                Log.d(Info.TAG, str);
                Toast.makeText(Info.this.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                Info info = Info.this;
                info.startActivity(new Intent(info, MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                Log.w(Info.TAG, "Error adding document", e);
                Toast.makeText(Info.this.getApplicationContext(), "Error cehching in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        this.fnameView = (TextView) findViewById(R.id.fname);
        this.lnameView = (TextView) findViewById(R.id.lname);
        this.seatView = (TextView) findViewById(R.id.seat);
        this.foodView = (TextView) findViewById(R.id.food);
        this.foodIconView = (ImageView) findViewById(R.id.foodicon);
        this.checkInButton = (Button) findViewById(R.id.checkin);
    }

    /* access modifiers changed from: private */
    public String getLname(String[] names) {
        String res = "";
        for (int i = 2; i < names.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(res);
            sb.append(names[i]);
            String res2 = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(res2);
            sb2.append(" ");
            res = sb2.toString();
        }
        return res;
    }

    private void getFirestoreValues(String id) {
        db.collection("attendees").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(Task<DocumentSnapshot> task) {
                String str;
                StringBuilder sb;
                boolean isSuccessful = task.isSuccessful();
                String str2 = Info.TAG;
                if (isSuccessful) {
                    DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                    String name = document.get("name").toString();
                    String seat = document.get("seat").toString();
                    String food = document.get("food").toString();
                    String str3 = " ";
                    String[] names = name.split(str3);
                    if (names.length > 2) {
                        sb = new StringBuilder();
                        sb.append(names[0]);
                        sb.append(str3);
                        str = names[1];
                    } else {
                        sb = new StringBuilder();
                        str = names[0];
                    }
                    sb.append(str);
                    sb.append(str3);
                    String fname = sb.toString();
                    String lname = "";
                    if (names.length != 1){
                        lname = names.length > 2 ? Info.this.getLname(names) : names[1];
                    }

                    Info.this.fnameView.setText(fname);
                    Info.this.lnameView.setText(lname);
                    Info.this.seatView.setText(seat);
                    if (food.equals("veg")) {
                        Info.this.foodIconView.setImageResource(R.drawable.veg);
                        Info.this.foodView.setText("Veg");
                    } else {
                        Info.this.foodIconView.setImageResource(R.drawable.nonveg);
                        Info.this.foodView.setText("Non-Veg");
                    }
                    return;
                }
                Log.w(str2, "Error getting documents.", task.getException());
            }
        });
    }
}
