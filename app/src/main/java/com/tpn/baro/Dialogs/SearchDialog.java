package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tpn.baro.ListStorePage;
import com.tpn.baro.R;

public class SearchDialog {

    private Context context;

    public SearchDialog(Context context){
        this.context = context;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.fragment_search);
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();

        final Button search = (Button) dlg.findViewById(R.id.search);
        final ImageButton close = (ImageButton) dlg.findViewById(R.id.close);
        final EditText body = (EditText) dlg.findViewById(R.id.searchBody);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = body.getText().toString();
                if(message.equals("")){
                    Toast.makeText(context,"키워드를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, ListStorePage.class);
                intent.putExtra("isSearchOrder", true);
                intent.putExtra("searchStore", message);
                intent.putExtra("list_type", "search");
                context.startActivity(intent);
                dlg.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
}
