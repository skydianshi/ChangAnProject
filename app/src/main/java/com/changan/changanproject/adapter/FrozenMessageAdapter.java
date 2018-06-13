package com.changan.changanproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changan.changanproject.R;
import com.changan.changanproject.bean.DtcMessage;

import java.util.List;

/**
 * Created by 张海逢 on 2017/12/14.
 */

public class FrozenMessageAdapter extends BaseAdapter{
    private List<String> list;
    private LayoutInflater inflater;
    Context context;

    public FrozenMessageAdapter(Context context, List<String> list){
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(list!=null){
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final String frozenMessage = (String)this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.frozenmessage_item, null);
            viewHolder.indexTextView = (TextView) convertView.findViewById(R.id.index);
            viewHolder.fmTextView = (TextView) convertView.findViewById(R.id.frozenMessage);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.indexTextView.setText(String.valueOf(position+1));
        viewHolder.fmTextView.setText(frozenMessage);


        viewHolder.indexTextView.setTextSize(13);
        viewHolder.fmTextView.setTextSize(13);

        return convertView;
    }

    public static class ViewHolder{
        public TextView indexTextView;
        public TextView fmTextView;
    }

}