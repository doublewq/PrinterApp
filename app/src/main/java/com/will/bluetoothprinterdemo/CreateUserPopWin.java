package com.will.bluetoothprinterdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.will.bluetoothprinterdemo.utils.SqliteProductUtil;
import com.will.bluetoothprinterdemo.vo.ProductSingle;

import java.util.List;


public class CreateUserPopWin extends PopupWindow {
    private Context mContext;

    private View view;
    public EditText dataNumEdit;
    //获取数据的id
    public Spinner spinColor;
    public EditText editPrice;
    private Button btn_save_pop;

    // 实现二级联动
    public Spinner spinCate;
    public Spinner spinName;
    private String[] province = {"枕头", "小米枕", "荞麦枕", "枕套", "床垫", "被子", "压缩枕", "凉枕", "夏凉被"};

    //二维数组用于根据省的选择来相应的显示城市
    private String[][] citys = {{"棉小定", "棉中定", "学生决明子", "棉特大", "决明子枕", "决明子小半圆", "麦穗决明子", "泡泡小磁铁", "决明子理疗枕", "普通磁疗枕", "针织水洗枕", "热熔水洗枕", "彩格水洗枕", "婚庆水洗枕", "绗缝磁疗枕", "浪琴决明子", "粗网决明子", "百年好合泡泡枕", "负离子养生枕", "皇家玉石枕", "麦饭石枕", "枕芯包装2", "枕芯包装3"},
            {"花边小米", "棉小米", "精品小米枕", "高档小米枕", "水晶绒小米枕", "花边小米（大号）", "泡沙小米枕"},
            {"花边荞麦", "棉荞麦", "精品荞麦", "小号定型荞麦", "中号定型荞麦", "大号定型荞麦", "儿童粗荞麦", "方枕荞麦", "卡通荞麦枕", "超柔儿童枕", "卡通枕"},
            {"全棉枕套", "磨毛枕套", "枕套（小号）", "枕套（大号）"},
            {"90方格床垫", "1.2方格床垫", "1.5方格床垫", "1.8方格床垫", "90学生床垫", "90兰格褥", "90白毛床垫", "1.2白毛床垫", "1.5白毛床垫", "1.8白毛床垫", "90羊羔毛床垫", "1.2羊羔毛床垫", "1.5羊羔毛床垫", "1.8羊羔毛床垫", "1.5水晶绒床垫", "1.8水晶绒床垫"},
            {"1.5钻石绒被", "1.8钻石绒被", "2m钻石绒被", "1.5亲肤绒被", "1.8亲肤绒被", "2m亲肤绒被", "1.5充绒被", "1.8充绒被", "2m充绒被", "1.5中印", "1.8大印", "1.5棉加丝", "1.8棉加丝", "2m水洗绣花被", "2m大红绣花", "1.5海藻棉", "2m海藻棉", "被子包装4", "被子包装5"},
            {"小双面", "大双面", "经典养生枕", "羽丝绒枕", "50芯", "60芯", "水鸟枕"},
            {"竹子学生枕","小麻将","竹子大号","中号麻将枕","大号麻将枕","精品大麻将","蕾丝大麻将","蕾丝滕枕","麻将花边枕","蝴蝶透气麻将枕","特大号麻将枕","麻将护颈枕","麻将颈椎枕","麻将糖果枕","节节高麻将枕","胡桃香枕","印尼绣花滕枕","绣花滕枕","麻将紫花边"},
            {"1.5钻石绒夏被","1.8钻石绒夏被","2米钻石绒夏被","1.5水洗棉夏被","1.8水洗棉夏被","2米水洗棉夏被","1.5纳米夏被","1.8纳米夏被","2米纳米夏被","1.5海岛棉夏被","2米海岛棉夏被","1.5珠光夏被","1.8珠光夏被","2米珠光夏被","1.5水洗棉绗綉夏被","2米水洗棉绗綉夏被","1.5春秋被","1.8春秋被","2米春秋被","夏被包装"}};
    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;

    private List<ProductSingle> listOfProductSingle;

    public String[][] getcitylist(final Activity mContext) {
        SqliteProductUtil dbUtil = new SqliteProductUtil(mContext);
        dbUtil.open();
        listOfProductSingle = dbUtil.fetchAll();
        List<ProductSingle> productLists = null;
        String[][] result = new String[9][];
        for (int i = 1; i <= 9; i++) {
            productLists = dbUtil.fetchByCID(i);
            result[i - 1] = new String[productLists.size()];
            for (int j = 0; j < productLists.size(); j++) {
                result[i - 1][j] = productLists.get(j).getpName();
            }
        }
        dbUtil.close();
        return result;
    }

