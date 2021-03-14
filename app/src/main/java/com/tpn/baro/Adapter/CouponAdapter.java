package com.tpn.baro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tpn.baro.AdapterHelper.Coupon;
import com.tpn.baro.AdapterHelper.CouponList;
import com.tpn.baro.R;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder> {
    static public CouponList couponList;

    public CouponAdapter(CouponList couponList) {
        this.couponList = couponList;
    }
    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.design_my_coupon, parent, false);
        CouponViewHolder couponViewHolder = new CouponViewHolder(view, viewType);
        return couponViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull CouponViewHolder holder, int position) {
        Coupon coupon = couponList.coupon.get(position);
        holder.couponId.setText(Integer.toString(coupon.getCoupon_id()));
        holder.couponCondition.setText(coupon.getCoupon_condition()+" 원 이상 구매시 적용");
//        if(coupon.getCoupon_content().length() >= 10) {
//            holder.couponContent.setTextSize(25);
//        }
//        if(coupon.getCoupon_content().length() >= 20) {
//            holder.couponContent.setTextSize(20);
//        }
//        if(coupon.getCoupon_content().length() >= 30) {
//            holder.couponContent.setTextSize(15);
//        }

        holder.couponContent.setText(coupon.getCoupon_content());
        holder.couponDate.setText(coupon.getCoupon_enddate());
        holder.couponDiscount.setText(coupon.getCoupon_discount());
//        if(coupon.getCoupon_title().length() >= 10) {
//            holder.couponName.setTextSize(20);
//        }
        holder.couponName.setText(coupon.getCoupon_title());


    }

    @Override
    public int getItemCount() {
        return couponList == null? 0 : couponList.coupon.size();
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull CouponAdapter.CouponViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CouponAdapter.CouponViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull CouponAdapter.CouponViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CouponAdapter.CouponViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class CouponViewHolder extends RecyclerView.ViewHolder {
        TextView couponName;
        TextView couponCondition;
        TextView couponContent;
        TextView couponDate;
        TextView couponDiscount;
        TextView couponId;
        public CouponViewHolder(@NonNull View itemView, int po) {
            super(itemView);
            couponName = itemView.findViewById(R.id.coupon_name);
            couponCondition = itemView.findViewById(R.id.coupon_condition);
            couponContent = itemView.findViewById(R.id.coupon_content);
            couponDate = itemView.findViewById(R.id.coupon_date);
            couponDiscount = itemView.findViewById(R.id.coupon_discount);
            couponId = itemView.findViewById(R.id.coupon_id);
        }
    }
}
