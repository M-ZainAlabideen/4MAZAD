package app.mazad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.mazad.R;
import app.mazad.classes.Model;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.viewHolder> {
    private Context context;
    private ArrayList<Model> SearchResultsList;

    public SearchResultsAdapter(Context context, ArrayList<Model> SearchResultsList) {
        this.context = context;
        this.SearchResultsList = SearchResultsList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_search_result_tv_name)
        TextView name;
        @BindView(R.id.item_search_result_v_details)
        View details;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public SearchResultsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_search_result, viewGroup, false);
        return new SearchResultsAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsAdapter.viewHolder viewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return SearchResultsList.size();
    }
}






