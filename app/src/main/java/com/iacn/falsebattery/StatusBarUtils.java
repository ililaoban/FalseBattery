package com.iacn.falsebattery;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * 状态栏工具类
 * <p/>
 * Modify from 2016.7.28 By Leon
 */
public class StatusBarUtils {
    /**
     * 设置状态栏颜色
     * <p/>
     * 需要在设置的 Activity 的根布局中加入 android:fitsSystemWindows="true" 属性
     * 需要在 Theme 设置 android:windowTranslucentStatus=true 属性
     * <p/>
     * 使用原生半透明效果(Android 5.0+黑色遮罩， Android 4.4渐变)
     */
    public static void setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 创建一个和状态栏一样大小的View
            View statusBarView = createStatusView(activity, color);

            // 添加statusBarView到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusBarView);
        }
    }

    /**
     * 设置状态栏颜色，自定义透明度
     * 仅适用于Android 5.0+，因为Android 4.4会带个黑色的渐变，自定义透明度之后更难看
     * <p/>
     * 需要在设置的 Activity 的根布局中加入 android:fitsSystemWindows="true" 属性
     * 需要在 Theme 设置 android:windowDrawsSystemBarBackgrounds=true 属性
     *
     * @param alpha 透明度（0 - 255）
     */
    public static void setColor(Activity activity, int color, int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, alpha));
        }
    }

    /**
     * 设置状态栏纯色 无半透明效果
     */
    public static void setPureColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 设置状态栏原生半透明效果
     * <p/>
     * Android 5.0+黑色遮罩， Android 4.4渐变
     * <p/>
     * 适用于图片作为背景的界面，此时可图片填充到状态栏
     */
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置状态栏彻底全透明效果
     * <p/>
     * 适用于图片作为背景的界面，此时可图片填充到状态栏
     */
    public static void setFullTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    /**
     * 设置灰色状态栏图标
     * 仅适用于 Android 6.0+
     * <p/>
     * 也可以在 Theme 内设置 android:windowLightStatusBar=true
     */
    public static void setDarkIcon(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 创建一个和状态栏一样大小的View
     */
    private static View createStatusView(Activity activity, int color) {
        View statusBarView = new View(activity);
        statusBarView.setBackgroundColor(color);
        statusBarView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity)));

        return statusBarView;
    }

    /**
     * 获取状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        int result = 0;
        // 获得状态栏高度在系统中的的Id
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    /**
     * 计算状态栏颜色
     *
     * @param alpha 透明度（0 - 255）
     */
    private static int calculateStatusColor(int color, int alpha) {
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;

        float a = 1 - alpha / 255f;

        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);

        return 0xff << 24 | red << 16 | green << 8 | blue;
    }
}