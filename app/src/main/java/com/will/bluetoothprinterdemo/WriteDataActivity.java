package com.will.bluetoothprinterdemo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.will.bluetoothprinterdemo.ui.BasePrintActivity;
import com.will.bluetoothprinterdemo.utils.BluetoothUtil;
import com.will.bluetoothprinterdemo.utils.OrderSqliteUtil;
import com.will.bluetoothprinterdemo.utils.PrintUtil;
import com.will.bluetoothprinterdemo.utils.ProductSqliteUtil;
import com.will.bluetoothprinterdemo.utils.SqliteCustomerUtil;
import com.will.bluetoothprinterdemo.vo.Customer;
import com.will.bluetoothprinterdemo.vo.Order;
import com.will.bluetoothprinterdemo.vo.Product;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WriteDataActivity extends BasePrintActivity {

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
    private EditText editMoney;
    private EditText editBeizhu;

    private RadioGroup rg_pay;
    private String paymentMethod;

    private List<Product> productLists;
    private Order printOrder;
    private List<Customer> customersList;


    @Override
    public void onConnected(BluetoothSocket socket, int taskType) {
        switch (taskType) {
            case TASK_TYPE_PRINT:
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.erweima);
                PrintUtil.printOrder(socket, bitmap, printOrder, productLists);
                break;
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.isCancel:
//                System.out.println("is cancel");
//                final View viewItemLast = llVipNumContainer.getChildAt(llVipNumContainer.getChildCount() - 1);
//                llVipNumContainer.removeView(viewItemLast);
//                createUserPopWin.dismiss();
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_data);
        initView();
        setListeners();
        initRaidoGroup();
    }

    private int position = -1;
    private void initRaidoGroup() {
        rg_pay = (RadioGroup) findViewById(R.id.rg_horizontal_demo);
        for (int i = 0; i < rg_pay.getChildCount(); i++) {
            final RadioButton radioButton = (RadioButton) rg_pay.getChildAt(i);
            final int finalI = i;
            //监听RadioButton的点击事件
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rg_pay.clearCheck();
                    //记录选中按钮文字的变量
                    String result = "";
                    //判断记录的值是否和点击的索引值是否相同
                    if (position == finalI) {//相同：点击的是同一个RadioButton执行以下逻辑
                        radioButton.setChecked(false);//设置RadioButton选中状态为false
                        position = -1;//初始化position的值
                        result = "";//初始化result的值
                    } else {//不同：点击的是不同RadioButton
                        radioButton.setChecked(true);////设置RadioButton选中状态为true
                        position = finalI;//为position赋值
                        result = (String) radioButton.getText();//为result赋值
                    }
                    paymentMethod = result;
                }
            });
        }
