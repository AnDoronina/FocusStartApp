package com.an.focusstartapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {


    private Context parent;
    private ArrayList<CurrencyListItem> dataList;

    public CurrencyAdapter(Context parent) {
        this.parent = parent;
    }

    public void setData(ArrayList<CurrencyListItem> dataList) {
        this.dataList = dataList;
        this.notifyDataSetChanged();

    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int  layoutIdForListItem = R.layout.currencies_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        CurrencyViewHolder viewHolder = new CurrencyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.dataList != null ? this.dataList.size() : 0;
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {

        TextView countryCode;
        TextView currencyValue;

        public CurrencyViewHolder( View itemView) {
            super(itemView);

            countryCode = itemView.findViewById(R.id.tv_currencies_item);
            currencyValue = itemView.findViewById(R.id.tv_view_holder_currencies);
        }

        void bind(int listIndex) {
            CurrencyListItem item = CurrencyAdapter.this.dataList.get(listIndex);
            countryCode.setText(item.code);
            currencyValue.setText(String.format("%.2f", item.value));
        }
    }
    public static class CurrencyListItem {
        String code;
        double value;

        public CurrencyListItem(String code, double value) {
            this.code = code;
            this.value = value;
        }
    }
}
