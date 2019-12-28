package com.will.bluetoothprinterdemo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.will.bluetoothprinterdemo.ui.BasePrintActivity;
import com.will.bluetoothprinterdemo.utils.BluetoothUtil;
import com.will.bluetoothprinterdemo.utils.OrderSqliteUtil;
import com.will.bluetoothprinterdemo.utils.PrintUtil;
import com.will.bluetoothprinterdemo.utils.ProductSqliteUtil;
import com.will.bluetoothprinterdemo.vo.Order;
import com.will.bluetoothprinterdemo.vo.Product;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WriteDataActivity extends BasePrintActivity implements View.OnClickListener {

    final static int TASK_TYPE_PRINT = 2;

    private ScrollView scrollView;
    private LinearLayout llVipNumContainer;
    private LinearLayout llAddVipNum;
    private Button btnYes;
    private Button btnSoonPrint;
    private CreateUserPopWin createUserPopWin;
    private EditText editTime;
    private EditText editUser;
    private EditText editPhone;

    private List<Product> productLists;
    private Order printOrder;

    @Override
    public void onConnected(BluetoothSocket socket, int taskType) {
        switch (taskType) {
            case TASK_TYPE_PRINT:
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.erweima);
                PrintUtil.printOrder(socket, bitmap, printOrder, productLists);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.isCancel:
                System.out.println("is cancel");
                final View viewItemLast = llVipNumContainer.getChildAt(llVipNumContainer.getChildCount() - 1);
                llVipNumContainer.removeView(viewItemLast);
                createUserPopWin.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_data);
        initView();
        setListeners();
    }

    private void initView() {
        btnYes = (Button) findViewById(R.id.btn_yes);
        btnSoonPrint = (Button) findViewById(R.id.btn_soonPrint);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        llVipNumContainer = (LinearLayout) findViewById(R.id.ll_vip_num_container);
        llAddVipNum = (LinearLayout) findViewById(R.id.ll_add_vip_num);
        editTime = (EditText) findViewById(R.id.editTime);
        editUser = (EditText) findViewById(R.id.editUser);
        editPhone = (EditText) findViewById(R.id.editPhone);

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        editTime.setText(dateString);
    }

    public void showEditPopWin(WriteDataActivity view, String result) {
        createUserPopWin = new CreateUserPopWin(view, onClickListener, result);
        createUserPopWin.showAtLocation(findViewById(R.id.write_viewId), Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.isOk:
                    String name = createUserPopWin.spinName.getSelectedItem().toString().trim();
                    String color = createUserPopWin.spinColor.getSelectedItem().toString().trim();
                    String dataNumEdit = createUserPopWin.dataNumEdit.getText().toString().trim();
                    dataNumEdit = dataNumEdit.equals("") ? "1" : dataNumEdit;
                    String price = createUserPopWin.editPrice.getText().toString().trim();
                    price = price.startsWith(".") ? "1" : price; //处理不合法输入
                    price = price.equals("") ? "1" : price;

                    // 小计 小数点保留两位
                    double sumPricedoub = (Integer.valueOf(dataNumEdit) * Double.valueOf(price));
                    DecimalFormat formPrice = new DecimalFormat("#.00");
                    String sumPrice = formPrice.format(sumPricedoub);

                    final View viewItem = llVipNumContainer.getChildAt(llVipNumContainer.getChildCount() - 1);
                    TextView tvIndex = (TextView) viewItem.findViewById(R.id.et_vip_number);
                    tvIndex.setText("\t" + name + "\t" + color + "\t" + dataNumEdit + "\t" + price + "￥" + "\t" + "小计:" + sumPrice);
                    createUserPopWin.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void setListeners() {

        llAddVipNum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addViewItem();
                showEditPopWin(WriteDataActivity.this, "");
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                productLists = getDataList();
                if (productLists == null || productLists.size() <= 0) {
                    Toast.makeText(WriteDataActivity.this, "请添加要打印的商品!", Toast.LENGTH_SHORT).show();
                } else {
                    //迁移打印命令
                    //解决重复点击打印，order数据库中出现重复order
                    StoreTODB(productLists, 1); //先存储再打印，也可以哟
                    try {
                        connectDevice(TASK_TYPE_PRINT);
                    } catch (Exception e) { //打印异常
                        //更新为未打印状态
                        updateToNoPrintFromDB(productLists.get(0).getOrderId());
                    }
                }
            }
        });

        btnSoonPrint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<Product> list = getDataList();
                if (list == null || list.size() <= 0) {
                    Toast.makeText(WriteDataActivity.this, "尚未添加要打印的商品!", Toast.LENGTH_SHORT).show();
                } else {
                    // 存储进待打印列表
                    if (!orderDBHasThisOrder(list.get(0).getOrderId())) {
                        StoreTODB(list, 0);
                        Toast.makeText(WriteDataActivity.this, "已加入待打印队列!", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(WriteDataActivity.this, "请勿重复加入待打印队列!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 判断数据库中是否已存在这个订单
    private boolean orderDBHasThisOrder(String orderId) {
        OrderSqliteUtil dbUtil = new OrderSqliteUtil(this);
        dbUtil.open();
        int ans = dbUtil.fetch(orderId);
        dbUtil.close();
        System.out.println(ans);
        return ans == 0 ? false : true;
    }

    private void updateToNoPrintFromDB(String orderId) {
        OrderSqliteUtil dbUtil = new OrderSqliteUtil(this);
        dbUtil.open();
        dbUtil.updateToNoPrint(orderId);
        dbUtil.close();
    }

    // 当订单id不存在于order表时，才加入order中
    private void StoreTODB(List<Product> list, int isPrint) {
        if (list == null || list.size() == 0) {
            return;
        }
        //存储订单
        boolean flag_isExist = false;
        String orderID = list.get(0).getOrderId();
        String consumerName = editUser.getText().toString().trim().length() == 0 ? "VIP" : editUser.getText().toString();
        String consumberPhone = editPhone.getText().toString().trim() == "" ? "18888888888" : editPhone.getText().toString();
        int productNum = list.size();
        double salary = 0.0;
        for (int i = 0; i < list.size(); i++) {
            Product product = list.get(i);
            salary += (1.0 * product.getNumbers() * product.getPrice());
        }
        double pay = 0.0;
        String time = editTime.getText().toString();
        printOrder = new Order(0, orderID, consumerName, consumberPhone, productNum, salary, pay, time, isPrint);
        OrderSqliteUtil dbUtil = new OrderSqliteUtil(this);
        dbUtil.open();
        if (dbUtil.fetch(orderID) == 0) {
            dbUtil.insert(orderID, consumerName, consumberPhone, productNum, salary, pay, time, isPrint);
            flag_isExist = true;
        }
        dbUtil.close();

        if(!flag_isExist){
            //存储产品
            ProductSqliteUtil pdbUtil = new ProductSqliteUtil(this);
            pdbUtil.open();
            for (int i = 0; i < list.size(); i++) {
                Product product = list.get(i);
                System.out.println(product.toString());
                pdbUtil.insert(product.getOrderId(), product.getName(), product.getColor(), product.getNumbers(), product.getPrice());
            }
            pdbUtil.close();
        }
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
     * 添加item
     */
    private void addViewItem() {
        View viewItem = LayoutInflater.from(this).inflate(R.layout.item_add_vip_num, llVipNumContainer, false);
        llVipNumContainer.addView(viewItem);
        sortViewItem();
        //添加并且排序之后将布局滚动到底部，方便用户继续添加
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    /**
     * 该方法主要用于排序（每个item中的序号），主要针对从中间删除item的情况
     */
    private void sortViewItem() {
        for (int i = 0; i < llVipNumContainer.getChildCount(); i++) {
            final View viewItem = llVipNumContainer.getChildAt(i);
            TextView tvIndex = (TextView) viewItem.findViewById(R.id.tv_index);
            tvIndex.setText((i + 1) + "");
            LinearLayout llDelete = (LinearLayout) viewItem.findViewById(R.id.ll_delete);
            llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llVipNumContainer.removeView(viewItem);
                    sortViewItem();
                }
            });
            //编辑选中行
            final TextView tvItem = (TextView) viewItem.findViewById(R.id.et_vip_number);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llVipNumContainer.removeView(viewItem);
                    addViewItem();
                    showEditPopWin(WriteDataActivity.this, tvItem.getText().toString());
                }
            });
        }
    }

    /**
     * 生成固定长度随机码
     *
     * @param n 长度
     */
    private static long getRandom(long n) {
        long min = 1, max = 9;
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min;
        return rangeLong;
    }

    private List<Product> getDataList() {
        List<Product> result = new ArrayList<>();
        //订单编号
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderString = formatter.format(currentTime) + getRandom(4);

        for (int i = 0; i < llVipNumContainer.getChildCount(); i++) {
            View itemView = llVipNumContainer.getChildAt(i);
            TextView et = (TextView) itemView.findViewById(R.id.et_vip_number);
            if (et != null) {
                String productItem = et.getText().toString().trim();
                if (!TextUtils.isEmpty(productItem)) {
                    //解析出来商品
                    String[] products = productItem.split("\\t");
                    Product product = new Product();
                    product.setName(products[0]);
                    product.setColor(products[1]);
                    product.setNumbers(Integer.valueOf(products[2]));
                    product.setPrice(Double.valueOf(products[3].substring(0, products[3].length() - 1)));
                    product.setOrderId(orderString);
                    result.add(product);
                }
            }
        }
        return result;
    }


}
