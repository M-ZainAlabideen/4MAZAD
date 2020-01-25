package app.mazad.fragments;

import android.os.Bundle;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordFragment extends Fragment {
    public static FragmentActivity activity;
    public static ChangePasswordFragment fragment;

    @BindView(R.id.fragment_change_password_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_change_password_il_oldPassContainer)
    TextInputLayout oldPassContainer;
    @BindView(R.id.fragment_change_password_il_newPassContainer)
    TextInputLayout newPassContainer;
    @BindView(R.id.fragment_change_password_il_confirmPassContainer)
    TextInputLayout confirmPassContainer;
    @BindView(R.id.fragment_change_password_et_oldPass)
    TextInputEditText oldPass;
    @BindView(R.id.fragment_change_password_et_newPass)
    TextInputEditText newPass;
    @BindView(R.id.fragment_change_password_et_confirmPass)
    TextInputEditText confirmPass;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static ChangePasswordFragment newInstance(FragmentActivity activity) {
        fragment = new ChangePasswordFragment();
        ChangePasswordFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, true, false, false,false,getString(R.string.changePassword));
        FixControl.setupUI(activity,container);
    }

    @OnClick(R.id.fragment_change_password_tv_save)
    public void saveCLick() {
    }
}





