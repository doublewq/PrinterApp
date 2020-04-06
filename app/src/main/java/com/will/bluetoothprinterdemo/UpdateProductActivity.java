package com.will.bluetoothprinterdemo;

import android.app.Activity;
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
import com.will.bluetoothprinterdemo.utils.SqliteProductUtil;
import com.will.bluetoothprinterdemo.vo.Category;
import com.will.bluetoothprinterdemo.vo.ProductSingle;

import java.util.List;

public class UpdateProductActivity extends AppCompatActivity {

    private void addToDB(int kind, String name, String price) {
        SqliteProductUtil dbUtil = new SqliteProductUtil(this);
        dbUtil.open();
        dbUtil.insert(kind, name, price);
        dbUtil.close();
    }

    private void deleteEmpty() {
        SqliteProductUtil dbUtil = new SqliteProductUtil(this);
        dbUtil.open();
        dbUtil.deleteEmpty();
        dbUtil.close();
    }

    private List<Category> getCategoryFromDB() {
        SqliteCategoryUtil dbUtil = new SqliteCategoryUtil(this);
        dbUtil.open();
        List<Category> categories = dbUtil.fetchAll();
        dbUtil.close();
        return categories;
    }

    private Spinner spinCate;
    private Spinner spinName;
    private List<Category> categoryList = null;
    private ArrayAdapter<String> categoryAdapter = null;

    private EditText price;
    private Button btn_addProduct;


    private String[][] citys = null;
    private String[] province = {"枕头", "小米枕", "荞麦枕", "枕套", "床垫", "被子", "压缩枕", "凉枕", "夏凉被"};

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateproduct);

        price = (EditText) findViewById(R.id.editupProductPrice);
        btn_addProduct = (Button) findViewById(R.id.btn_upProduct);

        spinCate = (Spinner) findViewById(R.id.spinnerupleibie);
        spinName = (Spinner) findViewById(R.id.spinnerupProductName);

//        deleteEmpty();
        citys = getcitylist();
        //省的适配器绑定
        provinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, province);
        spinCate.setAdapter(provinceAdapter);
        //市的适配器绑定
        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        spinName.setAdapter(cityAdapter);

        //监听省的Spinner选项的选择
        spinCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //根据省的选择i来改变市的数据
                String[] city = citys[position];
                //适配器清零
                cityAdapter.clear();
                //添加市的数据
                cityAdapter.addAll(city);
                spinName.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //监听市的Spinner选项的选择
        spinName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String kind = spinCate.getSelectedItem().toString();
                String proName = spinName.getSelectedItem().toString();
                String proprice = getPrice(kind, proName);
                price.setText(proprice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setListeners();
    }

    private String getPrice(String kind, String proName) {
        // 找类别编号
        int CID = 0;
        for (int i = 0; i < province.length; i++) {
            if (kind.equals(province[i])) {
                CID = i + 1;
            }
        }
        // 找price;
        Double proPrice = 0.0;
        SqliteProductUtil dbUtil = new SqliteProductUtil(this);
        dbUtil.open();
        List<ProductSingle> productSingleList = dbUtil.fetchByCID(CID);
        for (ProductSingle pro : productSingleList) {
            if (pro.getpName().equals(proName)) {
                proPrice = pro.getpPrice();
            }
        }
        return String.valueOf(proPrice);
    }

    public String[][] getcitylist() {
        SqliteProductUtil dbUtil = new SqliteProductUtil(this);
        dbUtil.open();
        String[][] result = new String[9][];
        List<ProductSingle> productLists = null;
        for (int i = 1; i <= 9; i++) {
            productLists = dbUtil.fetchByCID(i);
            result[i - 1] = new String[productLists.size()];
            for (int j = 0; j < productLists.size(); j++) {
                result[i - 1][j] = productLists.get(j).getpName();
            }
        }
        return result;
    }

    private void setListeners() {
        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kind = spinCate.getSelectedItem().toString();
                String proName = spinName.getSelectedItem().toString();
                String proPrice = price.getText().toString();
                if (proPrice.length() > 0) {
                    updateToDB(kind, proName, proPrice);
                    Toast.makeText(UpdateProductActivity.this, "更新价格成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateProductActivity.this, "请输入更新价格！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateToDB(String kind, String proName, String proPrice) {
        // 找类别编号
        int CID = 0;
        for (int i = 0; i < province.length; i++) {
            if (kind.equals(province[i])) {
                CID = i + 1;
            }
        }
        // 找pID;
        int pID = 0;
        SqliteProductUtil dbUtil = new SqliteProductUtil(this);
        dbUtil.open();
        List<ProductSingle> productSingleList = dbUtil.fetchByCID(CID);
        for (ProductSingle pro : productSingleList) {
            if (pro.getpName().equals(proName)) {
                pID = pro.getpID();
            }
        }
        dbUtil.updatePrice(pID, Double.valueOf(proPrice));
        dbUtil.close();
    }

}
