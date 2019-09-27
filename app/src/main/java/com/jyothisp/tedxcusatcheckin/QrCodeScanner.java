package com.jyothisp.tedxcusatcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import com.google.zxing.Result;


public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ZXingScannerView mScannerView;


    public void onCreate(Bundle state) {
        super.onCreate(state);
        String str = "android.permission.CAMERA";
        if (ActivityCompat.checkSelfPermission(this, str) == 0) {
            mScannerView = new ZXingScannerView(this);
            setContentView(mScannerView);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{str}, REQUEST_CAMERA_PERMISSION);
    }

    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    public void onPause() {
        super.onPause();
        this.mScannerView.stopCamera();
    }

    public void handleResult(Result rawResult) {
        String str = "result";
        Log.v(str, rawResult.getText());
        Log.v(str, rawResult.getBarcodeFormat().toString());
        Intent intent = new Intent(this, Info.class);
        intent.putExtra("qr", rawResult.getText());
        startActivity(intent);
    }
}
