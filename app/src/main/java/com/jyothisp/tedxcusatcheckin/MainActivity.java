package com.jyothisp.tedxcusatcheckin;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    /* access modifiers changed from: private */
    public String ACTION = "checkin";
    TextView RemainingView;
    TextView completedView;
    TextView actionView;
    LinearLayout statsView;
    int count = 0;

    /* renamed from: db */
    FirebaseFirestore db;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView((int) R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        Button btn = (Button) findViewById(R.id.button);
        completedView = (TextView) findViewById(R.id.completed);
        RemainingView = (TextView) findViewById(R.id.remaining);
        actionView = (TextView) findViewById(R.id.action);
        statsView =  findViewById(R.id.stats);
        getFirestoreValues();
        statsView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.startActivity(new Intent(mainActivity, QrCodeScanner.class));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        ACTION = getApplicationContext().getSharedPreferences("MyPref", 0).getString("action", "checkin");
        getFirestoreValues();
        String str = "";
        switch (ACTION) {
            case "checkin":
                str = "Check In";
                break;
            case "tea1":
                str = "Morning Tea";
                break;
            case "lunch":
                str = "Lunch";
                break;
            case "tea2":
                str = "Evening Tea";
                break;
            default:
                str = "extra";
                break;
        }
        actionView.setText(str);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.select_action) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.view_list) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    private void getFirestoreValues() {
        db.collection("attendees").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                MainActivity.this.count = 0;
                Iterator it = queryDocumentSnapshots.iterator();
                while (it.hasNext()) {
                    try {
                        if (((DocumentSnapshot) it.next()).getBoolean(ACTION).booleanValue()) {
                            MainActivity.this.count++;
                        }
                    } catch (Exception e) {
                        Log.e(MainActivity.TAG, "Error: ", e);
                    }
                }
                TextView textView = MainActivity.this.completedView;
                StringBuilder sb = new StringBuilder();
                sb.append(MainActivity.this.count);
                String str = "";
                sb.append(str);
                textView.setText(sb.toString());
                TextView textView2 = MainActivity.this.RemainingView;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(100 - MainActivity.this.count);
                sb2.append(str);
                textView2.setText(sb2.toString());
            }
        });
    }
}

//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//public class MainActivity extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Button button = findViewById(R.id.button);
//
//        button.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//
//                startActivity(new Intent(MainActivity.this, QrCodeScanner.class));
////                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
////                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
////                startActivityForResult(intent, 0);
//            }
//        });
//    }
//
//
//
//
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                String contents = intent.getStringExtra("SCAN_RESULT");
//                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
//                Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_SHORT).show();
//                // Handle successful scan
//            } else if (resultCode == RESULT_CANCELED) {
//                // Handle cancel
//                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    }
//}
