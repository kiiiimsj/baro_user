package com.example.wantchu.AdapterHelper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wantchu.OrderDetails;
import com.example.wantchu.R;

import java.util.HashMap;
import java.util.Iterator;

import static androidx.core.content.ContextCompat.getSystemService;

public class OrderDetailsEssentialTable {

    private TableLayout tableLayout;
    private HashMap<String,Integer> tableData;
    private LayoutInflater layoutInflater;
    private Context context;
    public OrderDetailsEssentialTable(Context context, ViewGroup viewGroups, HashMap<String, Integer> tableData) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.tableLayout = (TableLayout)layoutInflater.inflate(R.layout.activity_order_details_essential_table,viewGroups,false);
        this.tableData = tableData;
        this.context = context;
    }

    public TableLayout makeTable(){

        Iterator<String> keys = tableData.keySet().iterator();
        int count=1;
        TableRow tableRow;
        tableRow = (TableRow)layoutInflater.inflate(R.layout.activity_order_details_essential_table_child,tableLayout,false);
        tableLayout.addView(tableRow);
//        Log.i("afd","pass");
//        while(keys.hasNext()){
//            String optionName = keys.next();
//            int optionPrice = tableData.get(optionName);
//            LinearLayout optionElement =(LinearLayout)layoutInflater.inflate(R.layout.activity_order_details_essential_table_child2,tableRow,false);
//            tableRow.addView(optionElement);
//            if(count ==3) {
//                count = 0;
//                tableRow = new TableRow(context);
//                tableRow = (TableRow)layoutInflater.inflate(R.layout.activity_order_details_essential_table_child,tableLayout,false);
//                tableLayout.addView(tableRow);
//            }
//            count+=1;
//        }
        return tableLayout;
    }
}
