package app.mazad.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.mazad.R;
import app.mazad.classes.FixControl;
import app.mazad.classes.Model;
import app.mazad.classes.SessionManager;
import app.mazad.webservices.models.AuctionModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AuctionsAdapter extends RecyclerView.Adapter<AuctionsAdapter.viewHolder> {
    private Context context;
    private ArrayList<AuctionModel> auctionsList;
    private boolean isLatest;
    private SessionManager sessionManager;
    private OnItemCLickListener listener;

    public AuctionsAdapter(Context context, ArrayList<AuctionModel> auctionsList, boolean isLatest,OnItemCLickListener listener) {
        this.context = context;
        this.auctionsList = auctionsList;
        this.isLatest = isLatest;
        this.listener = listener;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_auctions_tv_highValue)
        TextView highValue;
        @BindView(R.id.item_auctions_riv_productImage)
        RoundedImageView productImage;
        @BindView(R.id.item_auctions_tv_remainingDays)
        TextView remainingDays;
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
        @BindView(R.id.item_auctions_tv_startTime)
        TextView startTime;
        @BindView(R.id.item_auctions_v_details)
        View details;
        @BindView(R.id.loading)
        ProgressBar loading;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            sessionManager = new SessionManager(context);
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
        if (isLatest)
            viewHolder.lastBid.setVisibility(View.GONE);
        else {
            if (auctionsList.get(position).lastBidUserId == sessionManager.getUserId())
                viewHolder.lastBid.setText(context.getString(R.string.lastBid) + " " + context.getString(R.string.me));
            else
                viewHolder.lastBid.setText(context.getString(R.string.lastBid) + " " + auctionsList.get(position).user);
        }
        if (auctionsList.get(position).isHighValue)
            viewHolder.highValue.setVisibility(View.VISIBLE);
        else
            viewHolder.highValue.setVisibility(View.GONE);

        setImage(auctionsList.get(position).photoUrl, viewHolder);
        viewHolder.productName.setText(auctionsList.get(position).getTitle());
        viewHolder.currentPrice.setText(auctionsList.get(position).lastBidPrice + " " + context.getString(R.string.kuwaitCurrency));
        viewHolder.biddingCounter.setText(String.valueOf(auctionsList.get(position).userBids));

        setRemainTime(auctionsList.get(position).remainTime, viewHolder);
        setStartTime(auctionsList.get(position).startTime, viewHolder);

        viewHolder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.detailsClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return auctionsList.size();
    }

    private void setImage(String imagePath, AuctionsAdapter.viewHolder viewHolder) {
        if (imagePath != null) {
            try {
                int Width = FixControl.getImageWidth(context, R.drawable.home_list_noimg);
                int Height = FixControl.getImageHeight(context, R.drawable.home_list_noimg);
                viewHolder.productImage.getLayoutParams().height = Height;
                viewHolder.productImage.getLayoutParams().width = Width;
                Glide.with(context.getApplicationContext()).load(imagePath)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.home_list_noimg)
                                .centerCrop())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                viewHolder.loading.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                viewHolder.loading.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(viewHolder.productImage);
                viewHolder.productImage.setCornerRadius(10, 10, 10, 10);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private void setRemainTime(Double remainTime, AuctionsAdapter.viewHolder viewHolder) {
        long startTimeLong = Math.round(remainTime);

        final long remainDays = TimeUnit.MILLISECONDS.toDays(startTimeLong);
        final long remainHours = TimeUnit.MILLISECONDS.toHours(startTimeLong)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(startTimeLong));
        final long remainMinutes = TimeUnit.MILLISECONDS.toMinutes(startTimeLong)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(startTimeLong));
        final long remainSeconds = TimeUnit.MILLISECONDS.toSeconds(startTimeLong)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTimeLong));

        String daysFormat = "<font color=#07476E>" + remainDays + "<br><\br>" + "</font> <font color=#464646>" + context.getString(R.string.day) + "</font>";
        viewHolder.remainingDays.setText(Html.fromHtml(daysFormat));

        String hoursFormat = "<font color=#07476E>" + remainHours + "<br><\br>" + "</font> <font color=#464646>" + context.getString(R.string.hour) + "</font>";
        viewHolder.remainingHours.setText(Html.fromHtml(hoursFormat));

        String minutesFormat = "<font color=#07476E>" + remainMinutes + "<br><\br>" + "</font> <font color=#464646>" + context.getString(R.string.minute) + "</font>";
        viewHolder.remainingMinutes.setText(Html.fromHtml(minutesFormat));

        String secondsFormat = "<font color=#07476E>" + remainSeconds + "<br><\br>" + "</font> <font color=#464646>" + context.getString(R.string.second) + "</font>";
        viewHolder.remainingSeconds.setText(Html.fromHtml(secondsFormat));
    }

    private void setStartTime(Double startTime, AuctionsAdapter.viewHolder viewHolder) {
        long startTimeLong = startTime.longValue();
        final long startDays = TimeUnit.MILLISECONDS.toDays(startTimeLong);
        final long startHours = TimeUnit.MILLISECONDS.toHours(startTimeLong)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(startTimeLong));
        final long startMinutes = TimeUnit.MILLISECONDS.toMinutes(startTimeLong)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(startTimeLong));
        viewHolder.startTime.setText(startDays + context.getString(R.string.day) + ":" + startHours + context.getString(R.string.hour) + ":" + startMinutes + context.getString(R.string.minute));
    }

    public interface OnItemCLickListener{
        public void detailsClick(int position);
    }
}





