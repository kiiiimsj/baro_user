package com.example.wantchu.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.AdapterHelper.TypeHelperClass;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {

    static private Context context;

    public interface OnListItemLongSelectedInterface{
        void onItemLongSelected(View v, int adapterPosition);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private static TypeAdapter.OnListItemSelectedInterface mListener;
    private static TypeAdapter.OnListItemLongSelectedInterface mLongListener;


    static ArrayList<TypeHelperClass> typeLocations;

    public TypeAdapter(ArrayList<TypeHelperClass> typeLocations, TypeAdapter.OnListItemSelectedInterface listener, TypeAdapter.OnListItemLongSelectedInterface longListener, Context context){
        this.typeLocations = typeLocations;
        this.mListener = listener;
        this.mLongListener = longListener;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public TypeAdapter.TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.type_design,parent, false);
        TypeViewHolder typeViewHolder = new TypeViewHolder(view, viewType);
        return typeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TypeAdapter.TypeViewHolder holder, int position) {
        TypeHelperClass typeHelperClass = typeLocations.get(position);
        holder.title.setText(typeHelperClass.typeName);
        holder.code.setText(typeHelperClass.typeCode);
        //holder.image.setBackgroundDrawable(Drawable.createFromPath(typeHelperClass.typeImage));

    }

    @Override
    public int getItemCount() {
        return typeLocations == null ? 0 : typeLocations.size();
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull TypeAdapter.TypeViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TypeAdapter.TypeViewHolder  holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull TypeAdapter.TypeViewHolder  holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TypeAdapter.TypeViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class TypeViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView code;
        public ImageView image;
        public LinearLayout type_design_back;
        public TypeViewHolder(@NonNull View itemView, int po) {
            super(itemView);

            title = itemView.findViewById(R.id.type_title);
            code = itemView.findViewById(R.id.type_code);
            image = itemView.findViewById(R.id.type_image);
            type_design_back = itemView.findViewById(R.id.type_design_back);
            TypeHelperClass typeHelperClass = typeLocations.get(po);

            makeRequest(typeHelperClass.typeImage, image, context);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemLongSelected(v, getAdapterPosition());
                    return false;
                }
            });

        }

        public void makeRequest(String type_image, final ImageView image, Context context) {
            String lastUrl = "ImageType.do?image_name=";
            UrlMaker urlMaker = new UrlMaker();
            String url = urlMaker.UrlMake(lastUrl);
            StringBuilder urlBuilder = new StringBuilder()
                    .append(url)
                    .append(type_image);
            Log.i("url", urlBuilder.toString());
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            ImageRequest request = new ImageRequest(urlBuilder.toString(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            Log.i("response", "response succeeded.");
                            image.setImageBitmap(response);
                        }
                    }, 100, 100, ImageView.ScaleType.FIT_CENTER, null,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", "error");
                        }
                    });
            requestQueue.add(request);
        }
    }
}
