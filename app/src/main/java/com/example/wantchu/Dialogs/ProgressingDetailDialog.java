package com.example.wantchu.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.wantchu.JsonParsingHelper.OrderProgressingParsingHelper;
import com.example.wantchu.R;
import com.google.gson.Gson;

public class ProgressingDetailDialog extends DialogFragment {
    private Context context;

    TextView store_name;
    TextView totals;
    Gson gson;
    public static ProgressingDetailDialog newInstance(Context context){
        Bundle bundle = new Bundle();
        ProgressingDetailDialog fragment = new ProgressingDetailDialog();
        fragment.context = context;
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());

        final int height  = ActionBar.LayoutParams.WRAP_CONTENT;


        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View progressingDetail = getActivity().getLayoutInflater().inflate(R.layout.fragment_progressing_detail, null);

        String json = getArguments().getString("json");
        gson = new Gson();
        OrderProgressingParsingHelper data = gson.fromJson(json,OrderProgressingParsingHelper.class);

        //////////////////////////////
        store_name = progressingDetail.findViewById(R.id.store_name);
        totals = progressingDetail.findViewById(R.id.totals);
        /////////////////////////////
        builder.setView(progressingDetail);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
