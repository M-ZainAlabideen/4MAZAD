package app.mazad.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.classes.FixControl;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends Fragment {
    public static FragmentActivity activity;
    public static SignUpFragment fragment;

    @BindView(R.id.fragment_sign_up_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_sign_up_il_emailContainer)
    TextInputLayout emailContainer;
    @BindView(R.id.fragment_sign_up_il_nameContainer)
    TextInputLayout nameContainer;
    @BindView(R.id.fragment_sign_up_il_mobileContainer)
    TextInputLayout mobileContainer;
    @BindView(R.id.fragment_sign_up_ccp_countryCode)
    CountryCodePicker countryCode;
    @BindView(R.id.fragment_sign_up_il_passContainer)
    TextInputLayout passContainer;
    @BindView(R.id.fragment_sign_up_il_confirmPassContainer)
    TextInputLayout confirmPassContainer;
    @BindView(R.id.fragment_sign_up_et_email)
    TextInputEditText email;
    @BindView(R.id.fragment_sign_up_et_name)
    TextInputEditText name;
    @BindView(R.id.fragment_sign_up_et_mobile)
    TextInputEditText mobile;
    @BindView(R.id.fragment_sign_up_et_pass)
    TextInputEditText pass;
    @BindView(R.id.fragment_sign_up_et_confirmPass)
    TextInputEditText confirmPass;
    @BindView(R.id.fragment_sign_up_cb_agree)
    CheckBox agree;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static SignUpFragment newInstance(FragmentActivity activity) {
        fragment = new SignUpFragment();
        SignUpFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(false, false, false, false,false,null);
        FixControl.setupUI(activity,container);
        loading.setVisibility(View.GONE);

        //make the input type passwordType programmatically because the passwordType in xml make the font not work
        pass.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //make the input type passwordType programmatically because the passwordType in xml make the font not work
        confirmPass.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());


        countryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                Toast.makeText(getContext(), "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.fragment_sign_up_iv_frontCivilId)
    public void frontCivilIdClick() {
    }

    @OnClick(R.id.fragment_sign_up_iv_backCivilId)
    public void backCivilIdClick() {
    }

    @OnClick(R.id.fragment_sign_up_iv_signUpBack)
    public void backClick() {
    }

    @OnClick(R.id.fragment_sign_up_tv_termsAndConditions)
    public void termsAndConditionsClick() {
    }

    @OnClick(R.id.fragment_sign_up_tv_changeLang)
    public void changeLangClick() {
    }

    @OnClick(R.id.fragment_sign_up_tv_signUp)
    public void signUpClick() {
    }
}
