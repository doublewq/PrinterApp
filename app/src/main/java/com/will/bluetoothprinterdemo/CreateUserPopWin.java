package com.will.bluetoothprinterdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;


public class CreateUserPopWin extends PopupWindow {
    private Context mContext;

    private View view;
    public EditText dataNumEdit;
    //获取数据的id
    public Spinner spinName;
    public Spinner spinColor;
    public EditText editPrice;

    private Button btn_save_pop;

    @SuppressLint("WrongViewCast")
    public CreateUserPopWin(final Activity mContext, View.OnClickListener itemsOnClick, String result) {
        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.popuwindow, null);
//        this.view.setAlpha(0.9f);
        btn_save_pop = (Button) view.findViewById(R.id.isOk);
        // 设置按钮监听
        btn_save_pop.setOnClickListener(itemsOnClick);

        //初始化下拉列表数据 --商品名称
        String[] ctype_name = new String[]{"决明子明目枕", "浪琴决明子枕", "中号定型荞麦", "1.5羊羔毛床垫", "花边小米单价", "浪琴决明子枕", "中号定型荞麦", "1.5羊羔毛床垫"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, ctype_name);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        Spinner spinnerName = (Spinner) view.findViewById(R.id.spinnerName);
        spinnerName.setAdapter(adapter);

        // 商品颜色
        String[] ctype_color = new String[]{"白色", "花色", "黑色", "红色", "大红色"};
        ArrayAdapter<String> adapte_color = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, ctype_color);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        Spinner spinner_color = (Spinner) view.findViewById(R.id.spinnerColor);
        spinner_color.setAdapter(adapte_color);

        spinColor = (Spinner) view.findViewById(R.id.spinnerColor);
        spinName = (Spinner) view.findViewById(R.id.spinnerName);
        editPrice = (EditText) view.findViewById(R.id.etPrice);
        dataNumEdit = (EditText) view.findViewById(R.id.dataNumEdit);

        //根据result来判断是否是编辑内容
        if (result != null && result.length() != 0) {
            try {
                int namePosition = 0;
                int colorPosition = 0;
                String[] productLists = result.split("\\t");
                for (int i = 0; i < ctype_name.length; i++) {
                    if (productLists[1].equals(ctype_name[i])) {
                        namePosition = i;
                    }
                }
                for (int i = 0; i < ctype_color.length; i++) {
                    if (productLists[2].equals(ctype_color[i])) {
                        colorPosition = i;
                    }
                }
                spinName.setSelection(namePosition);
                spinColor.setSelection(colorPosition);
                dataNumEdit.setText(productLists[3]);
                editPrice.setText(productLists[4].substring(0, productLists[4].length() - 1));
            } catch (Exception e) {

            }

        }

        // 设置外部可点击
        this.setOutsideTouchable(false);
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
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
                lp.alpha = 1.0f;
                mContext.getWindow().setAttributes(lp);
            }

        });
    }
}
