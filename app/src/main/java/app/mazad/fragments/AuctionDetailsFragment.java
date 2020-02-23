package app.mazad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.request.RequestOptions;
import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.w3c.dom.Attr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.adapters.AttributesAdapter;
import app.mazad.adapters.SliderAdapter;
import app.mazad.classes.Constants;
import app.mazad.classes.GlobalFunctions;
import app.mazad.classes.Navigator;
import app.mazad.classes.SessionManager;
import app.mazad.webservices.RetrofitConfig;
import app.mazad.webservices.models.AttributeModel;
import app.mazad.webservices.models.AuctionDetailsModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionDetailsFragment extends Fragment {
    public static FragmentActivity activity;
    public static AuctionDetailsFragment fragment;
    private View childView;
    private SessionManager sessionManager;
    private BottomSheetDialog feesDialog;
    private AuctionDetailsModel auctionDetails;
    private ArrayList<AuctionDetailsModel.AttachmentModel> attachmentList = new ArrayList<>();
    private SliderAdapter sliderAdapter;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private ArrayList<AttributeModel> attributesList = new ArrayList<>();
    private AttributesAdapter attributesAdapter;
    private LinearLayoutManager attributesLayoutManager;
    private Double userBiddingPriceValue;

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
    @BindView(R.id.fragment_auction_details_tv_remainingDays)
    TextView remainingDays;
    @BindView(R.id.fragment_auction_details_tv_remainingHours)
    TextView remainingHours;
    @BindView(R.id.fragment_auction_details_tv_remainingMinutes)
    TextView remainingMinutes;
    @BindView(R.id.fragment_auction_details_tv_remainingSeconds)
    TextView remainingSeconds;
    @BindView(R.id.fragment_auction_details_tv_description)
    TextView description;
    @BindView(R.id.fragment_auction_details_tv_biddingCounter)
    TextView biddingCounter;
    @BindView(R.id.fragment_auction_details_rv_attributes)
    RecyclerView attributes;
    @BindView(R.id.fragment_auction_details_vp_imagesSlider)
    RtlViewPager slider;
    @BindView(R.id.fragment_auction_details_ci_sliderCircles)
    CircleIndicator sliderCircles;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static AuctionDetailsFragment newInstance(FragmentActivity activity, int id) {
        fragment = new AuctionDetailsFragment();
        AuctionDetailsFragment.activity = activity;
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_auction_details, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(false, false, false, true, null);
        container.setVisibility(View.GONE);
        sessionManager = new SessionManager(activity);
        if (auctionDetails == null) {
            auctionDetailsApi();
        } else {
            setData();
            loading.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.fragment_auction_details_iv_detailsBack)
    public void backClick() {
        activity.onBackPressed();
    }

    @OnClick(R.id.fragment_auction_details_iv_share)
    public void shareClick() {
        share();
    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = auctionDetails.getTitle() + "\n" + getString(R.string.checkAdOnApp)
                + "\n\n" + getString(R.string.iPhone) + "\n" + "iphoneLink"
                + "\n\n" + getString(R.string.android) + "\n" + "androidLink";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @OnClick(R.id.fragment_auction_details_iv_increment)
    public void incrementClick() {
        userBiddingPriceValue = Double.parseDouble(userBiddingPrice.getText().toString().replace(getString(R.string.kuwaitCurrency), ""));
        userBiddingPrice.setText(userBiddingPriceValue + 1 + " " + getString(R.string.kuwaitCurrency));
    }

    @OnClick(R.id.fragment_auction_details_iv_decrement)
    public void decrementClick() {
        userBiddingPriceValue = Double.parseDouble(userBiddingPrice.getText().toString().replace(getString(R.string.kuwaitCurrency), ""));
        if (userBiddingPriceValue == auctionDetails.lastBidPrice + auctionDetails.minimumIncrement) {
            Snackbar.make(childView, getString(R.string.canNotDecrement), Snackbar.LENGTH_SHORT).show();
        } else {
            userBiddingPrice.setText(userBiddingPriceValue - 1 + " " + getString(R.string.kuwaitCurrency));
        }
    }

    @OnClick(R.id.fragment_auction_details_tv_bidNow)
    public void bidNowtClick() {
        if (!sessionManager.isLoggedIn())
            Navigator.loadFragment(activity, LoginFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
        else {
            if (!sessionManager.hasPackage()) {
                Navigator.loadFragment(activity, PackagesFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
            } else {
                if (auctionDetails.isHighValue && !auctionDetails.isBidBefore) {
                    showFeesDialog();
                } else {
                    userBiddingPriceValue = Double.parseDouble(userBiddingPrice.getText().toString().replace(getString(R.string.kuwaitCurrency), ""));
                    bidNowApi(userBiddingPriceValue);
                }
            }
        }

    }

    private void showFeesDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_fees, null);
        TextView fees = view.findViewById(R.id.bottom_sheet_dialog_fees_tv_cost);
        fees.setText(String.valueOf(auctionDetails.bidPrice));
        TextView done = view.findViewById(R.id.bottom_sheet_dialog_fees_tv_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userBiddingPriceValue = Double.parseDouble(userBiddingPrice.getText().toString().replace(getString(R.string.kuwaitCurrency), ""));
                bidNowApi(userBiddingPriceValue);
                feesDialog.cancel();
            }
        });
        feesDialog = new BottomSheetDialog(activity);
        feesDialog.setContentView(view);
        feesDialog.show();
    }

    private void bidNowApi(double userBiddingPriceValue) {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().BID_NOW_CALL(sessionManager.getUserToken(), auctionDetails.id, sessionManager.getUserId(), userBiddingPriceValue)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            String paymentLink = null;
                            try {
                                paymentLink = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (paymentLink.equals("1")) {
                                Snackbar.make(childView, getString(R.string.bidSuccessfully), Snackbar.LENGTH_SHORT).show();
                            } else if (paymentLink.contains("http")) {
                                Navigator.loadFragment(activity, UrlsFragment.newInstance(activity, paymentLink, true, Constants.AUCTION_DETAILS)
                                        , R.id.activity_main_fl_appContainer, false);
                            }
                        } else if (responseCode == 201) {
                            Log.d(Constants.MAZAD, "auction not available");
                        } else if (responseCode == 202) {
                            Log.d(Constants.MAZAD, "user not found");
                        } else if (responseCode == 203) {
                            Snackbar.make(childView, getString(R.string.canNotBid), Snackbar.LENGTH_SHORT).show();
                        } else if (responseCode == 204) {
                            Snackbar.make(childView, getString(R.string.paymentError), Snackbar.LENGTH_SHORT).show();
                        } else if (responseCode == 201) {
                        } else if (responseCode == 400) {
                            GlobalFunctions.generalErrorMessage(activity, childView, loading);
                        } else if (responseCode == 401) {
                            showSessionTimeOutAlert();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        GlobalFunctions.EnableLayout(container);
                        GlobalFunctions.generalErrorMessage(activity, childView, loading);
                    }
                });
    }

    private void auctionDetailsApi() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().AUCTION_DETAILS(sessionManager.getUserId(), getArguments().getInt(Constants.ID))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            container.setVisibility(View.VISIBLE);
                            auctionDetails = handleAuctionDetailsResponse(response).get(0);
                            setData();
                        } else if (responseCode == 201) {
                            Log.d(Constants.MAZAD, "auction not available");
                        } else if (responseCode == 400) {
                            GlobalFunctions.generalErrorMessage(activity, childView, loading);
                        } else if (responseCode == 401) {
                            showSessionTimeOutAlert();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        GlobalFunctions.EnableLayout(container);
                        GlobalFunctions.generalErrorMessage(activity, childView, loading);
                    }
                });
    }

    private ArrayList<AuctionDetailsModel> handleAuctionDetailsResponse(Response<ResponseBody> response) {
        ResponseBody body = response.body();
        String outResponse = "";
        String jsonResponse = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(body.byteStream()));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            outResponse = out.toString();
            jsonResponse = out.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ArrayList<AuctionDetailsModel> categories = null;
        if (outResponse != null) {
            outResponse = outResponse.replace("\"", "");
            outResponse = outResponse.replace("\n", "");
            Type type = new TypeToken<ArrayList<AuctionDetailsModel>>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            categories = new Gson().fromJson(jsonResponse, type);
        }
        return categories;
    }

    private void setData() {
        setupSlider(auctionDetails.productAttachment);
        if (auctionDetails.isHighValue) {
            highValue.setVisibility(View.VISIBLE);
        } else {
            highValue.setVisibility(View.GONE);
        }

        date.setText(auctionDetails.getStartDate());

        productName.setText(auctionDetails.getTitle());
        startPrice.setText(auctionDetails.startBidPrice + " " + getString(R.string.kuwaitCurrency));
        description.setText(auctionDetails.getDescription());

        setRemainTime(auctionDetails.remainTime);

        minimumIncrement.setText(auctionDetails.minimumIncrement + " " + getString(R.string.kuwaitCurrency));
        currentPrice.setText(auctionDetails.lastBidPrice + " " + getString(R.string.kuwaitCurrency));
        biddingCounter.setText(String.valueOf(auctionDetails.userBids));

        userBiddingPrice.setText(auctionDetails.lastBidPrice + auctionDetails.minimumIncrement + " " + getString(R.string.kuwaitCurrency));

        if (auctionDetails.isCar) {
            AttributeModel carBrand = new AttributeModel();
            carBrand.carBrand = auctionDetails.brand.getValue();

            AttributeModel carModel = new AttributeModel();
            carModel.carModel = auctionDetails.carModel.getValue();

            attributesList.add(carBrand);
            attributesList.add(carModel);
        }
        attributesList.addAll(auctionDetails.productAttribute);
        attributesLayoutManager = new LinearLayoutManager(activity);
        attributesAdapter = new AttributesAdapter(activity, attributesList);
        attributes.setLayoutManager(attributesLayoutManager);
        attributes.setAdapter(attributesAdapter);
    }

    private void setupSlider(ArrayList<AuctionDetailsModel.AttachmentModel> attachmentList) {
        /*make slider autoPlay , every 5 seconds the image will replace with the next one
         * when lastImage come , the cycle will start from 0 again*/
        slider.setCurrentItem(0, true);
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                slider.setCurrentItem(currentPage++, true);
            }
        };


        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 5000, 5000);

        NUM_PAGES = attachmentList.size();
        sliderAdapter = new SliderAdapter(activity, attachmentList);
        slider.setAdapter(sliderAdapter);
        sliderCircles.setViewPager(slider);
        sliderCircles.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
                currentPage = position;
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setRemainTime(Double remainTime) {
        long startTimeLong = Math.round(remainTime);

        final long remainDays = TimeUnit.MILLISECONDS.toDays(startTimeLong);
        final long remainHours = TimeUnit.MILLISECONDS.toHours(startTimeLong)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(startTimeLong));
        final long remainMinutes = TimeUnit.MILLISECONDS.toMinutes(startTimeLong)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(startTimeLong));
        final long remainSeconds = TimeUnit.MILLISECONDS.toSeconds(startTimeLong)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTimeLong));

        String daysFormat = "<font color=#07476E>" + remainDays + "<br><\br>" + "</font> <font color=#464646>" + getString(R.string.day) + "</font>";
        remainingDays.setText(Html.fromHtml(daysFormat));

        String hoursFormat = "<font color=#07476E>" + remainHours + "<br><\br>" + "</font> <font color=#464646>" + getString(R.string.hour) + "</font>";
        remainingHours.setText(Html.fromHtml(hoursFormat));

        String minutesFormat = "<font color=#07476E>" + remainMinutes + "<br><\br>" + "</font> <font color=#464646>" + getString(R.string.minute) + "</font>";
        remainingMinutes.setText(Html.fromHtml(minutesFormat));

        String secondsFormat = "<font color=#07476E>" + remainSeconds + "<br><\br>" + "</font> <font color=#464646>" + getString(R.string.second) + "</font>";
        remainingSeconds.setText(Html.fromHtml(secondsFormat));
    }

    private void showSessionTimeOutAlert() {
        new AlertDialog.Builder(activity)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.sessionTimeOut))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sessionManager.logout();
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                        startActivity(new Intent(activity, MainActivity.class));
                        Navigator.loadFragment(activity, LoginFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
                    }
                })
                .setIcon(R.mipmap.ic_launcher_icon)
                .show();
    }
}


