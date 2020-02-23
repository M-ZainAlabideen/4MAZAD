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
import app.mazad.adapters.NotificationsAdapter;
import app.mazad.classes.Constants;
import app.mazad.classes.GlobalFunctions;
import app.mazad.classes.Navigator;
import app.mazad.classes.SessionManager;
import app.mazad.webservices.RetrofitConfig;
import app.mazad.webservices.models.AuctionModel;
import app.mazad.webservices.models.NotificationModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {
    public static FragmentActivity activity;
    public static NotificationsFragment fragment;
    private View childView;
    private SessionManager sessionManager;

    private ArrayList<NotificationModel> notificationsList = new ArrayList<>();
    private LinearLayoutManager notificationsLayoutManager;
    private NotificationsAdapter notificationsAdapter;
    private int pageIndex = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @BindView(R.id.fragment_notifications_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_notifications_rv_notifications)
    RecyclerView notifications;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static NotificationsFragment newInstance(FragmentActivity activity) {
        fragment = new NotificationsFragment();
        NotificationsFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, false, false, true, getString(R.string.notifications));
        sessionManager = new SessionManager(activity);
        intiNotifications();
        if (notificationsList.size() == 0) {
            notificationsApi();
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    private void intiNotifications() {
        notificationsLayoutManager = new LinearLayoutManager(activity);
        notificationsAdapter = new NotificationsAdapter(activity, notificationsList);
        notifications.setLayoutManager(notificationsLayoutManager);
        notifications.setAdapter(notificationsAdapter);
    }

    private void notificationsApi() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().GET_NOTIFICATIONS_CALL(
                sessionManager.getUserToken(), sessionManager.getUserId(), pageIndex)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            container.setVisibility(View.VISIBLE);
                            notificationsList.addAll(handleNotificationsResponse(response));
                            notificationsAdapter.notifyDataSetChanged();
                            if (notificationsList.size() > 0) {
                                for (NotificationModel item : notificationsList) {
                                    if (!item.isRead) {
                                        updateNotificationsApi();
                                    }
                                }
                                notifications.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                    }

                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        if (!isLastPage) {
                                            int visibleItemCount = notificationsLayoutManager.getChildCount();

                                            int totalItemCount = notificationsLayoutManager.getItemCount();

                                            int pastVisibleItems = notificationsLayoutManager.findFirstVisibleItemPosition();

                                /*isLoading variable used for check if the user send many requests
                                for pagination(make many scrolls in the same time)
                                1- if isLoading true >> there is request already sent so,
                                no more requests till the response of last request coming
                                2- else >> send new request for load more data (News)*/
                                            if (!isLoading) {

                                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                                    isLoading = true;

                                                    pageIndex = pageIndex + 1;

                                                    getMoreNotification();

                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        } else if (responseCode == 203) {
                            Log.d(Constants.MAZAD, "user Id not found");
                        } else if (responseCode == 204) {
                            Snackbar.make(childView, getString(R.string.noNotification), Snackbar.LENGTH_SHORT).show();
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

    private void getMoreNotification() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().GET_NOTIFICATIONS_CALL(
                sessionManager.getUserToken(), sessionManager.getUserId(), pageIndex)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            if (handleNotificationsResponse(response).size() > 0) {
                                notificationsList.addAll(handleNotificationsResponse(response));
                                notificationsAdapter.notifyDataSetChanged();
                                for (NotificationModel item : notificationsList) {
                                    if (!item.isRead) {
                                        updateNotificationsApi();
                                    }
                                }
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

    private ArrayList<NotificationModel> handleNotificationsResponse(Response<ResponseBody> response) {
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
        ArrayList<NotificationModel> notificationsList = null;
        if (outResponse != null) {
            outResponse = outResponse.replace("\"", "");
            outResponse = outResponse.replace("\n", "");
            Type type = new TypeToken<ArrayList<NotificationModel>>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            notificationsList = new Gson().fromJson(jsonResponse, type);
        }
        return notificationsList;
    }

    private void updateNotificationsApi() {
        RetrofitConfig.getServices().UPDATE_NOTIFICATIONS_CALL(sessionManager.getUserToken(), sessionManager.getUserId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
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

