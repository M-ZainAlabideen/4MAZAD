package app.mazad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.mazad.R;
import app.mazad.classes.Model;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.viewHolder> {
    private Context context;
    private ArrayList<Model> filterList;

    public FilterAdapter(Context context, ArrayList<Model> filterList) {
        this.context = context;
        this.filterList = filterList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_filter_tv_name)
        TextView name;
        @BindView(R.id.item_filter_cb_checkBox)
        CheckBox checkBox;
        @BindView(R.id.item_filter_v_select)
        View select;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public FilterAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_filter, viewGroup, false);
        return new FilterAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.viewHolder viewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }
}


