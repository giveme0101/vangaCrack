package com.crake.june.vangacrake;

import android.telephony.TelephonyManager;

import com.crake.june.vangacrake.utils.ClassUtil;
import com.crake.june.vangacrake.utils.MLogger;
import com.crake.june.vangacrake.utils.Util;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedHookClass implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        MLogger.log("APP: " + lpparam.packageName);
        hookActive(lpparam);
        hookVanGaTV(lpparam);
    }

    private void hookVanGaTV(XC_LoadPackage.LoadPackageParam lpparam){
        if ("com.wjtv".equals(lpparam.packageName)) {
            MLogger.log("HOOK APP VanGaTV");

            final Class<?> logUtil = XposedHelpers.findClass("android.util.Log", lpparam.classLoader);
            XposedHelpers.findAndHookMethod(logUtil, "d", String.class, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    MLogger.log(String.format("Log.d: %s %s", param.args[0], param.args[1]));
                }
            });

            final Class<?> mLogClazz = XposedHelpers.findClass("com.linklib.utils.MLog", lpparam.classLoader);
            // ClassUtil.logMethod(mLogClazz);
            XposedHelpers.findAndHookMethod(mLogClazz, "d", String.class, String.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    MLogger.log("∞∞∞∞ start replace method: d() ∞∞∞∞");
                    MLogger.log(String.format("MLog.d： %s, %s", param.args[0], param.args[1]));
                    MLogger.log("∞∞∞∞ end replace method: d() ∞∞∞∞");
                    return null;
                }
            });

            XposedHelpers.findAndHookMethod(mLogClazz, "e", String.class, String.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    MLogger.log("∞∞∞∞ start replace method: e() ∞∞∞∞");
                    MLogger.log(String.format("MLog.e： %s, %s", param.args[0], param.args[1]));
                    MLogger.log("∞∞∞∞ end replace method: e() ∞∞∞∞");
                    return null;
                }
            });

            // 随机LAN MAC
            final Class<?> utilsClazz = XposedHelpers.findClass("com.linklib.utils.Utils", lpparam.classLoader);
            XposedHelpers.findAndHookMethod(utilsClazz, "getLanMac",  new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    MLogger.log("∞∞∞∞ start replace method: getLanMac() ∞∞∞∞");
                    String rtnValue = Util.ranWifiMac();
                    MLogger.log("getLanMac()返回值： " + rtnValue);
                    MLogger.log("∞∞∞∞ end replace method: getLanMac() ∞∞∞∞");
                    return rtnValue;
                }
            });

            // 随机WIFI MAC
            XposedHelpers.findAndHookMethod(utilsClazz, "getWifiMac", android.content.Context.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    MLogger.log("∞∞∞∞ start replace method: getWifiMac() ∞∞∞∞");
                    String rtnValue = VanGaTV.getOldValue();
                    MLogger.log("修改getWifiMac()返回值： " + rtnValue);
                    MLogger.log("∞∞∞∞ end replace method: getWifiMac() ∞∞∞∞");
                    return rtnValue;
                }
            });

            XposedHelpers.findAndHookMethod(TelephonyManager.class, "getDeviceId", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    String imei = getIMEI();
                    MLogger.log("∞∞∞∞ start replace method: getDeviceId() ∞∞∞∞");
                    MLogger.log("Random IMEI: " + imei);
                    MLogger.log("∞∞∞∞ end replace method: getDeviceId() ∞∞∞∞");
                    return imei;
                }
            });

            XposedHelpers.findAndHookMethod(TelephonyManager.class, "getSubscriberId", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    String imsi = getImsi();
                    MLogger.log("∞∞∞∞ start replace method: getSubscriberId() ∞∞∞∞");
                    MLogger.log("Random IMSI: " + imsi);
                    MLogger.log("∞∞∞∞ end replace method: getSubscriberId() ∞∞∞∞");
                    return imsi;
                }
            });

            // 去升级检测
            XposedHelpers.findAndHookMethod("com.linklib.a.a", lpparam.classLoader, "downloadUpdate", android.content.Context.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    MLogger.log("∞∞∞∞ start replace method: downloadUpdate() ∞∞∞∞");
                    MLogger.log("跳过本次升级检测！");
                    MLogger.log("∞∞∞∞ end replace method: downloadUpdate() ∞∞∞∞");
                    return null;
                }
            });

            final Class<?> acter = XposedHelpers.findClass("com.exact.ExActCls", lpparam.classLoader);
            ClassUtil.printAllFileds(acter);
        }
    }

    private static String getImsi() {
        String title = "4600";
        int second = 0;
        do {
            second = new java.util.Random().nextInt(8);
        } while (second == 4);
        int r1 = 10000 + new java.util.Random().nextInt(90000);
        int r2 = 10000 + new java.util.Random().nextInt(90000);
        return title + "" + second + "" + r1 + "" + r2;
    }

        private static String getIMEI() {
        int r1 = 1000000 + new java.util.Random().nextInt(9000000);
        int r2 = 1000000 + new java.util.Random().nextInt(9000000);
        String input = r1 + "" + r2;
        char[] ch = input.toCharArray();
        int a = 0, b = 0;
        for (int i = 0; i < ch.length; i++) {
            int tt = Integer.parseInt(ch[i] + "");
            if (i % 2 == 0) {
                a = a + tt;
            } else {
                int temp = tt * 2;
                b = b + temp / 10 + temp % 10;
            }
        }
        int last = (a + b) % 10;
        if (last == 0) {
            last = 0;
        } else {
            last = 10 - last;
        }
        return input + last;
    }

        private void hookActive (XC_LoadPackage.LoadPackageParam lpparam){
        if ("com.crake.june.vangacrake".equals(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod("com.crake.june.vangacrake.MainActivity", lpparam.classLoader, "checkHook", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return null;
                }
            });
        }
    }
}