package com.axzl.mobile.refueling.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.app.global.Constant;
import com.axzl.mobile.refueling.app.utils.CommonUtils;
import com.axzl.mobile.refueling.di.component.DaggerMainComponent;
import com.axzl.mobile.refueling.mvp.contract.MainContract;
import com.axzl.mobile.refueling.mvp.presenter.MainPresenter;
import com.axzl.mobile.refueling.mvp.ui.fragment.HomeFragment;
import com.axzl.mobile.refueling.mvp.ui.fragment.SettingFragment;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.Preconditions;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:首页
 * ================================================
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    @BindView(R.id.toba_mainactivity_toolbar)
    Toolbar mToolbar;

    private HomeFragment homeFragment;
    private SettingFragment settingFragment;

    /**
     * 当前首页内容
     */
    private Fragment currentFragment = new Fragment();
    /**
     * 侧滑Menu 之 头部 对象
     */
    private AccountHeader headerResult = null;
    /**
     * 侧滑Menu 之 内容 对象
     */
    private Drawer result = null;

    /**
     * 关闭滑动返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * 给状态栏设置颜色
     */
    @Override
    public int useStatusBarColor() {
        return R.color.colorPrimary;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.initPresenter();
        setSupportActionBar(mToolbar);
        initMaterialDrawer(savedInstanceState);
        initFragment();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        homeFragment = HomeFragment.newInstance();
        settingFragment = SettingFragment.newInstance();

        onTabSelected(Constant.MAIN_HOME);
    }

    /**
     * 初始化Menu
     */
    private void initMaterialDrawer(@Nullable Bundle savedInstanceState) {

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(100);
//        final IProfile profile2 = new ProfileDrawerItem().withName("Demo User").withEmail("demo@github.com").withIcon("https://avatars2.githubusercontent.com/u/3597376?v=3&s=460").withIdentifier(101);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withTextColor(Color.BLACK)
                .addProfiles(
                        profile,
//                        profile2,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName("新增帐户").withDescription("添加新的GitHub帐户").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(100000),
                        new ProfileSettingDrawerItem().withName("管理帐户").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001)
                )
                .withOnAccountHeaderListener((view, profile1, current) -> {
                    //sample usage of the onProfileChanged listener
                    //if the clicked item has the identifier 1 add a new profile ;)
                    if (profile1 instanceof IDrawerItem) {
                        showMessage(profile1.getName().getText(getActivity()));
                    }
                    //false if you have not consumed the event and it should close the drawer
                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)                                                              // 绑定ToolBar
                .withHasStableIds(true)
                .withTranslucentStatusBar(false)                                                    // 设置半透明statusBar模式
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult)                                                    // 设置左侧头部标题
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(Constant.MAIN_HOME),
                        new ExpandableBadgeDrawerItem().withName(R.string.drawer_item_lottery).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(999).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge("100").withSubItems(
                                new SecondaryDrawerItem().withName(R.string.drawer_item_lottery_LuckyMonkeyPane).withLevel(2).withIcon(FontAwesome.Icon.faw_empire).withIdentifier(Constant.MAIN_LOTTERY_WHEELFORTUNE),
                                new SecondaryDrawerItem().withName(R.string.drawer_item_lottery_Lottery).withLevel(2).withIcon(FontAwesome.Icon.faw_qq).withIdentifier(Constant.MAIN_LOTTERY_QQ),
                                new SecondaryDrawerItem().withName(R.string.drawer_item_lottery_WheelSurf).withLevel(2).withIcon(FontAwesome.Icon.faw_cloudscale).withIdentifier(Constant.MAIN_LOTTERY_WHEELSURF)
                        ),
                        new ExpandableDrawerItem().withName(R.string.drawer_item_icon).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(998).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName(R.string.drawer_item_icon_FontAwesome).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_wallpaper).withIdentifier(Constant.MAIN_ICON_FONTAWESOME),
                                new SecondaryDrawerItem().withName(R.string.drawer_item_icon_GoogleMaterial).withLevel(2).withIcon(FontAwesome.Icon.faw_google).withIdentifier(Constant.MAIN_ICON_GOOGLEMATERIAL),
                                new SecondaryDrawerItem().withName(R.string.drawer_item_icon_Octicons).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_data_usage).withIdentifier(Constant.MAIN_ICON_OCTICONS)
                        ),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_setting).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(Constant.MAIN_SETTING),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about).withIcon(FontAwesome.Icon.faw_info).withIdentifier(Constant.MAIN_ABOUT).withSelectable(false)

                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    //check if the drawerItem is set.
                    //there are different reasons for the drawerItem to be null
                    //--> click on the header
                    //--> click on the footer
                    //those items don't contain a drawerItem

                    if (drawerItem instanceof Nameable) {
                        onTabSelected(drawerItem.getIdentifier());
                    }

                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)                                                  // 第一次启动时默认打开抽屉
                .build();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(1, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

    }

    /**
     * 切换标签(Fragment)
     */
    private void onTabSelected(long position) {
        if (position == Constant.MAIN_HOME) {                                                       // 首页
            setTitle(R.string.drawer_item_home);
            switchFragment(homeFragment).commit();
        } else if (position == Constant.MAIN_LOTTERY_WHEELFORTUNE) {                                // 抽奖之幸运转盘

        } else if (position == Constant.MAIN_LOTTERY_QQ) {                                          // 抽奖之QQ积分

        } else if (position == Constant.MAIN_LOTTERY_WHEELSURF) {                                   // 抽奖之原生转盘

        } else if (position == Constant.MAIN_ICON_FONTAWESOME) {                                    // Icon-FontAwesome
            ActivityUtils.startActivity(FontAwesomeActivity.class);
        } else if (position == Constant.MAIN_ICON_GOOGLEMATERIAL) {                                 // Icon-GoogleMaterial
            ActivityUtils.startActivity(GoogleMaterialActivity.class);
        } else if (position == Constant.MAIN_ICON_OCTICONS) {                                       // Icon-Octicons
            ActivityUtils.startActivity(OcticonsActivity.class);
        } else if (position == Constant.MAIN_SETTING) {                                             // 设置
            setTitle(R.string.drawer_item_setting);
            switchFragment(settingFragment).commit();
        } else if (position == Constant.MAIN_ABOUT) {                                               // 关于
            ActivityUtils.startActivity(new LibsBuilder()
                    .withFields(R.string.class.getFields())
                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withAboutIconShown(true)                                                       // 显示图标
                    .withAboutVersionShown(true)                                                    // 显示版本
                    .withAboutDescription(ArmsUtils.getString(getActivity(), R.string.drawer_item_about_description))                   // 关于描述
                    .withActivityTitle(ArmsUtils.getString(getApplicationContext(), R.string.drawer_item_about))                                                      // 标题
                    .intent(getActivity()));
        }
    }

    /**
     * fragment的切换
     */
    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Preconditions.checkNotNull(targetFragment);

        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.frame_container, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            CommonUtils.exitSys(getActivity());
        }
    }
}
