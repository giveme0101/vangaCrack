package com.crake.june.vangacrake.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class MLogger {

    public static void log(String args){
        try {
            writeXBridgeLog(DateFormatUtils.format(System.currentTimeMillis(), "HH:mm:ss") + "    " + args + "\r\n");
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static Boolean writeXBridgeLog(String content){
        return Util.writeToSDCard("VirtualXposed/log", DateFormatUtils.format(new Date(), "YYYY-MM-dd").concat(".log"), content, Boolean.TRUE);
    }
}
