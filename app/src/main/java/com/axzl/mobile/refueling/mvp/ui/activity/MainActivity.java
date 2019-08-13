package com.axzl.mobile.refueling.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.di.component.DaggerMainComponent;
import com.axzl.mobile.refueling.mvp.contract.MainContract;
import com.axzl.mobile.refueling.mvp.presenter.MainPresenter;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
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
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:首页
 * ================================================
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    /**
     * 关闭滑动返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
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
        setSupportActionBar(toolbar);
        initMaterialDrawer(savedInstanceState);
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
                .withToolbar(toolbar)                                                               // 绑定ToolBar
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult)                                                    // 设置左侧头部标题
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withDescription(R.string.drawer_item_home_desc).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1).withSelectable(false),
                        new ExpandableBadgeDrawerItem().withName(R.string.drawer_item_lottery).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(18).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge("100").withSubItems(
                                new SecondaryDrawerItem().withName(R.string.drawer_item_lottery_LuckyMonkeyPane).withLevel(2).withIcon(FontAwesome.Icon.faw_empire).withIdentifier(2000),
                                new SecondaryDrawerItem().withName(R.string.drawer_item_lottery_Lottery).withLevel(2).withIcon(FontAwesome.Icon.faw_qq).withIdentifier(2001),
                                new SecondaryDrawerItem().withName(R.string.drawer_item_lottery_WheelSurf).withLevel(2).withIcon(FontAwesome.Icon.faw_cloudscale).withIdentifier(2002)
                        ),
                        new ExpandableDrawerItem().withName(R.string.drawer_item_icon).withIcon(GoogleMaterial.Icon.gmd_wallpaper).withIdentifier(19).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName(R.string.drawer_item_icon_FontAwesome).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_image).withIdentifier(2003),
                                new SecondaryDrawerItem().withName(R.string.drawer_item_icon_GoogleMaterial).withLevel(2).withIcon(FontAwesome.Icon.faw_google).withIdentifier(2004)
                        ),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_setting).withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(20).withSelectable(false)

                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    //check if the drawerItem is set.
                    //there are different reasons for the drawerItem to be null
                    //--> click on the header
                    //--> click on the footer
                    //those items don't contain a drawerItem

                    if (drawerItem instanceof Nameable) {
                        if (drawerItem.getIdentifier() == 2003) {                                   // Icon-FontAwesome
                            ActivityUtils.startActivity(FontAwesomeActivity.class);
                        } else if (drawerItem.getIdentifier() == 2004) {                            // Icon-GoogleMaterial
                            ActivityUtils.startActivity(GoogleMaterialActivity.class);
                        } else {
                            showMessage(((Nameable) drawerItem).getName().getText(getActivity()));
                        }
                    }

                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)                                                  // 第一次启动时默认打开抽屉
                .build();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(21, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

        result.updateBadge(4, new StringHolder(10 + ""));
    }

    @OnClick({
            R.id.btn_open_map,                                                                      // 打开地图
            R.id.btn_open_refueling,                                                                // 快捷加油
            R.id.btn_open_lottery                                                                   // 打开抽奖
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_open_map:
                ActivityUtils.startActivity(MapActivity.class);                                     // 打开地图
                break;
            case R.id.btn_open_refueling:                                                           // 快捷加油
                ActivityUtils.startActivity(SearchForGasStationsActivity.class);
                break;
            case R.id.btn_open_lottery:                                                             // 打开抽奖
                ActivityUtils.startActivity(MapActivity.class);
                break;
        }
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
//        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
