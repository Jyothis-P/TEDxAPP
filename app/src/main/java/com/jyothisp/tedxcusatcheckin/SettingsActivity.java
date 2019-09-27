package com.jyothisp.tedxcusatcheckin;



import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    RadioGroup actionGroup;
    SharedPreferences pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_settings);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        initViews();
        actionGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String action = "checkin";
                if (checkedId == R.id.action_checkin) {
                    action = "checkin";
                } else if (checkedId != R.id.action_extra) {
                    switch (checkedId) {
                        case R.id.action_tea_1 /*2131230781*/:
                            action = "tea1";
                            break;
                        case R.id.action_tea_2 /*2131230782*/:
                            action = "lunch";
                            break;
                        case R.id.action_tea_3 /*2131230783*/:
                            action = "tea2";
                            break;
                    }
                } else {
                    action = "extra";
                }
                Editor editor = SettingsActivity.this.pref.edit();
                editor.putString("action", action);
                editor.commit();
            }
        });
    }

    private void initViews() {
        actionGroup = (RadioGroup) findViewById(R.id.action_group);
    }
}