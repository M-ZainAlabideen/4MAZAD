package app.mazad.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.adapters.AuctionsAdapter;
import app.mazad.adapters.HomeCategoriesAdapter;
import app.mazad.classes.FixControl;
import app.mazad.classes.GlobalFunctions;
import app.mazad.classes.Navigator;
import app.mazad.classes.SessionManager;
import app.mazad.webservices.RetrofitConfig;
import app.mazad.webservices.models.AuctionModel;
import app.mazad.webservices.models.CategoryModel;
import app.mazad.webservices.responses.AdResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    public static FragmentActivity activity;
    public static HomeFragment fragment;
    private View childView;
    private SessionManager sessionManager;
    private String searchQuery;
    private AdResponse Ad;
    private ArrayList<CategoryModel> categoriesList = new ArrayList<>();
    private LinearLayoutManager categoriesLayoutManager;
    private HomeCategoriesAdapter categoriesAdapter;
    private ArrayList<AuctionModel> auctionsList = new ArrayList<>();
    private LinearLayoutManager auctionsLayoutManager;
    private AuctionsAdapter auctionsAdapter;
    private int pageIndex = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;


    @BindView(R.id.fragment_home_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_home_et_search)
    EditText search;
    @BindView(R.id.fragment_home_riv_Ad)
    RoundedImageView AdImage;
    @BindView(R.id.fragment_home_rv_categories)
    RecyclerView categories;
    @BindView(R.id.fragment_home_rv_latestAuctions)
    RecyclerView latestAuctions;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static HomeFragment newInstance(FragmentActivity activity) {
        fragment = new HomeFragment();
        HomeFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, false, false, true, getString(R.string.app_name));
        sessionManager = new SessionManager(activity);
        FixControl.setupUI(activity, container);
        container.setVisibility(View.GONE);
        if (Ad == null) {
            AdApi();
        } else {
            setImage(Ad.photoUrl,AdImage);
            loading.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }
        intiCategories();
        if (categoriesList.size() == 0) {
            categoriesApi();
        } else {
            loading.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }
        intiAuctions();
        if (auctionsList.size() == 0) {
            latestAuctionsApi();
        } else {
            loading.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.fragment_home_riv_Ad)
    public void AdClick() {
        if (Ad.link != null && !Ad.link.isEmpty()) {
            Navigator.loadFragment(activity, UrlsFragment.newInstance(activity,Ad.link,false,null),R.id.activity_main_fl_appContainer,true);
        } else {
            ArrayList<String> paintings = new ArrayList<>();
            paintings.add(Ad.photoUrl);
            Navigator.loadFragment(activity, ImagesGestureFragment.newInstance(activity,paintings,0),R.id.activity_main_fl_appContainer,true);
        }
    }

    @OnEditorAction(R.id.fragment_home_et_search)
    public boolean onEditorAction(EditText editText, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search.clearFocus();
            InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(search.getWindowToken(), 0);
            searchQuery = search.getText().toString();
        }
        Navigator.loadFragment(activity, SearchResultsFragment.newInstance(activity, searchQuery, 0), R.id.activity_main_fl_appContainer, true);
        return actionId == EditorInfo.IME_ACTION_SEARCH;
    }


    private void intiCategories() {
        categoriesLayoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        categoriesAdapter = new HomeCategoriesAdapter(activity, categoriesList, new HomeCategoriesAdapter.OnItemClickListener() {
            @Override
            public void categoryProductsClick(int position) {
                Navigator.loadFragment(activity, ProductsFragment.newInstance(activity,false, categoriesList.get(position)), R.id.activity_main_fl_appContainer, true);
            }
        });
        categories.setLayoutManager(categoriesLayoutManager);
        categories.setAdapter(categoriesAdapter);
    }

    private void intiAuctions() {
        auctionsLayoutManager = new LinearLayoutManager(activity);
        auctionsAdapter = new AuctionsAdapter(activity, auctionsList, true, new AuctionsAdapter.OnItemCLickListener() {
            @Override
            public void detailsClick(int position) {
                Navigator.loadFragment(activity, AuctionDetailsFragment.newInstance(activity, auctionsList.get(position).id), R.id.activity_main_fl_appContainer, true);
            }
        });
        latestAuctions.setLayoutManager(auctionsLayoutManager);
        latestAuctions.setAdapter(auctionsAdapter);
    }

    private void categoriesApi() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().CATEGORIES_CALL()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            container.setVisibility(View.VISIBLE);
                            categoriesList.addAll(handleCategoriesResponse(response));
                            categoriesAdapter.notifyDataSetChanged();
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

    private void latestAuctionsApi() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().LATEST_AUCTIONS(pageIndex)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            container.setVisibility(View.VISIBLE);
                            auctionsList.addAll(handleAuctionsResponse(response));
                            auctionsAdapter.notifyDataSetChanged();
                            if (auctionsList.size() > 0) {
                                latestAuctions.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                    }

                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        if (!isLastPage) {
                                            int visibleItemCount = auctionsLayoutManager.getChildCount();

                                            int totalItemCount = auctionsLayoutManager.getItemCount();

                                            int pastVisibleItems = auctionsLayoutManager.findFirstVisibleItemPosition();

                                /*isLoading variable used for check if the user send many requests
                                for pagination(make many scrolls in the same time)
                                1- if isLoading true >> there is request already sent so,
                                no more requests till the response of last request coming
                                2- else >> send new request for load more data (News)*/
                                            if (!isLoading) {

                                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                                    isLoading = true;

                                                    pageIndex = pageIndex + 1;

                                                    getMoreAuctions();

                                                }
                                            }
                                        }
                                    }
                                });
                            }
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

    private void getMoreAuctions() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().LATEST_AUCTIONS(pageIndex)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            if (handleAuctionsResponse(response).size() > 0) {
                                auctionsList.addAll(handleAuctionsResponse(response));
                                auctionsAdapter.notifyDataSetChanged();
                            } else {
                                isLastPage = true;
                                pageIndex = pageIndex - 1;
                            }
                            isLoading = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

    }

    private void AdApi() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().AD_CALL()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            container.setVisibility(View.VISIBLE);
                            Ad = handleAdResponse(response);
                            if (Ad != null && Ad.photoUrl != null && !Ad.photoUrl.isEmpty()) {
                                setImage(Ad.photoUrl, AdImage);
                            } else {
                                AdImage.setVisibility(View.GONE);
                            }
                            categoriesAdapter.notifyDataSetChanged();
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

    private void setImage(String imagePath, RoundedImageView image) {
        if (imagePath != null) {
            try {
                Glide.with(activity.getApplicationContext()).load(imagePath)
                        .apply(new RequestOptions().centerCrop()
                                .placeholder(R.drawable.advertisement))
                        .into(image);
                image.setCornerRadius(10, 10, 10, 10);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private ArrayList<CategoryModel> handleCategoriesResponse(Response<ResponseBody> response) {
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
        ArrayList<CategoryModel> categories = null;
        if (outResponse != null) {
            outResponse = outResponse.replace("\"", "");
            outResponse = outResponse.replace("\n", "");
            Type type = new TypeToken<ArrayList<CategoryModel>>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            categories = new Gson().fromJson(jsonResponse, type);
        }
        return categories;
    }

    private ArrayList<AuctionModel> handleAuctionsResponse(Response<ResponseBody> response) {
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

    private AdResponse handleAdResponse(Response<ResponseBody> response) {
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
        AdResponse ad = null;
        if (outResponse != null) {
            outResponse = outResponse.replace("\"", "");
            outResponse = outResponse.replace("\n", "");
            Type type = new TypeToken<AdResponse>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            ad = new Gson().fromJson(jsonResponse, type);
        }
        return ad;
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



