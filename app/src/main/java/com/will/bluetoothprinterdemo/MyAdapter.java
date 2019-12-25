package com.will.bluetoothprinterdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.will.bluetoothprinterdemo.vo.Model;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Model> data;
    private Context context;
    private NoPrintActivity.AllCheckListener allCheckListener;

    public MyAdapter(List<Model> data, Context context, NoPrintActivity.AllCheckListener allCheckListener) {
        this.data = data;
        this.context = context;
        this.allCheckListener = allCheckListener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHoder hd;
        if (view == null) {
            hd = new ViewHoder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.item_order_history, null);
            hd.textView = (TextView) view.findViewById(R.id.txt_htitle);
            hd.checkBox = (CheckBox) view.findViewById(R.id.cxk_his);
            view.setTag(hd);
        }
        Model mModel = data.get(i);
        hd = (ViewHoder) view.getTag();
        hd.textView.setText(mModel.getOrder().getOrderID()+"\t 客户:"+mModel.getOrder().getConsumerName());

        final ViewHoder hdFinal = hd;
        hd.checkBox.setChecked(mModel.isIscheck());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = hdFinal.checkBox;
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    data.get(i).setIscheck(false);
                } else {
                    checkBox.setChecked(true);
                    data.get(i).setIscheck(true);
                }
                //监听每个item，若所有checkbox都为选中状态则更改main的全选checkbox状态
                for (Model model : data) {
                    if (!model.isIscheck()) {
                        allCheckListener.onCheckedChanged(false);
                        return;
                    }
                }
                allCheckListener.onCheckedChanged(true);
            }
        });
        return view;
    }

    class ViewHoder {
        TextView textView;
        CheckBox checkBox;
    }
}
