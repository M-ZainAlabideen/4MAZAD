package app.mazad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.classes.FixControl;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationFragment extends Fragment {
    public static FragmentActivity activity;
    public static VerificationFragment fragment;

    @BindView(R.id.fragment_verification_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_verification_tv_mobileNumber)
    TextView mobileNumber;
    @BindView(R.id.fragment_verification_et_first)
    EditText first;
    @BindView(R.id.fragment_verification_et_second)
    EditText second;
    @BindView(R.id.fragment_verification_et_third)
    EditText third;
    @BindView(R.id.fragment_verification_et_forth)
    EditText forth;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static VerificationFragment newInstance(FragmentActivity activity) {
        fragment = new VerificationFragment();
        VerificationFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_verification, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(false, false, false, false, false,null);
        FixControl.setupUI(activity, container);
    }

    @OnClick(R.id.fragment_verification_iv_verificationBack)
    public void backClick() {
    }

    @OnClick(R.id.fragment_verification_ll_resendCode)
    public void resendCodeClick() {
    }

    @OnClick(R.id.fragment_verification_tv_verify)
    public void verifyClick() {
    }

}




