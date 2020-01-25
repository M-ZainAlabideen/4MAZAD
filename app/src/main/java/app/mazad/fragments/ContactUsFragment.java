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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsFragment extends Fragment {
    public static FragmentActivity activity;
    public static ContactUsFragment fragment;

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
        View childView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, true, false, false,false,getString(R.string.contactUs));
        FixControl.setupUI(activity,container);
    }

    @OnClick(R.id.fragment_contact_us_iv_facebook)
    public void facebookCLick() {
    }

    @OnClick(R.id.fragment_contact_us_iv_instagram)
    public void instagramCLick() {
    }

    @OnClick(R.id.fragment_contact_us_iv_twitter)
    public void twitterCLick() {
    }

    @OnClick(R.id.fragment_contact_us_iv_youtube)
    public void youtubeCLick() {
    }

    @OnClick(R.id.fragment_contact_us_tv_save)
    public void saveCLick() {
    }
}





