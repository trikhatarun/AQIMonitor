package com.android.aqimonitor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.aqimonitor.models.Reading;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trikh on 29-04-2017.
 */

public class ReadingAdapter extends RecyclerView.Adapter<ReadingAdapter.ViewHolder> {

    private Context contextInstance;
    private ArrayList<Reading> data;

    ReadingAdapter(Context context, ArrayList<Reading> dataList) {
        contextInstance = context;
        data = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemLayout = LayoutInflater.from(contextInstance).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(listItemLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.readingHeading.setText(data.get(position).getName());
        holder.readingValue.setText(data.get(position).getValue());
        holder.readingUnit.setText(data.get(position).getUnit());
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.readingHeading)
        TextView readingHeading;
        @BindView(R.id.readingValue)
        TextView readingValue;
        @BindView(R.id.readingUnit)
        TextView readingUnit;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
