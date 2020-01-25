package app.mazad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.smarteist.autoimageslider.SliderView;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuctionDetailsFragment extends Fragment {
    public static FragmentActivity activity;
    public static AuctionDetailsFragment fragment;
    private BottomSheetDialog feesDialog;

    @BindView(R.id.fragment_auction_details_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_auction_details_tv_highValue)
    TextView highValue;
    @BindView(R.id.fragment_auction_details_tv_date)
    TextView date;
    @BindView(R.id.fragment_auction_details_tv_productName)
    TextView productName;
    @BindView(R.id.fragment_auction_details_tv_currentPrice)
    TextView currentPrice;
    @BindView(R.id.fragment_auction_details_tv_startPrice)
    TextView startPrice;
    @BindView(R.id.fragment_auction_details_tv_minimumIncrement)
    TextView minimumIncrement;
    @BindView(R.id.fragment_auction_details_tv_userBiddingPrice)
    TextView userBiddingPrice;
    @BindView(R.id.fragment_auction_details_tv_remainingHours)
    TextView remainingHours;
    @BindView(R.id.fragment_auction_details_tv_remainingMinutes)
    TextView remainingMinutes;
    @BindView(R.id.fragment_auction_details_tv_remainingSeconds)
    TextView remainingSeconds;
    @BindView(R.id.fragment_auction_details_tv_shortDescription)
    TextView shortDescription;
    @BindView(R.id.fragment_auction_details_tv_fullDescription)
    TextView fullDescription;
    @BindView(R.id.fragment_auction_details_tv_biddingCounter)
    TextView biddingCounter;
    @BindView(R.id.fragment_auction_details_rv_attributes)
    RecyclerView attributes;
    @BindView(R.id.fragment_auction_details_sv_imagesSlider)
    SliderView imagesSlider;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static AuctionDetailsFragment newInstance(FragmentActivity activity) {
        fragment = new AuctionDetailsFragment();
        AuctionDetailsFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_auction_details, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(false, false, false, false,true,null);
    }

    @OnClick(R.id.fragment_auction_details_iv_detailsBack)
    public void backClick() {
    }

    @OnClick(R.id.fragment_auction_details_iv_share)
    public void shareClick() {
    }

    @OnClick(R.id.fragment_auction_details_iv_increment)
    public void incrementClick() {
    }

    @OnClick(R.id.fragment_auction_details_iv_decrement)
    public void decrementClick() {
    }

    @OnClick(R.id.fragment_auction_details_tv_bidNow)
    public void bidNowtClick() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_fees, null);
        TextView fees = view.findViewById(R.id.bottom_sheet_dialog_fees_tv_cost);
        TextView done = view.findViewById(R.id.bottom_sheet_dialog_fees_tv_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feesDialog.cancel();
            }
        });
        feesDialog = new BottomSheetDialog(activity);
        feesDialog.setContentView(view);
        feesDialog.show();
    }
}
//    String first = "This word is ";
//    String next = "<font color='#EE0000'>red</font>";
//t.setText(Html.fromHtml(first + next));

