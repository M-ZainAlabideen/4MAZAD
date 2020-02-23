package app.mazad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.appyvet.materialrangebar.RangeBar;
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
import app.mazad.adapters.FilterAdapter;
import app.mazad.classes.Constants;
import app.mazad.classes.GlobalFunctions;
import app.mazad.classes.Navigator;
import app.mazad.classes.SessionManager;
import app.mazad.webservices.RetrofitConfig;
import app.mazad.webservices.models.AuctionModel;
import app.mazad.webservices.models.CategoryModel;
import app.mazad.webservices.models.FilterModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FiltersFragment extends Fragment {
    public static FragmentActivity activity;
    public static FiltersFragment fragment;
    private View childView;
    private SessionManager sessionManager;
    private CategoryModel category;
    private ArrayList<FilterModel> responseFiltersList = new ArrayList<>();
    public static String startPriceValue;
    public static String endPriceValue;
    public static ArrayList<FilterModel> requestFilterList = new ArrayList<>();
    private FilterAdapter filterAdapter;
    private LinearLayoutManager filtersLayoutManager;

    @BindView(R.id.fragment_filters_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_filters_rv_filters)
    RecyclerView filters;
    @BindView(R.id.fragment_filters_rb_pricesRange)
    RangeBar pricesRange;
    @BindView(R.id.fragment_filters_tv_startPrice)
    TextView startPrice;
    @BindView(R.id.fragment_filters_tv_endPrice)
    TextView endPrice;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static FiltersFragment newInstance(FragmentActivity activity, CategoryModel category) {
        fragment = new FiltersFragment();
        FiltersFragment.activity = activity;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_filters, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, true, false, false, getString(R.string.filter));
        container.setVisibility(View.GONE);
        sessionManager = new SessionManager(activity);
        setupPricesRangeBar();
        intiFilters();
        category = (CategoryModel) getArguments().getSerializable(Constants.CATEGORY);
        if (responseFiltersList.size() == 0) {
            getFiltersApi(category.id);
        } else {
            loading.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }
    }

    private void setupPricesRangeBar() {
//        pricesRange.setTickStart(100);
        //    pricesRange.setTickEnd(1000);


        /*
        *  app:mrb_barWeight="2dp"
                app:mrb_connectingLineColors="@color/lightBlue"
                app:mrb_connectingLineWeight="4dp"
                app:mrb_leftSelectorColor="#FFB300"
                app:mrb_pinColor="#6c3f6a"
                app:mrb_pinMaxFont="15sp"
                app:mrb_pinMinFont="12sp"
                app:mrb_pinRadius="14dp"
                app:mrb_pinTextColor="@color/white"
                app:mrb_rangeBar="true"
                app:mrb_rangeBarPaddingBottom="30dp"
                app:mrb_rangeBar_rounded="true"
                app:mrb_rightSelectorColor="#1E88E5"
                app:mrb_selectorBoundaryColor="@color/gray"
                app:mrb_selectorBoundarySize="2dp"
                app:mrb_selectorSize="10dp"
                app:mrb_temporaryPins="true"
                app:mrb_tickDefaultLabel="label"
                app:mrb_tickEnd="10"
                app:mrb_tickHeight="4dp"
                app:mrb_tickInterval="1"
                app:mrb_tickLabelColor="@color/black"
                app:mrb_tickLabelSize="4sp"
                app:mrb_tickStart="1"
        * */
        pricesRange.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                startPrice.setText(pricesRange.getLeftPinValue() + " " + getString(R.string.kuwaitCurrency));
                endPrice.setText(pricesRange.getRightPinValue() + " " + getString(R.string.kuwaitCurrency));
            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }
        });
    }

    private void intiFilters() {
        filtersLayoutManager = new LinearLayoutManager(activity);
        filterAdapter = new FilterAdapter(activity, responseFiltersList);
        filters.setLayoutManager(filtersLayoutManager);
        filters.setAdapter(filterAdapter);
    }

    @OnClick(R.id.fragment_filters_tv_clear)
    public void clearClick() {
    }

    @OnClick(R.id.fragment_filters_tv_done)
    public void doneClick() {
        startPriceValue = pricesRange.getLeftPinValue();
        endPriceValue = pricesRange.getRightPinValue();
        for (FilterModel item : requestFilterList) {
            item.filterValues = null;
        }
        Navigator.loadFragment(activity, ProductsFragment.newInstance(activity, true, category), R.id.activity_main_fl_appContainer, true);
    }

    private void getFiltersApi(int categoryId) {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().GET_FILTERS(categoryId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        container.setVisibility(View.VISIBLE);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            responseFiltersList.clear();
                            requestFilterList.clear();
                            responseFiltersList.addAll(handleFiltersResponse(response));
                            requestFilterList.addAll(responseFiltersList);
                            filterAdapter.notifyDataSetChanged();
                        } else if (responseCode == 201) {
                            Log.d(Constants.MAZAD, "category Not Found");
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

    private ArrayList<FilterModel> handleFiltersResponse(Response<ResponseBody> response) {
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
        ArrayList<FilterModel> filters = null;
        if (outResponse != null) {
            outResponse = outResponse.replace("\"", "");
            outResponse = outResponse.replace("\n", "");
            Type type = new TypeToken<ArrayList<FilterModel>>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            filters = new Gson().fromJson(jsonResponse, type);
        }
        return filters;
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








