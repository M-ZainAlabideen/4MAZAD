package app.mazad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.classes.Navigator;
import app.mazad.classes.SessionManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountFragment extends Fragment {
    public static FragmentActivity activity;
    public static AccountFragment fragment;
    private SessionManager sessionManager;

    @BindView(R.id.fragment_account_iv_editProfileArrow)
    ImageView editProfileArrow;
    @BindView(R.id.fragment_account_iv_changePassArrow)
    ImageView changePassArrow;
    @BindView(R.id.fragment_account_iv_auctionsRequestsArrow)
    ImageView auctionsRequestsArrow;
    @BindView(R.id.fragment_account_iv_logoutArrow)
    ImageView logoutArrow;

    public static AccountFragment newInstance(FragmentActivity activity) {
        fragment = new AccountFragment();
        AccountFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, false, true, false,true,getString(R.string.myAccount));
        sessionManager = new SessionManager(activity);
        if(!MainActivity.isEnglish){
            editProfileArrow.setRotation(180);
            changePassArrow.setRotation(180);
            auctionsRequestsArrow.setRotation(180);
            logoutArrow.setRotation(180);
        }
    }

    @OnClick(R.id.fragment_account_v_editProfile)
    public void editProfileClick() {
        Navigator.loadFragment(activity, EditProfileFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_account_v_changePass)
    public void changePassClick() {
        Navigator.loadFragment(activity, ChangePasswordFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_account_v_auctionsRequests)
    public void auctionsRequestsClick() {
        Navigator.loadFragment(activity, AuctionsRequestsFragment.newInstance(activity), R.id.activity_main_fl_appContainer, true);
    }

    @OnClick(R.id.fragment_account_v_logout)
    public void logoutClick() {
        sessionManager.logout();
    }
}



