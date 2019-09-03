package com.axzl.mobile.refueling.mvp.ui.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.app.global.AccountManager;
import com.axzl.mobile.refueling.app.global.Constant;
import com.axzl.mobile.refueling.app.utils.CommonUtils;
import com.axzl.mobile.refueling.di.component.DaggerMainComponent;
import com.axzl.mobile.refueling.mvp.contract.MainContract;
import com.axzl.mobile.refueling.mvp.presenter.MainPresenter;
import com.axzl.mobile.refueling.mvp.ui.fragment.HomeFragment;
import com.axzl.mobile.refueling.mvp.ui.fragment.SettingFragment;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.cj.colorful.Colorful;
import com.jess.arms.cj.colorful.setter.ViewGroupSetter;
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
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import javax.inject.Inject;

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

    @Inject
    AccountManager mAccountManager;

    /**
     * 切换主题对象
     */
    private Colorful colorful;

    /**
     * 首页
     */
    private HomeFragment homeFragment;
    /**
     * 设置界面
     */
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentFragment = null;
        homeFragment = null;
        settingFragment = null;

        colorful = null;
        mAccountManager = null;
        headerResult = null;
        result = null;
    }

    /**
     * 关闭滑动返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * 夜间模式
     */
    private OnCheckedChangeListener onCheckedChangeListener = (drawerItem, buttonView, isChecked) -> {
        if (drawerItem instanceof Nameable) {

            // 切换主题
            if (isChecked)
                animChangeColor(R.style.AppNightTheme);
            else
                animChangeColor(R.style.AppDayTheme);

            // 刷新状态栏
            setStatusBar();
            // 保存切换结果
            mAccountManager.setNight(isChecked);

            // 侧滑 - 头部-新增账户的Icon
            headerResult.getProfiles().get(headerResult.getProfiles().size() - 2).withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(mAccountManager.getNight() ? R.color.material_drawer_dark_primary_text : R.color.material_drawer_primary_text));

            // 侧滑 - 头部-小三角的颜色
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(com.mikepenz.materialdrawer.R.attr.material_drawer_header_selection_text, typedValue, true);
            ImageView mAccountSwitcherArrow = (ImageView)headerResult.getView().findViewById(R.id.material_drawer_account_header_text_switcher);
            mAccountSwitcherArrow.setImageDrawable(new IconicsDrawable(this, MaterialDrawerFont.Icon.mdf_arrow_drop_down).sizeRes(R.dimen.material_drawer_account_header_dropdown).paddingRes(R.dimen.material_drawer_account_header_dropdown_padding).color(typedValue.data));

            // 侧滑 - 刷新MateriaDrawer列表
            result.getAdapter().notifyAdapterDataSetChanged();
        } else {
            showMessage("toggleChecked: " + isChecked);
        }
    };

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
        // 绑定Toolbar
        setSupportActionBar(mToolbar);
        // 初始化Menu侧滑
        initMaterialDrawer(savedInstanceState);
        // 初始化Fragment
        initFragment();
        // 初始化Colorful
        setupColorful();
        // 初始化业务逻辑
        mPresenter.initPresenter();
    }

    /**
     * 初始化
     */
    private void setupColorful() {
        ViewGroupSetter toolbarSetter = new ViewGroupSetter(mToolbar, R.attr.colorPrimary);
        ViewGroupSetter recyclerViewSetter = new ViewGroupSetter(result.getRecyclerView(), R.attr.material_drawer_background);
        recyclerViewSetter.childViewTextColor(com.mikepenz.materialdrawer.R.id.material_drawer_account_header_name, R.attr.material_drawer_header_selection_text);
        recyclerViewSetter.childViewTextColor(com.mikepenz.materialdrawer.R.id.material_drawer_account_header_email, R.attr.material_drawer_header_selection_text);

        colorful = new Colorful.Builder(this)
                .setter(toolbarSetter)
                .setter(recyclerViewSetter)
                .create();
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
                .addProfiles(
                        profile,
//                        profile2,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName(R.string.drawer_item_AddAccount).withDescription(R.string.drawer_item_AddAccount_description).withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(mAccountManager.getNight() ? R.color.material_drawer_dark_primary_text : R.color.material_drawer_primary_text)).withIdentifier(Constant.MAIN_ADDACCOUNT),
                        new ProfileSettingDrawerItem().withName(R.string.drawer_item_ManageAccount).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(Constant.MAIN_MANAGEACCOUNT)
                )
                .withOnAccountHeaderListener((view, profile1, current) -> {
                    //sample usage of the onProfileChanged listener
                    //if the clicked item has the identifier 1 add a new profile ;)
                    if (profile1 instanceof IDrawerItem) {
                        if (profile1.getIdentifier() == Constant.MAIN_ADDACCOUNT) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile6).withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        } else
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
                                new SecondaryDrawerItem().withName(R.string.drawer_item_icon_FontAwesome).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_wallpaper).withIdentifier(Constant.MAIN_ICON_FONTAWESOME).withSelectable(false),
                                new SecondaryDrawerItem().withName(R.string.drawer_item_icon_GoogleMaterial).withLevel(2).withIcon(FontAwesome.Icon.faw_google).withIdentifier(Constant.MAIN_ICON_GOOGLEMATERIAL).withSelectable(false),
                                new SecondaryDrawerItem().withName(R.string.drawer_item_icon_Octicons).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_data_usage).withIdentifier(Constant.MAIN_ICON_OCTICONS).withSelectable(false)
                        ),
                        new DividerDrawerItem(),
                        new SwitchDrawerItem().withName(R.string.drawer_item_NightMode).withIcon(R.mipmap.sunny_night_day).withIdentifier(Constant.MAIN_NIGHTMODE).withChecked(mAccountManager.getNight()).withSelectable(false).withOnCheckedChangeListener(onCheckedChangeListener),
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
                        if (drawerItem.getIdentifier() == Constant.MAIN_NIGHTMODE) {
                            // 切换 夜间模式 时不关闭Drawer
                            return true;
                        } else
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
                    .withActivityTheme(mAccountManager.getNight() ? R.style.AppNightTheme : R.style.AppDayTheme)                                               // 设置主题
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


    /**
     * 给夜间模式增加一个动画,颜色渐变
     *
     * @param newTheme
     */
    private void animChangeColor(final int newTheme) {
        final View rootView = getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);

        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        if (null != localBitmap && rootView instanceof ViewGroup) {
            final View tmpView = new View(this);
            tmpView.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) rootView).addView(tmpView, params);
            tmpView.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    colorful.setTheme(newTheme);
                    System.gc();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) rootView).removeView(tmpView);
                    localBitmap.recycle();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }
}
