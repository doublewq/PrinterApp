package com.will.bluetoothprinterdemo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.will.bluetoothprinterdemo.ui.BasePrintActivity;
import com.will.bluetoothprinterdemo.utils.BluetoothUtil;
import com.will.bluetoothprinterdemo.utils.OrderSqliteUtil;
import com.will.bluetoothprinterdemo.utils.PrintUtil;
import com.will.bluetoothprinterdemo.utils.ProductSqliteUtil;
import com.will.bluetoothprinterdemo.vo.Model;
import com.will.bluetoothprinterdemo.vo.Order;
import com.will.bluetoothprinterdemo.vo.Product;

import java.util.ArrayList;
import java.util.List;

public class NoPrintActivity extends BasePrintActivity {

    final static int TASK_TYPE_PRINT = 2;

    private int printNumber = 1;
    private Button btnPrint;
    private Button btnPrintTWO;
    private Button btnPrintTHREE;
    private ListView mListView;
    private List<Model> models;
    private CheckBox mMainCkb;
    private MyAdapter mMyAdapter;
    //监听来源
    public boolean mIsFromItem = false;

    private List<Order> orderLists;

    @Override
    public void onConnected(BluetoothSocket socket, int taskType) {
        switch (taskType) {
            case TASK_TYPE_PRINT:
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.erweima);
                for (int k = 0; k < printNumber; k++) {
                    for (int i = 0; i < orderLists.size(); i++) {
                        Order printOrder = orderLists.get(i);
                        List<Product> productLists = getProductListsFromDB(printOrder.getOrderID());
                        if (productLists.size() > 0) {
                            System.out.println(productLists.get(0).toString());
                        }
                        PrintUtil.printOrder(socket, bitmap, printOrder, productLists);
                        //打印完更新订单状态为已打印状态
//                    deleteFromDB(printOrder.getOrderID());
                    }
//                mMyAdapter.notifyDataSetChanged();
                }

                break;
        }
    }

    private void deleteFromDB(String orderID) {
        OrderSqliteUtil dbUtil = new OrderSqliteUtil(this);
        dbUtil.open();
        dbUtil.updateToHasPrint(orderID);
        dbUtil.close();
    }

    private List<Product> getProductListsFromDB(String orderID) {
        ProductSqliteUtil dbUtil = new ProductSqliteUtil(this);
        dbUtil.open();
        List<Product> products = dbUtil.fetchByOrderID(orderID);
        dbUtil.close();
        return products;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_print);
        initView();
        initData();
        initViewOper();
        setListeners();
    }

    private void setListeners() {
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printNumber = 1;
                StringBuilder sb = new StringBuilder();
                orderLists = new ArrayList<>();
                if (models != null && models.size() > 0) {
                    for (int i = 0; i < models.size(); i++) {
                        if (models.get(i).isIscheck()) {
                            orderLists.add(models.get(i).getOrder());
//                            sb.append(models.get(i).getOrder().getConsumerName() + ",");
                        }
                    }
                }
                if (orderLists != null && orderLists.size() > 0) {
                    // 连接打印机 开始打印喽！
                    try {
                        connectDevice(TASK_TYPE_PRINT);
                    } catch (Exception e) {
                        Toast.makeText(NoPrintActivity.this, "打印机连接异常", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NoPrintActivity.this, "请选择要打印的订单", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPrintTWO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printNumber = 2;
                StringBuilder sb = new StringBuilder();
                orderLists = new ArrayList<>();
                if (models != null && models.size() > 0) {
                    for (int i = 0; i < models.size(); i++) {
                        if (models.get(i).isIscheck()) {
                            orderLists.add(models.get(i).getOrder());
//                            sb.append(models.get(i).getOrder().getConsumerName() + ",");
                        }
                    }
                }
                if (orderLists != null && orderLists.size() > 0) {
                    // 连接打印机 开始打印喽！
                    try {
                        connectDevice(TASK_TYPE_PRINT);
                    } catch (Exception e) {
                        Toast.makeText(NoPrintActivity.this, "打印机连接异常", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NoPrintActivity.this, "请选择要打印的订单", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPrintTHREE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printNumber = 3;
                StringBuilder sb = new StringBuilder();
                orderLists = new ArrayList<>();
                if (models != null && models.size() > 0) {
                    for (int i = 0; i < models.size(); i++) {
                        if (models.get(i).isIscheck()) {
                            orderLists.add(models.get(i).getOrder());
//                            sb.append(models.get(i).getOrder().getConsumerName() + ",");
                        }
                    }
                }
                if (orderLists != null && orderLists.size() > 0) {
                    // 连接打印机 开始打印喽！
                    try {
                        connectDevice(TASK_TYPE_PRINT);
                    } catch (Exception e) {
                        Toast.makeText(NoPrintActivity.this, "打印机连接异常", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NoPrintActivity.this, "请选择要打印的订单", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void connectDevice(int taskType) {
        List<BluetoothDevice> devices = BluetoothUtil.getPairedPrinterDevices();
        if (devices.size() > 0) {
            BluetoothDevice device = devices.get(0);
            if (device != null)
                super.connectDevice(device, taskType);
        } else {
            Toast.makeText(this, "请前往打印机页面连接打印机", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * view初始化
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.list_hisOrder);
        mMainCkb = (CheckBox) findViewById(R.id.ckb_history);
        btnPrint = (Button) findViewById(R.id.btn_historyPrint);
        btnPrintTWO = (Button) findViewById(R.id.btn_historyPrintTWO);
        btnPrintTHREE = (Button) findViewById(R.id.btn_historyPrintTHREE);
    }

    /**
     * 数据加载
     */
    private void initData() {
        //从数据库中取未打印订单数据
        models = new ArrayList<>();
        OrderSqliteUtil dbUtil = new OrderSqliteUtil(this);
        dbUtil.open();
        List<Order> orders = dbUtil.fetchByisPrintAndDESC(0);  //0代表未打印
//        System.out.println("order+__________"+orders.size()+orders.get(0).getIsPrint());
        for (int i = 0; i < orders.size(); i++) {
            Model model = new Model();
            model.setOrder(orders.get(i));
            model.setIscheck(false);
            models.add(model);
        }
        dbUtil.close();
    }

    /**
     * 数据绑定
     */
    private void initViewOper() {
        mMyAdapter = new MyAdapter(models, this, new NoPrintActivity.AllCheckListener() {
            @Override
            public void onCheckedChanged(boolean b) {
                //根据不同的情况对maincheckbox做处理
                if (!b && !mMainCkb.isChecked()) {
                    return;
                } else if (!b && mMainCkb.isChecked()) {
                    mIsFromItem = true;
                    mMainCkb.setChecked(false);
                } else if (b) {
                    mIsFromItem = true;
                    mMainCkb.setChecked(true);
                }
            }
        });
        mListView.setAdapter(mMyAdapter);

        //全选的点击监听
        mMainCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //当监听来源为点击item改变maincbk状态时不在监听改变，防止死循环
                if (mIsFromItem) {
                    mIsFromItem = false;
//                    Log.e("mainCheckBox", "此时我不可以触发");
                    return;
                }
                //改变数据
                for (Model model : models) {
                    model.setIscheck(b);
                }
                //刷新listview
                mMyAdapter.notifyDataSetChanged();
            }
        });
    }

    //对item导致maincheckbox改变做监听
    interface AllCheckListener {
        void onCheckedChanged(boolean b);
    }
}
