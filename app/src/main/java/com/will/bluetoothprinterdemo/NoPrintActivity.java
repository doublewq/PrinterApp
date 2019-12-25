package com.will.bluetoothprinterdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.will.bluetoothprinterdemo.utils.OrderSqliteUtil;
import com.will.bluetoothprinterdemo.vo.Model;
import com.will.bluetoothprinterdemo.vo.Order;

import java.util.ArrayList;
import java.util.List;

public class NoPrintActivity extends AppCompatActivity {

    private Button btnPrint;
    private ListView mListView;
    private List<Model> models;
    private CheckBox mMainCkb;
    private MyAdapter mMyAdapter;
    //监听来源
    public boolean mIsFromItem = false;

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
                StringBuilder sb = new StringBuilder();
                List<Order> orderPrint = new ArrayList<>();
                if(models!=null && models.size()>0){
                    for (int i = 0; i < models.size(); i++) {
                        if(models.get(i).isIscheck()){
                            orderPrint.add(models.get(i).getOrder());
                            sb.append(models.get(i).getOrder().getConsumerName()+",");
                        }
                    }
                }
                //获取到了订单，接下来就是获取产品并打印了，将在writeDataActivity里面试验后迁移过来

                Toast.makeText(NoPrintActivity.this, "一会就打印喽！"+sb, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * view初始化
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.list_hisOrder);
        mMainCkb = (CheckBox) findViewById(R.id.ckb_history);
        btnPrint = (Button) findViewById(R.id.btn_historyPrint);
    }

    /**
     * 数据加载
     */
    private void initData() {
        //从数据库中取未打印订单数据
        models = new ArrayList<>();
        OrderSqliteUtil dbUtil = new OrderSqliteUtil(this);
        dbUtil.open();
        List<Order> orders = dbUtil.fetchByisPrint(0);
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
