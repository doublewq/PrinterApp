package com.will.bluetoothprinterdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.will.bluetoothprinterdemo.utils.SqliteCategoryUtil;
import com.will.bluetoothprinterdemo.utils.SqliteCustomerUtil;
import com.will.bluetoothprinterdemo.utils.SqliteProductUtil;
import com.will.bluetoothprinterdemo.vo.Category;
import com.will.bluetoothprinterdemo.vo.Customer;
import com.will.bluetoothprinterdemo.vo.ProductSingle;

import java.util.List;

public class UpdateCustomerActivity extends AppCompatActivity {

    private Spinner spinName;
    private List<Customer> customersList = null;

    private EditText cuName;
    private EditText cuPhone;
    private EditText cuBeizhu;
    private Button btn_upCustomer;


    private void updateToDB(String oldName,String name,String phone,String beizhu){
        int cId = -1;
        for(Customer customer :customersList){
            if(oldName.equals(customer.getName())){
                cId = customer.getId();
            }
        }
        if(cId !=-1){
            SqliteCustomerUtil dbUtil = new SqliteCustomerUtil(this);
            dbUtil.open();
            dbUtil.update(cId,name, phone, beizhu);
            dbUtil.close();
        }
    }


    private List<Customer> getCategoryFromDB() {
        SqliteCustomerUtil dbUtil = new SqliteCustomerUtil(this);
        dbUtil.open();
        List<Customer> customers = dbUtil.fetchAll();
        dbUtil.close();
        return customers;
    }
    private ArrayAdapter<String> cuNameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatecustomer);

        spinName = (Spinner) findViewById(R.id.spinnerupyCuName);

        cuName = (EditText) findViewById(R.id.editupCuName);
        cuPhone = (EditText) findViewById(R.id.editupCuPhone);
        cuBeizhu = (EditText) findViewById(R.id.editupCuBeizhu);

        btn_upCustomer = (Button) findViewById(R.id.btn_upCustomer);

//        deleteEmpty();
        customersList = getCategoryFromDB();
        String[] cuNameSpin = new String[customersList.size()];
        int index = 0;
        for (Customer customer:customersList) {
            cuNameSpin[index++] = customer.getName();
        }
        System.out.println(cuNameSpin.length+":"+cuNameSpin[0]+"----------length"+customersList.size());
        //适配器绑定
        cuNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cuNameSpin);
        cuNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinName.setAdapter(cuNameAdapter);

        final String[] cuNameFinal = cuNameSpin;

        //监听省的Spinner选项的选择
        spinName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //根据省的选择i来改变市的数据
                String cuNameSelect = cuNameFinal[position];
                for(Customer customer:customersList){
                    if(cuNameSelect.equals(customer.getName())){
                        cuName.setText(customer.getName());
                        cuPhone.setText(customer.getPhone());
                        cuBeizhu.setText(customer.getBeizhu());
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setListeners();
    }
    private void setListeners() {
        btn_upCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldName = spinName.getSelectedItem().toString();
                String cuNameit = cuName.getText().toString();
                String cuPhoneit = cuPhone.getText().toString();
                String cuBeizhuit = cuBeizhu.getText().toString();
                if(cuNameit.length()>0){
                    updateToDB(oldName,cuNameit,cuPhoneit,cuBeizhuit);
                    Toast.makeText(UpdateCustomerActivity.this, "更新客户信息成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UpdateCustomerActivity.this, "请输入修改后的名字", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
