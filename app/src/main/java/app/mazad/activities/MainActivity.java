package app.mazad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.mazad.R;
import app.mazad.classes.FixControl;
import app.mazad.classes.GlobalFunctions;
import app.mazad.classes.LocaleHelper;
import app.mazad.classes.Navigator;
import app.mazad.classes.SessionManager;
import app.mazad.fragments.AuctionDetailsFragment;
import app.mazad.fragments.CategoriesFragment;
import app.mazad.fragments.HomeFragment;
import app.mazad.fragments.LoginFragment;
import app.mazad.fragments.MoreFragment;
import app.mazad.fragments.NotificationsFragment;
import app.mazad.fragments.PackagesFragment;
import app.mazad.fragments.SignUpFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {

    public static ConstraintLayout topAppbar;
    public static BottomNavigationView bottomAppbar;
    public static ImageView back;
    public static ImageView sort;
    public static ImageView filter;
    public static SearchView search;
    public static TextView title;

    public static boolean isEnglish = true;
    private SessionManager sessionManager;

    @BindView(R.id.activity_main_cl_activityContainer)
    ConstraintLayout activityContainer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.onAttach(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topAppbar = findViewById(R.id.activity_main_cl_topAppbar);
        bottomAppbar = (BottomNavigationView) findViewById(R.id.activity_main_bnv_bottomAppbar);

        back = findViewById(R.id.activity_main_iv_back);
        sort = findViewById(R.id.activity_main_iv_sort);
        filter = findViewById(R.id.activity_main_iv_filter);
        search = findViewById(R.id.activity_main_sv_search);
        title = findViewById(R.id.activity_main_tv_title);

        setupBottomAppbar();
        ButterKnife.bind(this);
        GlobalFunctions.setDefaultLanguage(this);
        GlobalFunctions.setUpFont(this);
        FixControl.setupUI(this, activityContainer);
        sessionManager = new SessionManager(this);
        if (!sessionManager.isGuest() && !sessionManager.isLoggedIn()) {
            Navigator.loadFragment(this, LoginFragment.newInstance(this), R.id.activity_main_fl_appContainer, true);
        } else {
            Navigator.loadFragment(this, HomeFragment.newInstance(this), R.id.activity_main_fl_appContainer, true);
        }
    }

    //the back button action of all the app
    @OnClick(R.id.activity_main_iv_back)
    public void back() {
        if (!search.isIconified()) {
            search.onActionViewCollapsed();
        } else {
            onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!search.isIconified()) {
            search.onActionViewCollapsed();
        } else {
            onBackPressed();
        }
        return true;
    }

    /*this function used for initialization and setUp topAppbar for each screen
     * for example: if title = true and sideMenu = false >> this mean that this screen has title but not has sideMenu and so on
     * */
    public static void setupAppbar(boolean hasTopAppbar, boolean hasBack, boolean isProductPage, boolean hasBottomAppbar, String titleStr) {
        if (hasTopAppbar) {
            topAppbar.setVisibility(View.VISIBLE);
            if (hasBack) {
                back.setVisibility(View.VISIBLE);
            } else {
                back.setVisibility(View.GONE);
            }
            if (isProductPage) {
                sort.setVisibility(View.VISIBLE);
                filter.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
            } else {
                sort.setVisibility(View.GONE);
                filter.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
            }
            title.setText(titleStr);
        } else {
            topAppbar.setVisibility(View.GONE);
        }
        if (hasBottomAppbar) {
            bottomAppbar.setVisibility(View.VISIBLE);
        } else {
            bottomAppbar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("RestrictedApi")
    private void setupBottomAppbar() {
        if (bottomAppbar != null) {
            Menu menu = bottomAppbar.getMenu();
            selectFragment(menu.getItem(0));
            bottomAppbar.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            selectFragment(item);
                            return false;
                        }
                    });
        }
//        else {
//            HomeFragment homeFragment = HomeFragment.newInstance(this);
//            Navigator.loadFragment(this, homeFragment, R.id.activity_main_fl_appContainer, true);
//        }
    } // setup bottom navigation click listener

    public void selectFragment(MenuItem item) {
        item.setChecked(true);
//=======================================================================================================================
        switch (item.getItemId()) {
            // Home screen
            case R.id.bottom_navigation_menu_home:
                HomeFragment homeFragment = HomeFragment.newInstance(this);
                Navigator.loadFragment(this, homeFragment, R.id.activity_main_fl_appContainer, true);
                break;
//=======================================================================================================================
            // Categories screen
            case R.id.bottom_navigation_menu_categories:
                CategoriesFragment categoriesFragment = CategoriesFragment.newInstance(this);
                Navigator.loadFragment(this, categoriesFragment, R.id.activity_main_fl_appContainer, true);
                break;
//=======================================================================================================================
            // Notifications screen
            case R.id.bottom_navigation_menu_notifications:
                if (!sessionManager.isLoggedIn()) {
                    LoginFragment loginFragment = LoginFragment.newInstance(this);
                    Navigator.loadFragment(this, loginFragment, R.id.activity_main_fl_appContainer, true);
                } else {
                    NotificationsFragment notificationsFragment = NotificationsFragment.newInstance(this);
                    Navigator.loadFragment(this, notificationsFragment, R.id.activity_main_fl_appContainer, true);
                }
                break;
//=======================================================================================================================
            // More screen
            case R.id.bottom_navigation_menu_more:
                MoreFragment moreFragment = MoreFragment.newInstance(this);
                Navigator.loadFragment(this, moreFragment, R.id.activity_main_fl_appContainer, true);
                break;
//=======================================================================================================================
        }
    } // select bottom navigation fragments when clicked
}
