package com.will.bluetoothprinterdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.will.bluetoothprinterdemo.ui.PrinterSettingActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button kcBtn= (Button) findViewById(R.id.bluethPrinter);
        kcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kcI=new Intent(MainActivity.this, PrinterSettingActivity.class);
                startActivity(kcI);
            }
        });

        final Button writeButn= (Button) findViewById(R.id.writerData);
        writeButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent write=new Intent(MainActivity.this, WriteDataActivity.class);
                startActivity(write);
            }
        });
    }
}
