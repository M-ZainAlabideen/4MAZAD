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

public class AuctionsAdapter extends RecyclerView.Adapter<AuctionsAdapter.viewHolder> {
    private Context context;
    private ArrayList<Model> auctionsList;

    public AuctionsAdapter(Context context, ArrayList<Model> auctionsList) {
        this.context = context;
        this.auctionsList = auctionsList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_auctions_tv_highValue)
        TextView highValue;
        @BindView(R.id.item_auctions_riv_productImage)
        RoundedImageView productImage;
        @BindView(R.id.item_auctions_tv_remainingHours)
        TextView remainingHours;
        @BindView(R.id.item_auctions_tv_remainingMinutes)
        TextView remainingMinutes;
        @BindView(R.id.item_auctions_tv_remainingSeconds)
        TextView remainingSeconds;
        @BindView(R.id.item_auctions_tv_biddingCounter)
        TextView biddingCounter;
        @BindView(R.id.item_auctions_tv_currentPrice)
        TextView currentPrice;
        @BindView(R.id.item_auctions_tv_lastBid)
        TextView lastBid;
        @BindView(R.id.item_auctions_tv_productName)
        TextView productName;
        @BindView(R.id.item_auctions_tv_remainingTime)
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
    public AuctionsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_auctions, viewGroup, false);
        return new AuctionsAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionsAdapter.viewHolder viewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return auctionsList.size();
    }
}





