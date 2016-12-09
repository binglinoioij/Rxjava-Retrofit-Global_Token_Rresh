package org.binglinoioij.client.retrofit;


import org.binglinoioij.client.TokenManager;
import org.binglinoioij.client.dto.HuanxinFreshTokenDto;
import org.binglinoioij.client.dto.HuanxinTokenDto;
import org.binglinoioij.client.enums.HttpStatus;
import org.binglinoioij.client.retrofit.apiservice.HuanxinApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * <p>
 *
 * </p>
 *
 * <b>Creation Time:</b> 2016/12/1
 *
 * @author binglin
 */
public class HuanxinApiServiceProvider implements InvocationHandler {

    private static final Retrofit retrofit = RetrofitProvider.getHuanxinRetrofit();

    private static String huanxinClientId = "huanxinClientId";

    private static String huanxinSecret = "huanxin.clientSecret";

    private static final Logger logger = LoggerFactory.getLogger(HuanxinApiServiceProvider.class);

    //for reflect
    private Object target;

    private static final int maxRetries = 2;
    private static final int retryDelayMillis = 30;
    private int retryCount;

    public Object getTarget() {
        return target;
    }

    private void setTarget(Object target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    private <T> T getService(Class<T> tClass) {
        T t = retrofit.create(tClass);
        this.setTarget(t);
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class<?>[]{tClass}, this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //这里采用动态代理是为了给每一个T t = retrofit.create(tClass); 这样生成处理的Observable的对象都添加retryWhen否则就需要每一个
        //对象自己添加
        Object result;
        result = Observable.just(null)
                .flatMap((Func1<Object, Observable<?>>) o -> {
                    try {
                        return (Observable<?>) method.invoke(target, args);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("can not invoke method, method={},target={},args={},ex={}", method, target, args, e);
                        return Observable.error(e);
                    }
                }).retryWhen(observable -> observable.flatMap(this::dealWithHttpException));
        return result;
    }


    private Observable<?> dealWithHttpException(Throwable error) {
        if (error instanceof HttpException) {
            HttpException ex = (HttpException) error;
            if (HttpStatus.UNAUTHORIZED.value() == ex.code()) {
                return updateToken(ex);
            }
            if (HttpStatus.TOO_MANY_REQUESTS.value() == ex.code()) {
                //环信发生429时，证明接口限流了，所以要sleep一会
                try {
                    TimeUnit.MILLISECONDS.sleep(retryDelayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return Observable.just(null);
            }
        }
        return Observable.error(error);
    }

    private Observable<?> updateToken(HttpException error) {
        refreshToken();
        if (++retryCount <= maxRetries) {
            return Observable.just(null);
        } else {
            return Observable.error(error);
        }
    }

    public static void refreshToken() {
        synchronized (HuanxinApiServiceProvider.class) {
            //刷新token
            logger.info("刷新token");
            RetrofitProvider.getHuanxinRetrofit().create(HuanxinApiService.class).freshTokenRx(new HuanxinFreshTokenDto(huanxinClientId, huanxinSecret)).subscribe(new Subscriber<HuanxinTokenDto>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    logger.error("刷新token失败！！！throwable={}", throwable);
                }

                @Override
                public void onNext(HuanxinTokenDto huanxinTokenDto) {
                    if (null == huanxinTokenDto) {
                        logger.error("刷新token失败！！！");
                    } else {
                        logger.info("刷新token成功：token={}", huanxinTokenDto.getAccess_token());

                        //拿到新的token以后放入缓存中，实际应用应该是redis等缓存
                        TokenManager.tokenCache.put(huanxinClientId, huanxinTokenDto.getAccess_token());
                    }
                }
            });
        }
    }

    public static class Builder {
        private HuanxinApiServiceProvider apiServiceProvider;

        private Builder() {
            apiServiceProvider = new HuanxinApiServiceProvider();
        }

        public static <T> T createService(Class<T> tClass) {
            return new Builder().apiServiceProvider.getService(tClass);
        }
    }
}
