package com.crake.june.vangacrake;

import com.crake.june.vangacrake.utils.MLogger;
import com.crake.june.vangacrake.utils.Util;

import java.io.File;

public class VanGaTV {

    // 万佳历史记录
    private static final String[] vanGaHistory = new String[]{ ".a.dat", ".eeegesggeggwj", ".cc", ".um", ".uxx", "Android/data/.um", "Android/obj/.um"};

    private static final String ranMacPath = "VirtualXposed/VanGaCrack";
    private static final String ranMacFile = "VanGaCrack.log";
    private static final String ranMacFullPath = ranMacPath.concat(File.separator).concat(ranMacFile);

    public static void reInit (){
        for (String path : vanGaHistory){
            Util.delSDCardFile(path);
        }
        String mac = Util.ranWifiMac();
        Util.writeToSDCard(ranMacPath, ranMacFile, mac, Boolean.FALSE);
        MLogger.log("rebuild MAC: " + mac);
    }

    public static String getOldValue (){
        return Util.readSDCardFile(ranMacFullPath);
    }
}
