package com.example.canvasdrawing.Adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvasdrawing.Interface.ToolsListener;
import com.example.canvasdrawing.Interface.ViewOnClick;
import com.example.canvasdrawing.Model.ToolsItem;
import com.example.canvasdrawing.R;
import com.example.canvasdrawing.ViewHolder.ToolsViewHolder;

import java.util.List;

public class ToolsAdapters extends RecyclerView.Adapter<ToolsViewHolder> {
    private List<ToolsItem> toolsItems;
    private int selected = -1;
    private ToolsListener listener;

    public ToolsAdapters(List<ToolsItem> toolsItems, ToolsListener listener) {
        this.toolsItems = toolsItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToolsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tools_item,parent,false);
        return new ToolsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToolsViewHolder holder, int position) {
holder.name.setText(toolsItems.get(position).getName());
holder.icone.setImageResource(toolsItems.get(position).getIcone());
holder.setViewOnClick(new ViewOnClick() {
    @Override
    public void onClick(int pos) {
        selected = pos;
        listener.onSelected(toolsItems.get(pos).getName());
        notifyDataSetChanged();
    }
});
if (selected==position){
    holder.name.setTypeface(holder.name.getTypeface(), Typeface.BOLD);
}else {
    holder.name.setTypeface(Typeface.DEFAULT);
}
    }

    @Override
    public int getItemCount() {
        return toolsItems.size();
    }
}
