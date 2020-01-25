package app.mazad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

public class HomeCategoriesAdapter extends RecyclerView.Adapter<HomeCategoriesAdapter.viewHolder> {
    private Context context;
    private ArrayList<Model> homeCategoriesList;

    public HomeCategoriesAdapter(Context context, ArrayList<Model> homeCategoriesList) {
        this.context = context;
        this.homeCategoriesList = homeCategoriesList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_home_category_riv_categoryImage)
        RoundedImageView categoryImage;
        @BindView(R.id.item_home_category_tv_categoryName)
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
    public HomeCategoriesAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_home_category, viewGroup, false);
        return new HomeCategoriesAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoriesAdapter.viewHolder viewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return homeCategoriesList.size();
    }
}



