package com.abhilash.androidactivitylifecycle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.androidactivitylifecycle.R;

public class MainActivity extends AppCompatActivity {
    int threadCounter = 0;
    private static final int ACTIVITY_B = 1;
    private static final int ACTIVITY_C = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        TextView threadCounterText = findViewById(R.id.thread_counter_txt);
        threadCounterText.setText("ThreadCounter: " + String.valueOf(threadCounter));
    }

    public void onStartActivityB (View view) {
        Intent activityBIntent = new Intent(MainActivity.this, ActivityB.class);
        startActivityForResult(activityBIntent, ACTIVITY_B);
    }
    public void onStartActivityC(View view) {
        Intent activityCIntent = new Intent(MainActivity.this, ActivityC.class);
        startActivityForResult(activityCIntent, ACTIVITY_C);
    }

    public void onTriggerDialog (View view) {
        new AlertDialog.Builder(this)
                .setTitle("Dialog")
                .setMessage("This is a Simple dialog")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_B) {
            threadCounter += 5;
        } else if (requestCode == ACTIVITY_C) {
            threadCounter += 10;
        }
        TextView threadCounterText = findViewById(R.id.thread_counter_txt);
        threadCounterText.setText("ThreadCounter: " + String.valueOf(threadCounter));
    }

    public void onCloseApp (View view) {
        finish();
        System.exit(0);
    }
}