    @SuppressLint("WrongViewCast")
    public CreateUserPopWin(final Activity mContext, View.OnClickListener itemsOnClick, String result) {
        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.popuwindow, null);
//        this.view.setAlpha(0.9f);
        btn_save_pop = (Button) view.findViewById(R.id.isOk);
        // 设置按钮监听
        btn_save_pop.setOnClickListener(itemsOnClick);

        spinCate = (Spinner) view.findViewById(R.id.spinnerCategory);
        spinName = (Spinner) view.findViewById(R.id.spinnerName);
        spinColor = (Spinner) view.findViewById(R.id.spinnerColor);
        editPrice = (EditText) view.findViewById(R.id.etPrice);
        dataNumEdit = (EditText) view.findViewById(R.id.dataNumEdit);

        citys = getcitylist(mContext);
        //省的适配器绑定
        provinceAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, province);
        spinCate.setAdapter(provinceAdapter);
        //市的适配器绑定
        cityAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
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
//                System.out.println("kingwen我被执行了" + proName + "postition" + position);
                String proprice = getPrice(kind, proName, mContext);
                editPrice.setText(proprice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //初始化下拉列表数据 --商品名称
//        String[] ctype_name = new String[]{"决明子明目枕", "浪琴决明子枕", "中号定型荞麦", "1.5羊羔毛床垫", "花边小米单价", "浪琴决明子枕", "中号定型荞麦", "1.5羊羔毛床垫"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, ctype_name);  //创建一个数组适配器
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
//        Spinner spinnerName = (Spinner) view.findViewById(R.id.spinnerName);
//        spinnerName.setAdapter(adapter);

        // 商品颜色
        String[] ctype_color = new String[]{"配色", "花色", "白色", "红色", "灰色", "蓝色", "卡色"};
        ArrayAdapter<String> adapte_color = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, ctype_color);  //创建一个数组适配器
        adapte_color.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        Spinner spinner_color = (Spinner) view.findViewById(R.id.spinnerColor);
        spinner_color.setAdapter(adapte_color);

        //根据result来判断是否是编辑内容
        if (result != null && result.length() != 0) {
            try {
                int catePosition = 0;
                int namePosition = 0;
                int colorPosition = 0;
                String[] productLists = result.split("\\t");

                for (ProductSingle producSinge : listOfProductSingle) {
                    if (producSinge.getpName().equals(productLists[1])) {
                        catePosition = producSinge.getP_CID() - 1;
                    }
                }

                for (int i = 0; i < citys[catePosition].length; i++) {
                    if (productLists[1].equals(citys[catePosition][i])) {
                        namePosition = i;
                    }
                }
                for (int i = 0; i < ctype_color.length; i++) {
                    if (productLists[2].equals(ctype_color[i])) {
                        colorPosition = i;
                    }
                }
                spinCate.setSelection(catePosition, true);
                spinName.setAdapter(cityAdapter);
                cityAdapter.notifyDataSetChanged();
                spinName.setSelection(namePosition, true);

                spinColor.setSelection(colorPosition);
                dataNumEdit.setText(productLists[3]);
                editPrice.setText(productLists[4].substring(0, productLists[4].length() - 1));

                spinName.setAdapter(cityAdapter);
                cityAdapter.notifyDataSetChanged();
                spinName.setSelection(namePosition, true);
            } catch (Exception e) {

            }

        }

        final WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);

        // 设置弹出窗体的宽和高
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = mContext.getWindow();

        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值

        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth((int) (d.getWidth() * 0.8));
        setBackgroundDrawable(new BitmapDrawable());

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 设置外部可点击
        this.setOutsideTouchable(true);

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
                lp.alpha = 1.0f;
                mContext.getWindow().setAttributes(lp);
            }

        });
    }

    private String getPrice(String kind, String proName, final Activity mContext) {
        // 找类别编号
        int CID = 0;
        for (int i = 0; i < province.length; i++) {
            if (kind.equals(province[i])) {
                CID = i + 1;
            }
        }
        // 找price;
        Double proPrice = 0.0;
        SqliteProductUtil dbUtil = new SqliteProductUtil(mContext);
        dbUtil.open();
        List<ProductSingle> productSingleList = dbUtil.fetchByCID(CID);
        for (ProductSingle pro : productSingleList) {
            if (pro.getpName().equals(proName)) {
                proPrice = pro.getpPrice();
            }
        }
        return String.valueOf(proPrice);
    }
}
