package com.example.wantchu.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.wantchu.AdapterHelper.MyPageListbuttons;
import com.example.wantchu.R;

import java.util.ArrayList;

public class MyPageExpandAdapter extends BaseExpandableListAdapter {
    private Context context;
    private int groupLayout = 0;
    private int childLayout = 0;
    private ArrayList<MyPageListbuttons> buttons;
    private LayoutInflater layoutInflater = null;

    public MyPageExpandAdapter(Context context, int groupLayout,int childLayout, ArrayList<MyPageListbuttons> buttons) {
        this.context = context;
        this.groupLayout = groupLayout;
        this.childLayout = childLayout;
        this.buttons = buttons;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        Log.i("GETCOUNT", buttons.size()+"");
        return buttons.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return buttons.get(groupPosition).childText.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return buttons.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return buttons.get(groupPosition).childText.get(childPosition);
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
            convertView = layoutInflater.inflate(this.groupLayout, parent, false);
        }
        TextView groupName = (TextView)convertView.findViewById(R.id.my_page_expandable);
        TextView setArrow = (TextView)convertView.findViewById(R.id.set_arrow);

        Log.i("GETGROUPNAME ", buttons.get(groupPosition).parentName);

        groupName.setText(buttons.get(groupPosition).parentName);
        if(buttons.get(groupPosition).childText.size() == 0) {
            return convertView;
        }
        if(isExpanded) {
            setArrow.setText(Html.fromHtml("&and;"));
            setArrow.setTextSize(10);
            return convertView;
        }
        setArrow.setText(Html.fromHtml("&or;"));
        setArrow.setTextSize(10);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = layoutInflater.inflate(this.childLayout, parent, false);
        }

        TextView childText = (TextView)convertView.findViewById(R.id.child_text);
        childText.setText(buttons.get(groupPosition).childText.get(childPosition));
        childText.setLeft(10);
        childText.setTextSize(13);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
