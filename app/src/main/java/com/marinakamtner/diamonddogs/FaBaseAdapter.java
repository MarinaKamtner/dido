package com.marinakamtner.diamonddogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by marina on 26.06.15.
 * FinanzamtBaseAdapter
 */
public class FaBaseAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Finanzamt> finanzamtList;

    public FaBaseAdapter(Context context, List<Finanzamt> finanzaemter) {
        mInflater = LayoutInflater.from(context);
        finanzamtList = finanzaemter;
    }

    @Override
    public int getCount() {
        return finanzamtList.size();
    }

    @Override
    public Object getItem(int position) {
        return finanzamtList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView)view.findViewById(R.id.name);
            holder.address = (TextView)view.findViewById(R.id.address);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Finanzamt finanzamt = finanzamtList.get(position);
        holder.name.setText(finanzamt.getName());
        holder.address.setText(finanzamt.getPostcode() + " "+ finanzamt.getLocation() + ", "+finanzamt.getStreet());

        return view;
    }

    private class ViewHolder {
        public TextView name, address;
    }

}
