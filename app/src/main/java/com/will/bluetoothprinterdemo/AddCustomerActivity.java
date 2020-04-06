package com.will.bluetoothprinterdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.will.bluetoothprinterdemo.utils.SqliteCategoryUtil;
import com.will.bluetoothprinterdemo.utils.SqliteCustomerUtil;
import com.will.bluetoothprinterdemo.utils.SqliteProductUtil;
import com.will.bluetoothprinterdemo.vo.Category;

import java.util.List;

public class AddCustomerActivity extends AppCompatActivity {

    private Spinner spinCate;
    private List<Category> categoryList = null;
    private ArrayAdapter<String> categoryAdapter = null;

    private EditText name;
    private EditText phone;
    private EditText beizhu;
    private Button btn_addCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcustomer);

        name = (EditText) findViewById(R.id.editCuName);
        phone = (EditText) findViewById(R.id.editCuPhone);
        beizhu = (EditText) findViewById(R.id.editCuBeiZhu);
        btn_addCustomer = (Button) findViewById(R.id.btn_addCustomer);
        setListeners();
    }

    private void setListeners() {
        btn_addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameit = name.getText().toString();
                String phoneit = phone.getText().toString();
                String beizhuit = beizhu.getText().toString();

                if (nameit.trim().length() > 0) {
                    openDialog("确认添加？", "提示", nameit, phoneit, beizhuit);
                } else {
                    Toast.makeText(AddCustomerActivity.this, "请输入客户姓名", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addToDB(String name, String phone, String beizhu) {
        SqliteCustomerUtil dbUtil = new SqliteCustomerUtil(this);
        dbUtil.open();
        dbUtil.insert(name, phone, beizhu);
        dbUtil.close();
    }


    private void openDialog(String strMsg, String strTitle, final String name, final String phone, final String beizhu) {
        new AlertDialog.Builder(this)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                addToDB(name, phone, beizhu);
                                Toast.makeText(AddCustomerActivity.this, "添加客户完毕", Toast.LENGTH_SHORT).show();
                            }
                        })
                .show();
    }

}