//        rg_pay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton rb_temp = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
//                paymentMethod = rb_temp.getText().toString();
//                System.out.println(paymentMethod+"pay--------------");
////                Toast.makeText(WriteDataActivity.this, String.format("你选择了%s", rb_temp.getText().toString()), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void initView() {
//        btnYes = (Button) findViewById(R.id.btn_yes);
        btnSoonPrint = (Button) findViewById(R.id.btn_soonPrint);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        llVipNumContainer = (LinearLayout) findViewById(R.id.ll_vip_num_container);
        llAddVipNum = (LinearLayout) findViewById(R.id.ll_add_vip_num);
        editTime = (EditText) findViewById(R.id.editTime);
        editUser = (EditText) findViewById(R.id.editUser);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editMoney = (EditText) findViewById(R.id.editMoney);
        editBeizhu = (EditText) findViewById(R.id.editBeizhu);

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        editTime.setText(dateString);

        // 初始化客户姓名
        editUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showListPopulWindow(); //调用显示PopuWindow 函数
                }
            }
        });  //对edit 进行焦点监听
    }

    private void showListPopulWindow() {
//        final String[] list = {"汉城店", "大悟店", "谷城店", "竹溪店", "竹山店", "十堰店", "房县店", "岐山店", "镇原店",
//                "陇县店", "铜川店", "山阳店", "咸阳一店", "咸阳二店", "城固一店", "城固二店", "商洛店", "长安店",
//                "杨建群一店", "杨建群二店", "米脂衣世界", "三桥衣世界", "十里铺衣世界", "邓文斌衣世界", "贵阳荔波店", "勉县店(林希)",
//                "四川开江点(林希)", "贵州玉屏店(林希)", "衣佳汇二店(谭敏刚)", "柞水店(谭敏刚)", "陕西衣都汇1店", "陕西衣都汇2店", "陕西衣都汇3店", "陕西衣都汇5店"};//要填充的数据
        // 取得数据库中的客户姓名
        SqliteCustomerUtil dbUtil = new SqliteCustomerUtil(this);
        dbUtil.open();
        customersList = dbUtil.fetchAll();
        dbUtil.close();
        String[] resOfCus = new String[customersList.size()];
        int index = 0;
        for (Customer customer : customersList) {
            resOfCus[index++] = customer.getName();
        }
        final String[] list = resOfCus;

        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(editUser);//以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editUser.setText(list[i]);//把选择的选项内容展示在EditText上
                String afterPhone = "";
                String afterBeizhu = "";
                for(Customer customer : customersList){
                    if(list[i].equals(customer.getName())){
                        afterPhone = customer.getPhone();
                        afterBeizhu = customer.getBeizhu();
                    }
                }
                editPhone.setText(afterPhone);
                editBeizhu.setText(afterBeizhu);
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if(createUserPopWin!=null&&createUserPopWin.isShowing()){
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    public void showEditPopWin(WriteDataActivity view, String result) {
        createUserPopWin = new CreateUserPopWin(view, onClickListener, result);
        createUserPopWin.showAtLocation(findViewById(R.id.write_viewId), Gravity.CENTER, 0, 0);
    }

    private double currentMoney = 0.0;
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
                    if(name.length()>0 && dataNumEdit.length()>0){
                        tvIndex.setText("\t" + name + "\t" + color + "\t" + dataNumEdit + "\t" + price + "￥" + "\t" + "小计:" + sumPrice);
                        currentMoney += sumPricedoub;
                        editMoney.setText(formPrice.format(currentMoney));
                        createUserPopWin.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void showCoverDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确认开单？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    private void setListeners() {
        llAddVipNum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addViewItem();
                showEditPopWin(WriteDataActivity.this, "");
            }
        });

//        btnYes.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                productLists = getDataList();
//                if (productLists == null || productLists.size() <= 0) {
//                    Toast.makeText(WriteDataActivity.this, "请添加要打印的商品!", Toast.LENGTH_SHORT).show();
//                } else {
//                    //迁移打印命令
//                    //解决重复点击打印，order数据库中出现重复order
//                    if (!orderDBHasThisOrder(productLists.get(0).getOrderId())) {
//                        StoreTODB(productLists, 1); //先存储再打印，也可以哟
//                    }
//                    try {
//                        connectDevice(TASK_TYPE_PRINT);
//                    } catch (Exception e) { //打印异常
//                        //更新为未打印状态
//                        updateToNoPrintFromDB(productLists.get(0).getOrderId());
//                    }
//                }
//            }
//        });

        btnSoonPrint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                productLists = getDataList();
                if (productLists == null || productLists.size() <= 0) {
                    Toast.makeText(WriteDataActivity.this, "尚未添加要打印的商品!", Toast.LENGTH_SHORT).show();
                } else {
                    // 确认窗口
                    AlertDialog.Builder builder = new AlertDialog.Builder(WriteDataActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确认生成订单？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 存储进待打印列表
                            StoreTODB(productLists, 0);
                            // 跳转到订单页面
                            Intent intent = new Intent(WriteDataActivity.this, NoPrintActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
//                    if (!orderDBHasThisOrder(productLists.get(0).getOrderId())) {
//                        StoreTODB(productLists, 0);
//                        Toast.makeText(WriteDataActivity.this, "已加入待打印队列!", Toast.LENGTH_SHORT).show();
//                    }
//                    Toast.makeText(WriteDataActivity.this, "请勿重复加入待打印队列!", Toast.LENGTH_SHORT).show();
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
//        System.out.println(ans);
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
        String orderID = list.get(0).getOrderId();
        String consumerName = editUser.getText().toString().trim().length() == 0 ? "VIP" : editUser.getText().toString();
        String consumberPhone = editPhone.getText().toString().trim() == "" ? "***" : editPhone.getText().toString();
        String Beizhu = editBeizhu.getText().toString();
        consumberPhone+=" \t "+Beizhu;
        int productNum = 0;
        double salary = 0.0;
        for (int i = 0; i < list.size(); i++) {
            Product product = list.get(i);
            salary += (1.0 * product.getNumbers() * product.getPrice());
            productNum += product.getNumbers();
        }
        double pay = 0.0;  //已支付金额 1.0代表用微信支付完了
        if("微信".equals(paymentMethod)){
            pay = 1.0;
        }else if("支付宝".equals(paymentMethod)){
            pay = 2.0;
        }else if("现金".equals(paymentMethod)){
            pay = 3.0;
        }
        System.out.println(paymentMethod+"--------------payment----------"+pay);
        String time = editTime.getText().toString();
        printOrder = new Order(0, orderID, consumerName, consumberPhone, productNum, salary, pay, time, isPrint);
        OrderSqliteUtil dbUtil = new OrderSqliteUtil(this);
        dbUtil.open();
        if (dbUtil.fetch(orderID) == 0) {
            dbUtil.insert(orderID, consumerName, consumberPhone, productNum, salary, pay, time, isPrint);
        }
        dbUtil.close();

        //存储产品
        ProductSqliteUtil pdbUtil = new ProductSqliteUtil(this);
        pdbUtil.open();
        for (int i = 0; i < list.size(); i++) {
            Product product = list.get(i);
//            System.out.println(product.toString());
            pdbUtil.insert(product.getOrderId(), product.getName(), product.getColor(), product.getNumbers(), product.getPrice());
        }
        pdbUtil.close();

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
                    TextView textView = (TextView) viewItem.findViewById(R.id.et_vip_number);
                    if(textView.getText().toString().length()>0){
                        String[] products = textView.getText().toString().split("\\t");
                        double money = Double.valueOf(products[5].substring(3, products[5].length()));
                        currentMoney -= money;
                        DecimalFormat formPrice = new DecimalFormat("#.00");
                        editMoney.setText(formPrice.format(currentMoney));
                    }
                    sortViewItem();
                }
            });
            //编辑选中行
            final TextView tvItem = (TextView) viewItem.findViewById(R.id.et_vip_number);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llVipNumContainer.removeView(viewItem);
                    TextView textView = (TextView) v.findViewById(R.id.et_vip_number);
                    String bianjiContent = textView.getText().toString();
                    if(bianjiContent.length()>0){
                        String[] products = bianjiContent.split("\\t");
                        double money = Double.valueOf(products[5].substring(3, products[5].length()));
                        currentMoney -= money;
                        DecimalFormat formPrice = new DecimalFormat("#.00");
                        editMoney.setText(formPrice.format(currentMoney));
                        addViewItem();
                        showEditPopWin(WriteDataActivity.this, bianjiContent);
                        System.out.println("选中行编辑完毕");
                    }
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
