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

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.viewHolder> {
    private Context context;
    private ArrayList<Model> ProductsList;

    public ProductsAdapter(Context context, ArrayList<Model> ProductsList) {
        this.context = context;
        this.ProductsList = ProductsList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_product_riv_productImage)
        RoundedImageView productImage;
        @BindView(R.id.item_product_tv_productName)
        TextView productName;
        @BindView(R.id.item_product_tv_currentPrice)
        TextView currentPrice;
        @BindView(R.id.item_product_tv_biddingCounter)
        TextView biddingCounter;
        @BindView(R.id.item_product_tv_remainingTime)
        TextView remainingTime;
        @BindView(R.id.loading)
        ProgressBar loading;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ProductsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_product, viewGroup, false);
        return new ProductsAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.viewHolder viewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return ProductsList.size();
    }
}