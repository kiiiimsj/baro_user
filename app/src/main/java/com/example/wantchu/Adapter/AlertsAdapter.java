package com.example.wantchu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.AdapterHelper.AlertsHelperClass;
import com.example.wantchu.JsonParsingHelper.AlertIsNewParsing;
import com.example.wantchu.R;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.AlertsViewHolder> {
    Context context;
    AlertsHelperClass alertsHelperClass;
    public AlertsAdapter(Context context, AlertsHelperClass alertsHelperClass) {
        this.context = context;
        this.alertsHelperClass = alertsHelperClass;
    }
    @NonNull
    @Override
    public AlertsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.alert, parent, false);
        AlertsViewHolder alertsViewHolder = new AlertsViewHolder(view, viewType);
        return alertsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlertsViewHolder holder, int position) {

//        AlertsHelperClass.AlertsHelperClassParsing alertsHelperClassParsing =alertsHelperClass.alert.get(position);
        AlertsHelperClass.AlertsHelperClassParsing reverse =alertsHelperClass.alert.get(alertsHelperClass.alert.size()-position-1);
        if(holder == null || reverse == null) {
            return;
        }
        holder.alertTitle.setText(reverse.alert_title);
        holder.alertContent.setText(reverse.alert_content);
        holder.alertStartDate.setText(reverse.alert_startdate);
        holder.alertId.setText(reverse.alert_id+"");
    }

    @Override
    public int getItemCount() {
        return alertsHelperClass == null ? 0 : alertsHelperClass.alert.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class AlertsViewHolder extends RecyclerView.ViewHolder {
        TextView alertTitle;
        TextView alertContent;
        TextView alertStartDate;
        TextView alertId;
        public AlertsViewHolder(@NonNull View itemView, int pos) {
            super(itemView);
            alertTitle = itemView.findViewById(R.id.alert_title);
            alertContent = itemView.findViewById(R.id.alert_content);
            alertStartDate = itemView.findViewById(R.id.alert_start_date);
            alertId = itemView.findViewById(R.id.alert_id);
        }
    }
}
