package com.axzl.mobile.refueling.app.utils;

import android.text.TextUtils;
import android.util.Log;

import com.axzl.mobile.refueling.app.global.Constant;
import com.blankj.utilcode.util.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import timber.log.Timber;

/**
 * 包名： com.zqw.mobile.recycling.utils
 * 对象名： FileLoggingTree
 * 描述：储存地址
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/9/6 14:53
 */

public class FileLoggingTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {

        if (TextUtils.isEmpty(Constant.LOG_PATH)) {
            return;
        }
        FileUtils.createOrExistsDir(Constant.LOG_PATH);
        File file = new File(Constant.LOG_PATH + "log.txt");
        Log.v("dyp", "file.path:" + file.getAbsolutePath() + ",message:" + message);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(message);
            bufferedWriter.flush();

        } catch (IOException e) {
            Log.v("dyp", "存储文件失败");
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}