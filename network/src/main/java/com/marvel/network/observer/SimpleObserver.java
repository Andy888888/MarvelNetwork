package com.marvel.network.observer;

import io.reactivex.disposables.Disposable;

/**
 * 描述:简单的观察者.
 * <p>
 *
 * @author yanwenqiang.
 * @date 2019/1/30
 */
public abstract class SimpleObserver<T> extends ResponseObserver<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }
}
