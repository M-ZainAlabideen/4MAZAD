package app.mazad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
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

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.classes.Constants;
import app.mazad.classes.FixControl;
import app.mazad.classes.GlobalFunctions;
import app.mazad.classes.Navigator;
import app.mazad.classes.SessionManager;
import app.mazad.webservices.RetrofitConfig;
import app.mazad.webservices.requests.LoginRequest;
import app.mazad.webservices.responses.AuthResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    public static FragmentActivity activity;
    public static LoginFragment fragment;
    private SessionManager sessionManager;
    private View childView;
    private String mobileTxt;
    private String passwordTxt;


    @BindView(R.id.fragment_login_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_login_il_mobileContainer)
    TextInputLayout mobileContainer;
    @BindView(R.id.fragment_login_il_passContainer)
    TextInputLayout passContainer;
    @BindView(R.id.fragment_login_et_mobile)
    TextInputEditText mobile;
    @BindView(R.id.fragment_login_et_pass)
    TextInputEditText pass;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static LoginFragment newInstance(FragmentActivity activity) {
        fragment = new LoginFragment();
        LoginFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(false, false, false, false, null);
        sessionManager = new SessionManager(activity);
        FixControl.setupUI(activity, container);
        loading.setVisibility(View.GONE);

        //make the input type passwordType programmatically because the passwordType in xml make the font not work
        pass.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @OnClick(R.id.fragment_login_tv_changeLang)
    public void changeLangCLick() {
        GlobalFunctions.changeLanguage(activity);
    }

    @OnClick(R.id.fragment_login_tv_forgetPass)
    public void forgetPassCLick() {
        Navigator.loadFragment(activity, ForgetPasswordFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_login_tv_signUp)
    public void signUpCLick() {
        Navigator.loadFragment(activity, SignUpFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_login_tv_asGuest)
    public void asGuestCLick() {
        Navigator.loadFragment(activity, HomeFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_login_tv_login)
    public void loginCLick() {
        checkMobile();
        checkPassword();

        if (mobileContainer.getError() == null && passContainer.getError() == null) {
            LoginRequest request = new LoginRequest();
            request.mobile = mobileTxt;
            request.password = passwordTxt;
            loginApi(request);
        }
    }

    private void checkMobile() {
        mobileTxt = mobile.getText().toString();
        if (mobileTxt == null || mobileTxt.isEmpty()) {
            mobileContainer.setError(getString(R.string.enterMobile));
        } else {
            if (!FixControl.isValidPhone(mobileTxt)) {
                mobileContainer.setError(getString(R.string.invalidMobile));
            } else {
                mobileContainer.setError(null);
            }
        }
    }

    private void checkPassword() {
        passwordTxt = pass.getText().toString();
        if (passwordTxt == null || passwordTxt.isEmpty()) {
            passContainer.setError(getString(R.string.enterPassword));
        } else {
            if (passwordTxt.length() < 6) {
                passContainer.setError(getString(R.string.invalidPass));
            } else {
                passContainer.setError(null);
            }
        }
    }

    private void loginApi(LoginRequest request) {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().LOGIN_CALL(request)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            AuthResponse authentication = handleLoginResponse(response);
                            sessionManager.setUserToken(authentication.token);
                            sessionManager.setUserId(authentication.user.id);
                            sessionManager.setPhoneNumber(authentication.user.mobile);
                            if (authentication.isSubscribe) {
                                sessionManager.setPackage(true);
                            } else {
                                sessionManager.setPackage(false);
                            }
                            if (sessionManager.isGuest()) {
                                sessionManager.guestLogout();
                            }
                            sessionManager.LoginSession();
                            if (authentication.user.isActive) {
                                Navigator.loadFragment(activity, HomeFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
                            } else {
                                Navigator.loadFragment(activity, VerificationFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
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

    private AuthResponse handleLoginResponse(Response<ResponseBody> response) {
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
        AuthResponse authentication = null;
        if (outResponse != null) {
            outResponse = outResponse.replace("\"", "");
            outResponse = outResponse.replace("\n", "");
            Type type = new TypeToken<AuthResponse>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            authentication = new Gson().fromJson(jsonResponse, type);
        }
        return authentication;
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
