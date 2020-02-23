package app.mazad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.adapters.AuctionsAdapter;
import app.mazad.adapters.HomeCategoriesAdapter;
import app.mazad.adapters.ProductsAdapter;
import app.mazad.classes.GlobalFunctions;
import app.mazad.classes.Navigator;
import app.mazad.classes.SessionManager;
import app.mazad.webservices.RetrofitConfig;
import app.mazad.webservices.models.AuctionModel;
import app.mazad.webservices.models.CategoryModel;
import app.mazad.webservices.responses.AdResponse;
import app.mazad.webservices.responses.AuthResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionsRequestsFragment extends Fragment {
    public static FragmentActivity activity;
    public static AuctionsRequestsFragment fragment;
    private View childView;
    private SessionManager sessionManager;
    private ArrayList<AuctionModel> endedAuctionsList = new ArrayList<>();
    private LinearLayoutManager auctionsLayoutManager;
    private AuctionsAdapter auctionsAdapter;
    private int endedPageIndex = 1;
    private boolean endedIsLoading = false;
    private boolean endedIsLastPage = false;

    private ArrayList<AuctionModel> currentAuctionsList = new ArrayList<>();
    private int currentPageIndex = 1;
    private boolean currentIsLoading = false;
    private boolean currentIsLastPage = false;
    private boolean iscurrent = true;

    @BindView(R.id.fragment_auctions_requests_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_auctions_requests_tv_current)
    TextView current;
    @BindView(R.id.fragment_auctions_requests_tv_ended)
    TextView ended;
    @BindView(R.id.fragment_auctions_requests_rv_auctionsRequests)
    RecyclerView auctionsRequests;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static AuctionsRequestsFragment newInstance(FragmentActivity activity) {
        fragment = new AuctionsRequestsFragment();
        AuctionsRequestsFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_auctions_requests, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, true, false, true, getString(R.string.myBidding));
        sessionManager = new SessionManager(activity);

        if (iscurrent) {
            intiAuctions(currentAuctionsList);
            if (currentAuctionsList.size() == 0) {
                currentAuctionsApi();
            } else {
                loading.setVisibility(View.GONE);
            }
        } else {
            intiAuctions(endedAuctionsList);
            if (endedAuctionsList.size() == 0) {
                endedAuctionApi();
            } else {
                loading.setVisibility(View.GONE);
            }
        }
    }


    private void intiAuctions(ArrayList<AuctionModel> requestsList) {
        auctionsLayoutManager = new LinearLayoutManager(activity);
        auctionsAdapter = new AuctionsAdapter(activity, requestsList, false, new AuctionsAdapter.OnItemCLickListener() {
            @Override
            public void detailsClick(int position) {
                Navigator.loadFragment(activity,AuctionDetailsFragment.newInstance(activity,requestsList.get(position).id),R.id.activity_main_fl_appContainer,true);
            }
        });
        auctionsRequests.setLayoutManager(auctionsLayoutManager);
        auctionsRequests.setAdapter(auctionsAdapter);
    }

    @OnClick(R.id.fragment_auctions_requests_tv_current)
    public void currentClick() {
        iscurrent = true;
        current.setBackgroundResource(R.drawable.tap_sel_right);
        current.setTextColor(Color.parseColor("#ffffff"));
        intiAuctions(currentAuctionsList);
        if (currentAuctionsList.size() == 0) {
            currentAuctionsApi();
        } else {
            loading.setVisibility(View.GONE);
        }

        ended.setBackground(null);
        ended.setTextColor(Color.parseColor("#52AECD"));
    }

    @OnClick(R.id.fragment_auctions_requests_tv_ended)
    public void endedClick() {
        iscurrent = false;
        ended.setBackgroundResource(R.drawable.tap_sel_right);
        ended.setTextColor(Color.parseColor("#ffffff"));
        intiAuctions(endedAuctionsList);
        if (endedAuctionsList.size() == 0) {
            endedAuctionApi();
        } else {
            loading.setVisibility(View.GONE);
        }
        current.setBackground(null);
        current.setTextColor(Color.parseColor("#52AECD"));
    }

    private void currentAuctionsApi() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().CURRENT_AUCTIONS_CALL(sessionManager.getUserToken(), sessionManager.getUserId(), currentPageIndex)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            currentAuctionsList.addAll(handleAuctionsRequestsResponse(response));
                            auctionsAdapter.notifyDataSetChanged();
                            if (currentAuctionsList.size() > 0) {
                                auctionsRequests.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                    }

                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        if (!currentIsLastPage) {
                                            int visibleItemCount = auctionsLayoutManager.getChildCount();

                                            int totalItemCount = auctionsLayoutManager.getItemCount();

                                            int pastVisibleItems = auctionsLayoutManager.findFirstVisibleItemPosition();

                                /*isLoading variable used for check if the user send many requests
                                for pagination(make many scrolls in the same time)
                                1- if isLoading true >> there is request already sent so,
                                no more requests till the response of last request coming
                                2- else >> send new request for load more data (News)*/
                                            if (!currentIsLoading) {

                                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                                    currentIsLoading = true;

                                                    currentPageIndex = currentPageIndex + 1;

                                                    getMoreCurrentAuction();

                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                Snackbar.make(childView, getString(R.string.noCurrentRequestsFound), Snackbar.LENGTH_SHORT).show();
                            }
                        } else if (responseCode == 400) {
                            Snackbar.make(childView, getString(R.string.generalError), Snackbar.LENGTH_SHORT).show();
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

    private void getMoreCurrentAuction() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().CURRENT_AUCTIONS_CALL(sessionManager.getUserToken(), sessionManager.getUserId(), currentPageIndex)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            if (handleAuctionsRequestsResponse(response).size() > 0) {
                                currentAuctionsList.addAll(handleAuctionsRequestsResponse(response));
                                auctionsAdapter.notifyDataSetChanged();
                            } else {
                                currentIsLastPage = true;
                                currentPageIndex = currentPageIndex - 1;
                            }
                            currentIsLoading = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    private void endedAuctionApi() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().ENDED_AUCTIONS_CALL(sessionManager.getUserToken(), sessionManager.getUserId(), endedPageIndex)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            endedAuctionsList.addAll(handleAuctionsRequestsResponse(response));
                            auctionsAdapter.notifyDataSetChanged();
                            if (endedAuctionsList.size() > 0) {
                                auctionsRequests.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                    }

                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        if (!endedIsLastPage) {
                                            int visibleItemCount = auctionsLayoutManager.getChildCount();

                                            int totalItemCount = auctionsLayoutManager.getItemCount();

                                            int pastVisibleItems = auctionsLayoutManager.findFirstVisibleItemPosition();

                                /*isLoading variable used for check if the user send many requests
                                for pagination(make many scrolls in the same time)
                                1- if isLoading true >> there is request already sent so,
                                no more requests till the response of last request coming
                                2- else >> send new request for load more data (News)*/
                                            if (!endedIsLoading) {

                                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                                    endedIsLoading = true;

                                                    endedPageIndex = endedPageIndex + 1;

                                                    getMoreEndedAuction();

                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                Snackbar.make(childView, getString(R.string.noEndedRequestsFound), Snackbar.LENGTH_SHORT).show();
                            }
                        } else if (responseCode == 400) {
                            Snackbar.make(childView, getString(R.string.incorrectUserNameOrPass), Snackbar.LENGTH_SHORT).show();
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

    private void getMoreEndedAuction() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().ENDED_AUCTIONS_CALL(sessionManager.getUserToken(), sessionManager.getUserId(), endedPageIndex)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            if (handleAuctionsRequestsResponse(response).size() > 0) {
                                endedAuctionsList.addAll(handleAuctionsRequestsResponse(response));
                                auctionsAdapter.notifyDataSetChanged();
                            } else {
                                endedIsLastPage = true;
                                endedPageIndex = endedPageIndex - 1;
                            }
                            endedIsLoading = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    private ArrayList<AuctionModel> handleAuctionsRequestsResponse(Response<ResponseBody> response) {
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
        ArrayList<AuctionModel> auctions = null;
        if (outResponse != null) {
            outResponse = outResponse.replace("\"", "");
            outResponse = outResponse.replace("\n", "");
            Type type = new TypeToken<ArrayList<AuctionModel>>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            auctions = new Gson().fromJson(jsonResponse, type);
        }
        return auctions;
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




