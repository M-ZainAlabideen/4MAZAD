package app.mazad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.mazad.R;
import app.mazad.classes.Model;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAttributesAdapter extends RecyclerView.Adapter<ProductAttributesAdapter.viewHolder> {
    private Context context;
    private ArrayList<Model> productAttributesList;

    public ProductAttributesAdapter(Context context, ArrayList<Model> productAttributesList) {
        this.context = context;
        this.productAttributesList = productAttributesList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_product_attribute_tv_name)
        TextView name;
        @BindView(R.id.item_product_attribute_tv_value)
        TextView value;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ProductAttributesAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_product_attributes, viewGroup, false);
        return new ProductAttributesAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAttributesAdapter.viewHolder viewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return productAttributesList.size();
    }
}