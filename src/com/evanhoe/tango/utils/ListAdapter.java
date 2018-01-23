package com.evanhoe.tango.utils;
import java.util.ArrayList;
import java.util.TreeSet;

import com.evanhoe.tango.R;
import com.evanhoe.tango.objs.WorkOrderViewEntryItem;
import com.evanhoe.tango.objs.WorkOrderViewItem;
import com.evanhoe.tango.objs.WorkOrderViewSectionItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList items;
    private LayoutInflater vi;
    public ListAdapter(Context context,ArrayList items) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final WorkOrderViewItem i = (WorkOrderViewItem) items.get(position);
        if (i != null) {
            if(i.isSection()){
                WorkOrderViewSectionItem si = (WorkOrderViewSectionItem)i;
                v = vi.inflate(R.layout.work_order_list_section, null);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
                final TextView sectionView =
                    (TextView) v.findViewById(R.id.textSeparator);
                sectionView.setText(si.getTitle());
            } else {
                WorkOrderViewEntryItem ei = (WorkOrderViewEntryItem)i;
                v = vi.inflate(R.layout.work_order_list_row, null);
                final TextView title =
                    (TextView)v.findViewById(R.id.workOrderNumber);
                final TextView subtitle =
                    (TextView)v.findViewById(R.id.workOrderName);
                final ImageView rowIcon = 
                		(ImageView)v.findViewById(R.id.rowIcon);
                
                if (rowIcon != null) 
                	{
                		if (ei.status.equals("IN PROGRESS")) 
                			{
                				rowIcon.setImageResource(R.drawable.ic_dot_inprogress);
                			}
                		else if (ei.status.equals("ON HOLD"))
                		{
                			rowIcon.setImageResource(R.drawable.ic_dot_onhold);
                		}
                		else
                		{
                			rowIcon.setImageResource(R.drawable.circle_bullet_blank);
                		}
                	}
                
                if (title != null) title.setText(ei.title);
                if(subtitle != null) subtitle.setText(ei.subtitle);
            
//enter code to update image for green red or no light                
            }
        }
        return v;
    }

}
