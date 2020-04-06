package com.will.bluetoothprinterdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.will.bluetoothprinterdemo.utils.OrderSqliteUtil;
import com.will.bluetoothprinterdemo.vo.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class HistoryActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private ListView lv_main;
    private List<Order> data;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //初始化成员变量
        lv_main = (ListView) findViewById(R.id.lv_history);
        data = getAllOrderInfos();

        adapter = new OrderAdapter();
        //显示列表
        lv_main.setAdapter(adapter);

        //给ListView设置item的点击监听
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * parent : ListView
             * view : 当前行的item视图对象
             * position : 当前行的下标
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //提示当前行的应用名称
                String appName = data.get(position).getConsumerName();
                String orderID = data.get(position).getOrderID();
                //提示
                Intent kcI = new Intent(HistoryActivity.this, ProductsActivity.class);
                kcI.putExtra("orderID", orderID);
                startActivity(kcI);
//                Toast.makeText(HistoryActivity.this, appName, Toast.LENGTH_SHORT).show();
            }
        });

        //给LitView设置Item的长按监听
        lv_main.setOnItemLongClickListener(this);

    }

    private void openDialog(String strMsg, String strTitle, final int position) {
        new AlertDialog.Builder(this)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Order scOrder = data.get(position);
                                OrderSqliteUtil dbUtil = new OrderSqliteUtil(HistoryActivity.this);
                                dbUtil.open();
                                dbUtil.updateToHasPrint(scOrder.getOrderID());
                                dbUtil.close();

                                data.remove(position);
                                //显示列表
                                lv_main.setAdapter(adapter);//显示列表, 不会使用缓存的item的视图对象
                                adapter.notifyDataSetChanged();//通知更新列表, 使用所有缓存的item的视图对象
                            }
                        })
                .show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // 确认删除窗口
        openDialog("确认删除？", "提示", position);
        //删除当前行
        //删除当前行的数据
//        data.remove(position-1);
//        //显示列表
//        lv_main.setAdapter(adapter);//显示列表, 不会使用缓存的item的视图对象
//        adapter.notifyDataSetChanged();//通知更新列表, 使用所有缓存的item的视图对象
        return true;
    }

    protected List<Order> getAllOrderInfos() {
        List<Order> list = new ArrayList<>();
        OrderSqliteUtil dbUtil = new OrderSqliteUtil(this);
        dbUtil.open();
        list = dbUtil.fetchByisPrintDESCOnlyPrint(0); //取未删除的
        dbUtil.close();
        return list;
    }

    class OrderAdapter extends BaseAdapter {

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
                convertView = View.inflate(HistoryActivity.this, R.layout.item_order_history, null);
            }
            //2. 得到当前行数据对象
            Order appInfo = data.get(position);
            //3. 得到当前行需要更新的子View对象
            TextView textView = (TextView) convertView.findViewById(R.id.txt_history_order);
            //4. 给视图设置数据
            String payment = (appInfo.getHasPay() - 0.0 < 1e-10) ? "未支付" : "已支付";
            System.out.println(appInfo.getHasPay() + "---------pay");
            textView.setText(appInfo.getTime().split(" ")[0] + "\t\t\t" + appInfo.getConsumerName() + "\t\t\t" + appInfo.getConsumerPhone() + "\t\t" + payment);
            //返回convertView
            return convertView;
        }

    }

}
