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
import com.will.bluetoothprinterdemo.utils.SqliteProductUtil;
import com.will.bluetoothprinterdemo.vo.Category;

import java.util.List;

public class AddProductsActivity extends AppCompatActivity {

    private void addToDB(int kind, String name, String price) {
        SqliteProductUtil dbUtil = new SqliteProductUtil(this);
        dbUtil.open();
        dbUtil.insert(kind, name, price);
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
    private List<Category> categoryList = null;
    private ArrayAdapter<String> categoryAdapter = null;

    private EditText name;
    private EditText price;
    private Button btn_addProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        name = (EditText) findViewById(R.id.editProductName);
        price = (EditText) findViewById(R.id.editProductPrice);
        btn_addProduct = (Button) findViewById(R.id.btn_addProduct);

        spinCate = (Spinner) super.findViewById(R.id.spinnerleibie);
        spinCate.setPrompt("请选择类别:");
        //省的适配器绑定
        categoryList = getCategoryFromDB();
        String[] categorys = new String[categoryList.size()];
        int index = 0;
        for (Category cate : categoryList) {
            String tmp = cate.getcID() + " " + cate.getcName();
            categorys[index++] = tmp;
        }
        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categorys);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCate.setAdapter(categoryAdapter);
        setListeners();
    }

    private void setListeners() {
        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kind = spinCate.getSelectedItem().toString().split(" ")[0];
                String proName = name.getText().toString();
                String proPrice = price.getText().toString();
//                System.out.println("--------" + kind + "  " + proName + "  " + proPrice);
                if(proName.trim().length()>0){
                    openDialog("确认添加？","提示",Integer.valueOf(kind), proName, proPrice);
//                    addToDB(Integer.valueOf(kind), proName, proPrice);
//                    Toast.makeText(AddProductsActivity.this, "添加完毕", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddProductsActivity.this, "请输入商品名称", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void openDialog(String strMsg, String strTitle, final int kind,final String proName,final String proPrice) {
        new AlertDialog.Builder(this)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                addToDB(kind, proName, proPrice);
                                Toast.makeText(AddProductsActivity.this, "添加完毕", Toast.LENGTH_SHORT).show();
                            }
                        })
                .show();
    }

}
