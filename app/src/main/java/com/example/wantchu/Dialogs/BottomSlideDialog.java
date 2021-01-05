package com.example.wantchu.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.wantchu.R;

public class BottomSlideDialog extends DialogFragment {
    public Context context;
    private View rootView;
    private TextView title;
    private TextView content;

    private Button bottomBtn;
    private Button topBtn;

    public BottomSlideDialog(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog builder = new Dialog(getActivity());
        rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_bottom_slide_dialog,null);

        title = rootView.findViewById(R.id.bottom_slide_dialog_title);
        content = rootView.findViewById(R.id.bottom_slide_dialog_content);
        bottomBtn = rootView.findViewById(R.id.bottom_slide_dialog_bottom_button);
        topBtn = rootView.findViewById(R.id.bottom_slide_dialog_top_button);

        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        builder.setContentView(rootView);
        builder.setCanceledOnTouchOutside(false);
        return builder;
    }
}
