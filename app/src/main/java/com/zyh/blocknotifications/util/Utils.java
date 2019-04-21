package com.zyh.blocknotifications.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * Time:2019/4/21
 * Author:ZYH
 * Description:
 */
public class Utils {

    /**
     * 打开通知权限设置
     */
    public static void openNotificationListenSettings(Context context) {

        try {
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 检查是否开启服务
     */
    public static boolean isEnabled(Context context)
    {
        String str = context.getPackageName();
        String localObject = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(localObject))
        {
            String[] strArr = (localObject).split(":");
            int i = 0;
            while (i < strArr.length)
            {
                ComponentName localComponentName = ComponentName.unflattenFromString(strArr[i]);
                if ((localComponentName != null) && (TextUtils.equals(str, localComponentName.getPackageName())))
                    return true;
                i += 1;
            }
        }
        return false;
    }
}
