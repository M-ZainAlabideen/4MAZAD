package app.mazad.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.classes.FixControl;
import app.mazad.classes.GlobalFunctions;
import app.mazad.classes.Navigator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {
    public static FragmentActivity activity;
    public static LoginFragment fragment;

    @BindView(R.id.fragment_login_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_login_il_emailMobileContainer)
    TextInputLayout emailMobileContainer;
    @BindView(R.id.fragment_login_il_passContainer)
    TextInputLayout passContainer;
    @BindView(R.id.fragment_login_et_emailMobile)
    TextInputEditText emailMobile;
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
        View childView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(false, false, false, false, false,null);
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
    }

    @OnClick(R.id.fragment_login_tv_login)
    public void loginCLick() {

    }

    @OnClick(R.id.fragment_login_tv_signUp)
    public void signUpCLick() {
        Navigator.loadFragment(activity, SignUpFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_login_tv_asGuest)
    public void asGuestCLick() {
        Navigator.loadFragment(activity, HomeFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
    }
}
