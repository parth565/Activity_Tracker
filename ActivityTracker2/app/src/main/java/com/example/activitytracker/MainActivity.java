package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import static android.Manifest.permission.ACTIVITY_RECOGNITION;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    Button btnSubmitHeight;
    TextView txtInputHeight;
    TextView txtInputWeight;
    TextView txtTest;
    String inputHeight;
    String inputWeight;

    int PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp;
        sp = getSharedPreferences("userHeight", Context.MODE_PRIVATE);

        if (!checkPermission()) {
            Toast.makeText(getApplicationContext(), "We need this permission to continue!", Toast.LENGTH_SHORT).show();
            requestPermission();
        }

        txtInputHeight = (TextView) findViewById(R.id.txtInputHeight);
        txtInputWeight = (TextView) findViewById(R.id.txtInputWeight);
        btnSubmitHeight = (Button) findViewById(R.id.btnSubmitHeight);
        txtTest = (TextView) findViewById((R.id.txtTesting));

        btnSubmitHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputHeight = txtInputHeight.getText().toString();
                inputWeight = txtInputWeight.getText().toString();
                txtTest.setText(inputHeight);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("height", inputHeight);
                editor.commit();
                Toast.makeText(MainActivity.this, "Information Saved.", Toast.LENGTH_LONG).show();
                openActivity2();
            }
        });
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, ACTIVITY_RECOGNITION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(getApplicationContext(), "Please allow access to continue!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void openActivity2(){
        Intent intent = new Intent(this, Statistics.class);
        intent.putExtra("Height", inputHeight);
        intent.putExtra("Weight", inputWeight);
        startActivity(intent);
    }

}