package com.axzl.mobile.refueling.app.utils;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.app.utils.chinesecalendar.Consts;
import com.jess.arms.cj.colorful.Colorful;
import com.jess.arms.utils.ArmsUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 包名： com.zqw.mobile.aptitude.app.utils
 * 对象名： CommonUtils
 * 描述：第三方没有的工具这里都有
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2018/6/8 16:15
 */

public class CommonUtils {

    /**
     * 给夜间模式增加一个动画,颜色渐变
     *
     * @param newTheme
     */
    public static void animChangeColor(Context mContext, Colorful colorful, final int newTheme) {
        final View rootView = ((Activity)mContext).getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);

        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        if (null != localBitmap && rootView instanceof ViewGroup) {
            final View tmpView = new View(mContext);
            tmpView.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), localBitmap));
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

    /// <summary>
    /// 测试某位是否为真
    /// </summary>
    /// <param name="num"></param>
    /// <param name="bitpostion"></param>
    /// <returns></returns>
    public static boolean BitTest32(int num, int bitpostion) throws Exception {

        if ((bitpostion > 31) || (bitpostion < 0))
            throw new Exception("Error Param: bitpostion[0-31]:" + bitpostion);

        int bit = 1 << bitpostion;

        if ((num & bit) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static final int daysBetween(Date late, Date early) {

        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
//        System.out.println("1  "+caled.getTime().getTime()/1000+"  "+calst.getTime().getTime()/1000);
//        System.out.println("2  "+(caled.getTime().getTime()/1000-calst.getTime().getTime()/1000)/ 3600 / 24);
        //得到两个日期相差的天数
        int days = (int) (((caled.getTime().getTime() / 1000) - (calst.getTime().getTime() / 1000)) / 3600 / 24);
//        System.out.println("3  "+late.toString()+"  "+early.toString());
        return days;
    }

    //计算两个日期相差年数
    public static int yearDateDiff(Date date2, Date date1) {
        Calendar calBegin = Calendar.getInstance(); //获取日历实例
        Calendar calEnd = Calendar.getInstance();
        calBegin.setTime(date1); //字符串按照指定格式转化为日期
        calEnd.setTime(date2);
        return calEnd.get(Calendar.YEAR) - calBegin.get(Calendar.YEAR);
    }

    /// <summary>
    /// 将0-9转成汉字形式
    /// </summary>
    /// <param name="n"></param>
    /// <returns></returns>
    public static String ConvertNumToChineseNum(char nn) {
        int i = Integer.parseInt(nn + "");
        if ((i < 0) || (i > 9)) return "";
        return Consts.HZNum[i] + "";

    }

    /**
     * 获取assets的images目录中图片数量
     */
    public static int getAssetListCount(Context mContext) {
        int imgCount = 0;
        AssetManager assetManager = mContext.getAssets();
        try {
            // -2 代表 减去android-logo-shine和android-logo-mask
            imgCount = Objects.requireNonNull(assetManager.list("images")).length - 2;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgCount;
    }

    /**
     * 验证是否为浮点数
     *
     * @param input
     * @return
     */
    public static boolean checkContainsFloat(String input) {
        Pattern p = Pattern.compile("\\d+(\\.\\d+)?");
        if (input == null)
            return false;
        return p.matcher(input).matches();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    private static final int DEF_DIV_SCALE = 10; //这个类不能实例化

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 数字格式化对象
     */
    public static String round(double val) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(val);
    }

    // 最后一次点击时间
    private static long lastClickTime;

    /**
     * 按两次Back在退出程序
     *
     * @param context 句柄
     */
    public static void exitSys(Activity context) {
        if ((System.currentTimeMillis() - lastClickTime) > 2000) {
            ArmsUtils.makeText(context, ArmsUtils.getString(context, R.string.common_exit));
            lastClickTime = System.currentTimeMillis();
        } else {
            ArmsUtils.killAll();
//            context.finish();
//            /*当前是退出APP后结束进程。如果不这样做，那么在APP结束后需求手动将EventBus中所注册的监听全部清除以免APP在次启动后重复注册监听*/
//            Process.killProcess(Process.myPid());
//            返回到桌面
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            context.startActivity(intent);
        }
    }

    /**
     * 如果当前是否非空，则返回当前值
     *
     * @param cs
     * @return
     */
    public static String isEmptyReturnStr(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return "";
        }
        String val = cs.toString();
        return ((val != null && val.length() > 0 && (!val.equalsIgnoreCase("null"))) ? val : "");
    }

    /***
     * Sim卡序列号
     */
    public static String getSimSerialNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String ret = tm.getSimSerialNumber();
            if (ret != null)
                return ret;
            else
                return "";
        } catch (Exception ex) {
            return "";
        }
    }

    /***
     * 拿到电话号码(此接口不会100%的获取手机号码，原因是手机号码是映射到sim卡中的。要想100%获取手机号码只能通过靠对接运营商接口获得)
     */
    public static String getPhoneNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String ret = tm.getLine1Number();
            if (ret != null)
                return ret;
            else
                return "";
        } catch (Exception ex) {
            return "";
        }
    }
}
