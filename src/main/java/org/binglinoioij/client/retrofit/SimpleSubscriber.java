package org.binglinoioij.client.retrofit;

import rx.Subscriber;

/**
 * <p>
 * 获取返回值
 * </p>
 *
 * <b>Creation Time:</b> 2016/11/30
 *
 * @author binglin
 */
public class SimpleSubscriber<T> extends Subscriber<T> {
    private T response;

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onNext(T t) {
        response = t;
    }

    public T getResponse() {
        return response;
    }
}