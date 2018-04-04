package ru.goodibunakov.testforzennex2018.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.goodibunakov.testforzennex2018.R;

public class RecyclerJSONAdapter extends RecyclerView.Adapter<RecyclerJSONAdapter.ViewHolder> {

    private ArrayList<String> arrayList;

    public RecyclerJSONAdapter(ArrayList<String> fromAsyncTask) {
        arrayList = fromAsyncTask;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }

    @Override
    public RecyclerJSONAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_parsing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerJSONAdapter.ViewHolder holder, int position) {
        holder.tv.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}