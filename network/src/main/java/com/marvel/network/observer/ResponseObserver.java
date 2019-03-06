package com.marvel.network.observer;


import com.marvel.network.exception.ResponseException;

import io.reactivex.Observer;
import retrofit2.HttpException;

/**
 * 描述:数据返回统一处理观察者.
 * <p>
 *
 * @author yanwenqiang.
 * @date 2019/1/29
 */
public abstract class ResponseObserver<T> implements Observer<T> {

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            int errorCode = ((HttpException) e).code();
            onFail(new ResponseException(errorCode, getMsgByCode(errorCode)).setDiagnostic(
                    e.getMessage()));
        } else if (e instanceof ResponseException) {
            onFail((ResponseException) e);
        } else {
            onFail(new ResponseException(-1, "请求失败").setDiagnostic("uncaught exception"));
        }
        onComplete();
    }

    /**
     * 根据状态码获取消息
     *
     * @param code 状态码
     * @return 消息
     */
    protected String getMsgByCode(int code) {
        String msg = "";
        switch (code) {
            case 404:
                msg = "未找到指定地址";
                break;
            case 500:
            case 502:
                msg = "服务器内部出错";
                break;
            case 504:
                msg = "网络异常";
                break;
            default:
                msg = "请求失败";
                break;
        }
        return msg;
    }

    /**
     * 数据成功返回
     *
     * @param result 数据
     */
    protected abstract void onSuccess(T result);

    /**
     * 响应失败
     *
     * @param responseException 响应异常
     */
    protected abstract void onFail(ResponseException responseException);
}
