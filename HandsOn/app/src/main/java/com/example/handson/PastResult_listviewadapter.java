package com.example.handson;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PastResult_listviewadapter extends BaseAdapter {
    private ArrayList<PastResult_listview> listviewItemList = new ArrayList<PastResult_listview>();
    private Context context;

    public PastResult_listviewadapter(ArrayList<PastResult_listview> arrayList, Context context) {
        this.listviewItemList = arrayList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return listviewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listviewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_past_result, parent, false);
        }

        TextView dateTextView = (TextView)convertView.findViewById(R.id.textview_layout_pastresult_date);
        TextView typeTextView = (TextView)convertView.findViewById(R.id.textview_layout_pastresult_type);
        TextView resultTextView = (TextView)convertView.findViewById(R.id.textview_layout_pastresult_result);
        resultTextView.setEllipsize(TextUtils.TruncateAt.END);

        PastResult_listview listviewItem = listviewItemList.get(position);

        dateTextView.setText(listviewItem.getDate());
        typeTextView.setText(listviewItem.getTestType());
        resultTextView.setText(listviewItem.getResult());

        return convertView;
    }

    public void addItem(String date, String testType, String result) {
        PastResult_listview item = new PastResult_listview(date, testType, result);

        item.setDate(date);
        item.setTestType(testType);
        item.setResult(result);

        listviewItemList.add(item);
    }

    public void clear() {
        listviewItemList.clear();
    }
}