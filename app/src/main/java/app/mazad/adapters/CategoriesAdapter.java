package app.mazad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import app.mazad.R;
import app.mazad.classes.Model;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.viewHolder> {
    private Context context;
    private ArrayList<Model> categoriesList;

    public CategoriesAdapter(Context context, ArrayList<Model> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_category_riv_categoryImage)
        RoundedImageView categoryImage;
        @BindView(R.id.item_category_tv_categoryName)
        TextView categoryName;
        @BindView(R.id.loading)
        ProgressBar loading;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public CategoriesAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_category, viewGroup, false);
        return new CategoriesAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.viewHolder viewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }
}
