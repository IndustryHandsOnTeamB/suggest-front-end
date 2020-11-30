package com.example.handson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MbtiListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private int titleLayout = 0;
    private int itemLayout = 0;
    private String mbtiListItem;
    private LayoutInflater inflater = null;

    public MbtiListViewAdapter(Context context, int titleLayout, int itemLayout, String mbtiListItem){
        this.mbtiListItem = mbtiListItem;
        this.context = context;
        this.titleLayout = titleLayout;
        this.itemLayout = itemLayout;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mbtiListItem;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(this.titleLayout, parent, false);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(this.itemLayout, parent, false);
        }
        TextView mbtiItemText = (TextView)convertView.findViewById(R.id.text_mbti_job_list);
        mbtiItemText.setText(mbtiListItem);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
