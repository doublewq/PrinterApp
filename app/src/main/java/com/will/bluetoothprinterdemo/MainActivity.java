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

        Button kcBtn = (Button) findViewById(R.id.bluethPrinter);
        kcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kcI = new Intent(MainActivity.this, AddProductsActivity.class);
                startActivity(kcI);
            }
        });

        Button addCustomer = (Button) findViewById(R.id.btn_addCustomer);
        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addcus = new Intent(MainActivity.this, AddCustomerActivity.class);
                startActivity(addcus);
            }
        });

        final Button upCustomerbutton = (Button) findViewById(R.id.updateCustomer);
        upCustomerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent write = new Intent(MainActivity.this, UpdateCustomerActivity.class);
                startActivity(write);
            }
        });

        final Button updatebutton = (Button) findViewById(R.id.updateProduct);
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent write = new Intent(MainActivity.this, UpdateProductActivity.class);
                startActivity(write);
            }
        });

        final Button writeButn = (Button) findViewById(R.id.writerData);
        writeButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent write = new Intent(MainActivity.this, WriteDataActivity.class);
                startActivity(write);
            }
        });



        final Button noPrintButn = (Button) findViewById(R.id.noPrintOrder);
        noPrintButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noPrintOrder = new Intent(MainActivity.this, NoPrintActivity.class);
                startActivity(noPrintOrder);
            }
        });

        final Button history = (Button) findViewById(R.id.historyData);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent history = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(history);
            }
        });
    }
}
