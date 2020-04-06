package com.will.bluetoothprinterdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.will.bluetoothprinterdemo.utils.OrderSqliteUtil;
import com.will.bluetoothprinterdemo.utils.ProductSqliteUtil;
import com.will.bluetoothprinterdemo.vo.Order;
import com.will.bluetoothprinterdemo.vo.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private EditText editProUser;
    private EditText editProPhone;
    private EditText editProTime;

    private EditText editProNum;
    private EditText editProPay;
    private ListView lv_products;
    private List<Product> data;

    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        //取传过来的值
        Intent intent = getIntent();
        //用getXxxExtra()取出对应类型的数据。取出String只需要指定key
        String orderID = intent.getStringExtra("orderID");
        //初始化成员变量
        lv_products = (ListView) findViewById(R.id.lv_products);
        editProUser = (EditText) findViewById(R.id.editProUser);
        editProPhone = (EditText) findViewById(R.id.editProPhone);
        editProTime = (EditText) findViewById(R.id.editProTime);
        editProNum = (EditText) findViewById(R.id.editProNum);
        editProPay = (EditText) findViewById(R.id.editProPay);

        Order curOrder = getOrderFromDB(orderID);
        editProUser.setText(curOrder.getConsumerName());
        editProPhone.setText(curOrder.getConsumerPhone());
        editProTime.setText(curOrder.getTime());
        editProPay.setText(curOrder.getSalary() + "");
        editProNum.setText(curOrder.getProductNum()+"");
        data = getAllProductInfos(orderID);

        adapter = new ProductAdapter();
        //显示列表
        lv_products.setAdapter(adapter);
    }

    private Order getOrderFromDB(String orderID) {
        OrderSqliteUtil dbUtil = new OrderSqliteUtil(this);
        dbUtil.open();
        Order order = dbUtil.fetchByOrderID(orderID);
        dbUtil.close();
        return order;
    }

    protected List<Product> getAllProductInfos(String orderID) {
        List<Product> list = new ArrayList<>();
        ProductSqliteUtil dbUtil = new ProductSqliteUtil(this);
        dbUtil.open();
        list = dbUtil.fetchByOrderID(orderID);
        dbUtil.close();
        return list;
    }

    class ProductAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        //返回带数据当前行的Item视图对象
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //1. 如果convertView是null, 加载item的布局文件
            if (convertView == null) {
                convertView = View.inflate(ProductsActivity.this, R.layout.item_order_history, null);
            }
            //2. 得到当前行数据对象
            Product appInfo = data.get(position);
            //3. 得到当前行需要更新的子View对象
            TextView textView = (TextView) convertView.findViewById(R.id.txt_history_order);
            //4. 给视图设置数据
            // 小计 小数点保留两位
            double sumPricedoub = (Integer.valueOf(appInfo.getNumbers()) * Double.valueOf(appInfo.getPrice()));
            DecimalFormat formPrice = new DecimalFormat("#.00");
            String sumPrice = formPrice.format(sumPricedoub);

            textView.setText(appInfo.getName() + "\t\t\t" + appInfo.getColor() + "\t\t\t" + appInfo.getNumbers() + "\t\t\t" + appInfo.getPrice() + "\t\t\t" + sumPrice);
            //返回convertView
            return convertView;
        }

    }
}
