package com.crake.june.vangacrake.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.crake.june.vangacrake.BuildConfig;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Util {

    public static String ranWifiMac() {
        Random random = new Random();
        String[] mac = {
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff))
        };
        return String.join(":", mac);
    }

    public static Boolean writeToSDCard(String dir, String fileName, String content, Boolean append){

        String rootPath = getSDCardPath();
        FileOutputStream fos = null;
        try{
            File dirFile = new File(rootPath + File.separator + dir + File.separator);
            if (!dirFile.exists()){
                dirFile.mkdirs();
            }

            File file = new File(rootPath + File.separator + dir + File.separator + fileName);
            if (!file.exists()){
                file.createNewFile();
            }

            fos = new FileOutputStream(file, append);
            fos.write(content.getBytes());
            fos.flush();

            return Boolean.TRUE;
        } catch (IOException ioe){
            ioe.printStackTrace();
            return Boolean.FALSE;
        } finally{
            try {
                if (null != fos){
                    fos.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static File getSDCardFile(String path){
        try {
            String rootPath = getSDCardPath();
            File file = new File(rootPath.concat(File.separator).concat(path));
            if (!file.exists()) {
                return null;
            }
            return file;
        } catch (Exception ex){
            return null;
        }
    }

    public static String readSDCardFile(String path){
        try {
            File file = getSDCardFile(path);
            if (null == file){
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuffer buf = new StringBuffer();
            String cache = null;
            while (null != (cache = br.readLine())){
                buf.append(cache);
            }
            return buf.toString();
        } catch (Exception ex){
            MLogger.log(ex.toString());
            return null;
        }
    }

    public static void delSDCardFile(String dir){
        if (StringUtils.isBlank(dir)){
            return;
        }
        try {
            String res = new ExeCommand().run(String.format("rm -rf %s/%s", getSDCardPath(), dir), 10000).getResult();
            MLogger.log("del " + getSDCardPath() + "/" + dir + " :" + res);
        } catch (Exception ex){
            MLogger.log(ex.toString());
        }
    }

    public static String getSDCardPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     *  打开XPosed的模块列表
     *
     * @param activity
     * @return
     */
    public static boolean openXposed(final Activity activity) {
        if (isInstalledXPosed(activity) ) {
            Intent intent = new Intent("de.robv.android.xposed.installer.OPEN_SECTION");
            if (activity.getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
                intent = activity.getPackageManager().getLaunchIntentForPackage("de.robv.android.xposed.installer");
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("section", "modules")
                    .putExtra("fragment", 1)
                    .putExtra("module", BuildConfig.APPLICATION_ID);
            activity.startActivity(intent);

            return true;
        }
        return  false;
    }

    /**
     *  XPosed是否安装
     *
     * @param context
     * @return
     */
    public static boolean isInstalledXPosed(Context context){

        return isAppInstalled(context, "de.robv.android.xposed.installer");
    }

    /**
     *  软件是否安装在本机上
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
