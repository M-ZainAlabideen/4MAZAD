package app.mazad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import app.mazad.classes.Constants;
import app.mazad.classes.FixControl;
import app.mazad.classes.GlobalFunctions;
import app.mazad.classes.Navigator;
import app.mazad.classes.SessionManager;
import app.mazad.webservices.RetrofitConfig;
import app.mazad.webservices.models.ContactModel;
import app.mazad.webservices.requests.SendMessageRequest;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsFragment extends Fragment {
    public static FragmentActivity activity;
    public static ContactUsFragment fragment;
    private  View childView;
    private SessionManager sessionManager;
    private ArrayList<ContactModel> contacts = new ArrayList<>();
    private String twitter;
    private String facebook;
    private String instagram;
    private String youtube;

    private String emailTxt;
    private String subjectTxt;
    private String messageTxt;

    @BindView(R.id.fragment_contact_us_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_contact_us_il_emailContainer)
    TextInputLayout emailContainer;
    @BindView(R.id.fragment_contact_us_il_messageContainer)
    TextInputLayout messageContainer;
    @BindView(R.id.fragment_contact_us_il_subjectContainer)
    TextInputLayout subjectContainer;
    @BindView(R.id.fragment_contact_us_et_email)
    TextInputEditText email;
    @BindView(R.id.fragment_contact_us_et_message)
    TextInputEditText message;
    @BindView(R.id.fragment_contact_us_et_subject)
    TextInputEditText subject;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static ContactUsFragment newInstance(FragmentActivity activity) {
        fragment = new ContactUsFragment();
        ContactUsFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, true, false, false, getString(R.string.contactUs));
        sessionManager = new SessionManager(activity);
        FixControl.setupUI(activity, container);
        loading.setVisibility(View.GONE);
        if (contacts.size() == 0) {
            contactsApi();
        }
    }

    @OnClick(R.id.fragment_contact_us_iv_facebook)
    public void facebookCLick() {
        Navigator.loadFragment(activity, UrlsFragment.newInstance(activity, "https://www.google.com", false,null), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_contact_us_iv_instagram)
    public void instagramCLick() {
        Navigator.loadFragment(activity, UrlsFragment.newInstance(activity, instagram, false,null), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_contact_us_iv_twitter)
    public void twitterCLick() {
        Navigator.loadFragment(activity, UrlsFragment.newInstance(activity, twitter, false,null), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_contact_us_iv_youtube)
    public void youtubeCLick() {
        Navigator.loadFragment(activity, UrlsFragment.newInstance(activity, youtube, false,null), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_contact_us_tv_send)
    public void sendCLick() {
        emailTxt = email.getText().toString();
        subjectTxt = subject.getText().toString();
        messageTxt = message.getText().toString();
        if (emailTxt == null || emailTxt.isEmpty()) {
            emailContainer.setError(getString(R.string.enterEmail));
        } else {
        }
        if (subjectTxt == null || subjectTxt.isEmpty()) {
            subjectContainer.setError(getString(R.string.enterSubject));
        } else {
        }
        if (messageTxt == null || messageTxt.isEmpty()) {
            messageContainer.setError(getString(R.string.enterMessage));
        } else {
            SendMessageRequest request = new SendMessageRequest();
            request.email = emailTxt;
            request.name = subjectTxt;
            request.message = messageTxt;
            sendMessageApi(request);
        }
    }

    private void contactsApi() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().CONTACTS_CALL().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.setVisibility(View.GONE);
                GlobalFunctions.EnableLayout(container);
                int responseCode = response.code();
                if (responseCode == 200) {
                    handleResponse(response);
                    if (contacts.size() > 0) {
                        for (ContactModel item : contacts) {
                            if (item.name.equals(Constants.TWITTER)) {
                                twitter = item.value;
                            } else if (item.name.equals(Constants.FACEBOOK)) {
                                facebook = item.value;
                            } else if (item.name.equals(Constants.INSTAGRAM)) {
                                instagram = item.value;
                            } else if (item.name.equals(Constants.YOUTUBE)) {
                                youtube = item.value;
                            }
                        }
                    }
                } else if (responseCode == 400) {
                    GlobalFunctions.generalErrorMessage(activity,childView,loading);
                } else if (responseCode == 401) {
                    showSessionTimeOutAlert();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                GlobalFunctions.EnableLayout(container);
                GlobalFunctions.generalErrorMessage(activity,childView,loading);
            }
        });
    }

    private void sendMessageApi(SendMessageRequest sendMessageRequest) {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().SEND_MESSAGE_CALL(sendMessageRequest)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            Snackbar.make(loading, getString(R.string.thanksMessage), Snackbar.LENGTH_LONG).show();
                            emailContainer.setError(null);
                            subjectContainer.setError(null);
                            messageContainer.setError(null);
                            email.setText("");
                            subject.setText("");
                            message.setText("");
                        } else if (responseCode == 400) {
                            Snackbar.make(loading, getString(R.string.generalError), Snackbar.LENGTH_SHORT).show();
                        } else if (responseCode == 401) {
                            //unauthorized
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        GlobalFunctions.EnableLayout(container);
                        GlobalFunctions.generalErrorMessage(activity,childView,loading);
                    }
                });
    }

    private ArrayList<ContactModel> handleResponse(Response<ResponseBody> response) {
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
        if (outResponse != null) {
            outResponse = outResponse.replace("\"", "");
            outResponse = outResponse.replace("\n", "");
            Type type = new TypeToken<ArrayList<ContactModel>>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            contacts.clear();
            contacts = new Gson().fromJson(jsonResponse, type);
        }
        return contacts;
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





