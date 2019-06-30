package com.crake.june.vangacrake;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crake.june.vangacrake.utils.MLogger;
import com.crake.june.vangacrake.utils.Util;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkHook();
    }

    public void reInit(View view){
        try {
            VanGaTV.reInit();
            Toast.makeText(MainActivity.this, "操作完成！Mac: " + VanGaTV.getOldValue(), Toast.LENGTH_LONG).show();
        } catch (Exception ex){
            MLogger.log(ex.toString());
            Toast.makeText(MainActivity.this, "操作失败！" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void checkHook() {
        new AlertDialog.Builder(this)
                .setCancelable(false) // 屏蔽系統返回鍵
                .setMessage("模块未激活，请先激活模块并重启手机!")
                .setPositiveButton("立即激活", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!Util.openXposed(MainActivity.this)){
                            Toast.makeText(MainActivity.this, "Xposed Installer 未安装!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exit0();
                    }
                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            exit();
        }
        return true;
    }

    private void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(this, "再点击一次退出应用程序", Toast.LENGTH_LONG).show();
        } else {
            exit0();
        }
    }

    private void exit0(){
        this.finish();
        System.exit(0);
    }
}
