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
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.classes.FixControl;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileFragment extends Fragment {
    public static FragmentActivity activity;
    public static EditProfileFragment fragment;

    @BindView(R.id.fragment_edit_profile_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_edit_profile_il_emailContainer)
    TextInputLayout emailContainer;
    @BindView(R.id.fragment_edit_profile_il_nameContainer)
    TextInputLayout nameContainer;
    @BindView(R.id.fragment_edit_profile_il_mobileContainer)
    TextInputLayout mobileContainer;
    @BindView(R.id.fragment_edit_profile_ccp_countryCode)
    CountryCodePicker countryCode;
    @BindView(R.id.fragment_edit_profile_et_email)
    TextInputEditText email;
    @BindView(R.id.fragment_edit_profile_et_name)
    TextInputEditText name;
    @BindView(R.id.fragment_edit_profile_et_mobile)
    TextInputEditText mobile;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static EditProfileFragment newInstance(FragmentActivity activity) {
        fragment = new EditProfileFragment();
        EditProfileFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, true, false, false,false,getString(R.string.editProfile));
        FixControl.setupUI(activity,container);
    }

    @OnClick(R.id.fragment_edit_profile_tv_save)
    public void saveCLick() {
    }
}