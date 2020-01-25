package app.mazad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.mazad.R;
import app.mazad.classes.Model;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.viewHolder> {
    private Context context;
    private ArrayList<Model> packagesList;

    public PackagesAdapter(Context context, ArrayList<Model> packagesList) {
        this.context = context;
        this.packagesList = packagesList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_package_cv_container)
        CardView container;
        @BindView(R.id.item_package_tv_packageName)
        TextView packageName;
        @BindView(R.id.item_package_tv_packagePrice)
        TextView packagePrice;
        @BindView(R.id.item_package_tv_packageDescription)
        TextView packageDescription;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public PackagesAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_package, viewGroup, false);
        return new PackagesAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull PackagesAdapter.viewHolder viewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return packagesList.size();
    }
}