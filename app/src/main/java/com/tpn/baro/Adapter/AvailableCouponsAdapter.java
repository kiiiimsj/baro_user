package com.tpn.baro.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tpn.baro.JsonParsingHelper.AvailableCouponsParsingHelper;
import com.tpn.baro.R;

import java.util.ArrayList;

public class AvailableCouponsAdapter extends RecyclerView.Adapter<AvailableCouponsAdapter.ViewHolder> {
    private OnItemClickListener mListener;
    private ArrayList<AvailableCouponsParsingHelper> coupons;
    private Context context;
    private ArrayList<RelativeLayout> arrayList;
    private TextView realTotal;
    private TextView totalPriceText;
    private TextView discountMoney;
    private int discountTotal;
    private int dc;
    private int coupon_id;
    public AvailableCouponsAdapter(ArrayList<AvailableCouponsParsingHelper> coupons, TextView realTotal, TextView totalPriceText, TextView discountMoney) {
        this.coupons = coupons;
        this.realTotal = realTotal;
        this.totalPriceText = totalPriceText;
        this.discountMoney = discountMoney;
        arrayList = new ArrayList<>();
        discountTotal = Integer.parseInt(totalPriceText.getText().toString());
    }

    public int getDiscountTotal(){
        return discountTotal;
    }

    public int getDc() {
        if(discountTotal==Integer.parseInt(totalPriceText.getText().toString())){
            dc = -1;
        }
        return dc;
    }

    public int getCoupon_id() {
        if(discountTotal==Integer.parseInt(totalPriceText.getText().toString())){
            coupon_id = -1;
        }
        return coupon_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_dialog_coupon_child,parent,false);
        ViewHolder viewHolder = new AvailableCouponsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AvailableCouponsParsingHelper now = coupons.get(position);
        String coupon_title = now.getCoupon_title();
        String coupon_end_date = now.getCoupon_enddate();
        final int coupon_discount_amount = now.getCoupon_discount();
        String coupon_type = now.getCoupon_type();

        holder.couponTitle.setText(coupon_title);
        holder.couponEnd.setText(coupon_end_date);
        holder.discountAmount.setText(""+coupon_discount_amount);
        if(coupon_type.equals(AvailableCouponsParsingHelper.SALE)){
            holder.couponType.setText("%");
        }else if(coupon_type.equals(AvailableCouponsParsingHelper.DISCOUNT)){
            holder.couponType.setText("원");
        }else{
            ;
        }
        arrayList.add(holder.couponShell);
        holder.couponShell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total = Integer.parseInt(totalPriceText.getText().toString());
                if(coupon_id == now.getCoupon_id()){
                    holder.couponShell.setBackgroundColor(context.getResources().getColor(R.color.white));
                    coupon_id=0;
                    int discount = 0;
                    dc = discount;
                    discountMoney.setText(discount+"");
                    discountTotal = total-discount;
                    realTotal.setText(""+discountTotal);
                    return;
                }else{
                    for(int i = 0;i<arrayList.size();i++){
                        Paint paint = new Paint();
                        paint.setColor(context.getResources().getColor(R.color.white));
                        arrayList.get(i).setBackgroundColor(paint.getColor());

                        paint.setColor(context.getResources().getColor(R.color.nearWhite));
                        holder.couponShell.setBackgroundColor(paint.getColor());
                        coupon_id = now.getCoupon_id();

                        if(now.getCoupon_type().equals(AvailableCouponsParsingHelper.SALE)){
                            int discount = (int)(total*(coupon_discount_amount*0.01));
                            discountMoney.setText(discount+"");
                            dc = discount;
                            discountTotal = total-discount;
                            realTotal.setText(""+discountTotal);
                        }else if(now.getCoupon_type().equals(AvailableCouponsParsingHelper.DISCOUNT)){
                            int discount = coupon_discount_amount;
                            dc = discount;
                            discountMoney.setText(discount+"");
                            discountTotal = total-discount;
                            realTotal.setText(""+discountTotal);
                        }else{
                            ;
                        }
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (coupons == null? 0 : coupons.size());
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull AvailableCouponsAdapter.ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull AvailableCouponsAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull AvailableCouponsAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull AvailableCouponsAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView couponEnd;
        TextView discountAmount;
        TextView couponType;
        TextView couponTitle;
        RelativeLayout couponShell;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            couponTitle = (itemView).findViewById(R.id.coupon_title);
            couponEnd = (itemView).findViewById(R.id.coupon_enddate);
            discountAmount = (itemView).findViewById(R.id.discount_amount);
            couponType = (itemView).findViewById(R.id.coupon_type);
            couponShell = (itemView).findViewById(R.id.coupon_Shell);

        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
}
