package com.abhilash.servicesapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
        }

        Button downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(view -> {
            EditText pdf1 = findViewById(R.id.pdf1);
            EditText pdf2 = findViewById(R.id.pdf2);
            EditText pdf3 = findViewById(R.id.pdf3);
            EditText pdf4 = findViewById(R.id.pdf4);
            EditText pdf5 = findViewById(R.id.pdf5);

            String[] pdfUrls = {
                    pdf1.getText().toString(),
                    pdf2.getText().toString(),
                    pdf3.getText().toString(),
                    pdf4.getText().toString(),
                    pdf5.getText().toString()
            };

            Intent intent = new Intent(MainActivity.this, PDFDownloadService.class);
            intent.putExtra("pdfUrls", pdfUrls);
            startService(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission is required for download notifications", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
