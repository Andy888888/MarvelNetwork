package com.marvel.network.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * 描述:打印工具.
 * <p>
 *
 * @author yanwenqiang.
 * @date 2019/1/21
 */
public class WLog {
    private static final String TAG = "marvel";
    private static PrettyFormatStrategy.Builder mBuilder;
    private static boolean mEnable = false;

    static {
        init();
    }

    private static final void init() {
        mBuilder = PrettyFormatStrategy.newBuilder()
                // 是否显示线程信息。 默认值为true
                .showThreadInfo(true)
                // 要显示的方法行数。 默认2
                .methodCount(2)
                // 隐藏内部方法调用到偏移量。 默认0
                .methodOffset(0)
                // 每个日志的全局标记。 默认PRETTY_LOGGER
                .tag(TAG);

        // 根据上面的格式设置logger相应的适配器
        Logger.addLogAdapter(new AndroidLogAdapter(mBuilder.build()));
        // 保存到文件
        // Logger.addLogAdapter(DiskLogAdapter());
    }

    private static Strategy mStrategy;

    public static void strategy(Strategy strategy) {
        if (mStrategy == null) {
            mStrategy = strategy;
        }
        mBuilder = PrettyFormatStrategy.newBuilder()
                // 是否显示线程信息。 默认值为true
                .showThreadInfo(strategy.showThreadInfo)
                // 要显示的方法行数。 默认2
                .methodCount(strategy.methodCount)
                // 隐藏内部方法调用到偏移量。 默认5
                .methodOffset(strategy.methodOffset)
                // 每个日志的全局标记。 默认PRETTY_LOGGER
                .tag(strategy.tag);

        // 根据上面的格式设置logger相应的适配器
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter(mBuilder.build()));
    }

    /**
     * set debug
     */
    public static void setEnable(boolean debug) {
        mEnable = debug;
    }

    /**
     * isDebug
     */
    public static boolean isEnable() {
        return mEnable;
    }

    protected static boolean checkLogAble() {
        return mEnable;
    }


    /**
     * 普通debug日志
     *
     * @param obj 打印内容
     */
    public static void p(Object obj) {
        if (checkLogAble() && obj != null) {
            Logger.d(obj.toString());
        }
    }

    /**
     * 普通debug日志
     *
     * @param tag 标记
     * @param obj 打印内容
     */
    public static void p(String tag, Object obj) {
        if (checkLogAble() && obj != null) {
            Logger.t(tag).d(obj.toString());
        }
    }

    /**
     * 普通debug日志
     *
     * @param tag 标记
     * @param obj 打印内容
     */
    public static void tempP(String tag, Object obj) {
        if (checkLogAble() && obj != null) {
            simple();
            Logger.t(tag).d(obj.toString());
            reset();
        }
    }

    /**
     * 格式化打印json串
     */
    public static void json(Object json) {
        if (checkLogAble() && json != null) {
            Logger.json(json.toString());
        }
    }

    /**
     * 格式化打印json串
     *
     * @param tag  标记
     * @param json 打印json内容
     */
    public static void json(String tag, Object json) {
        if (checkLogAble() && json != null) {
            Logger.t(tag).json(json.toString());
        }
    }

    /**
     * 错误日志
     *
     * @param obj 打印内容
     */
    public static void e(Object obj) {
        if (checkLogAble() && obj != null) {
            Logger.e(obj.toString());
        }
    }

    /**
     * 错误日志
     *
     * @param tag 标记
     * @param obj 打印内容
     */
    public static void e(String tag, Object obj) {
        if (checkLogAble() && obj != null) {
            Logger.t(tag).e(tag, obj.toString());
        }
    }

    /**
     * 错误日志
     *
     * @param tag 标记
     * @param obj 打印内容
     */
    public static void tempE(String tag, Object obj) {
        if (checkLogAble() && obj != null) {
            simple();
            Logger.t(tag).e(tag, obj.toString());
            reset();
        }
    }

    /**
     * 原生打印
     */
    public static void nativeLog(Object obj) {
        if (checkLogAble() && obj != null) {
            Log.d(TAG, obj.toString());
        }
    }

    /**
     * 原生打印
     */
    public static void nativeLog(String tag, Object obj) {
        if (checkLogAble() && obj != null) {
            Log.d(tag, obj.toString());
        }
    }

    private static void simple() {
        WLog.strategy(new Strategy().tag("temp").methodCount(0).showThreadInfo(false));
    }

    public static void reset() {
        WLog.strategy(mStrategy);
    }


    /**
     * 策略
     */
    public static class Strategy {
        int methodCount = 2;
        int methodOffset = 1;
        boolean showThreadInfo = true;
        @Nullable
        String tag = "marvel";

        public Strategy() {
        }

        @NonNull
        public Strategy methodCount(int val) {
            methodCount = val;
            return this;
        }

        @NonNull
        public Strategy methodOffset(int val) {
            methodOffset = val;
            return this;
        }

        @NonNull
        public Strategy showThreadInfo(boolean val) {
            showThreadInfo = val;
            return this;
        }

        @NonNull
        public Strategy tag(@Nullable String tag) {
            this.tag = tag;
            return this;
        }
    }
}

