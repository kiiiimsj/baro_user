package com.tpn.baro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tpn.baro.AdapterHelper.AlertsHelperClass;
import com.tpn.baro.R;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.AlertsViewHolder> {
    public interface ClickAlert {
        void clickAlertListener(int alertId, int position);
    }

    Context context;
    AlertsHelperClass alertsHelperClass;
    ClickAlert clickAlert;

    public AlertsAdapter(Context context, AlertsHelperClass alertsHelperClass, ClickAlert clickAlert) {
        this.context = context;
        this.alertsHelperClass = alertsHelperClass;
        this.clickAlert = clickAlert;
    }


    @NonNull
    @Override
    public AlertsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.design_alert, parent, false);
        AlertsViewHolder alertsViewHolder = new AlertsViewHolder(view, viewType);
        return alertsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlertsViewHolder holder, int position) {
        AlertsHelperClass.AlertsHelperClassParsing reverse =alertsHelperClass.alert.get(alertsHelperClass.alert.size()-position-1);
        if(holder == null || reverse == null) {
            return;
        }
        if(reverse.is_read.equals("N")) {
            holder.isRead.setVisibility(View.VISIBLE);
        }
        else if(reverse.is_read.equals("Y")){
            holder.isRead.setVisibility(View.INVISIBLE);
        }

        holder.alertTitle.setText(reverse.alert_title);
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
        TextView alertStartDate;
        TextView alertId;
        RelativeLayout isRead;
        View clickListener;
        public AlertsViewHolder(@NonNull View itemView, final int pos) {
            super(itemView);
            alertTitle = itemView.findViewById(R.id.alert_title);
            alertStartDate = itemView.findViewById(R.id.alert_start_date);
            alertId = itemView.findViewById(R.id.alert_id);
            isRead = itemView.findViewById(R.id.is_read_new_label);
            clickListener = itemView.findViewById(R.id.click_listener);
            final AlertsHelperClass.AlertsHelperClassParsing alertsHelperClassParsing = alertsHelperClass.alert.get(alertsHelperClass.alert.size()-pos-1);
            clickListener.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAlert.clickAlertListener(alertsHelperClassParsing.alert_id, alertsHelperClass.alert.size()-pos-1);
                }
            });
        }
    }
}
