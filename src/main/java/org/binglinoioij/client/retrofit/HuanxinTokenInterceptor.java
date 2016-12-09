package org.binglinoioij.client.retrofit;


import org.binglinoioij.client.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>
 * 这是一个okhttp的拦截器，为了统一在请求的header中添加token
 * </p>
 *
 * <b>Creation Time:</b> 2016/12/1
 *
 * @author binglin
 */
public class HuanxinTokenInterceptor implements Interceptor {

    //实际应该写到配置文件中，然后中配置文件中读取
    private String huanxinClientAppId = "huanxinClientAppId";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String path = originalRequest.url().url().getPath();
        if (path.endsWith("token")) {
            return chain.proceed(originalRequest);
        }
        Request request = chain.request().newBuilder().header("Authorization", "Bearer " + TokenManager.tokenCache.get(huanxinClientAppId)).build();
        return chain.proceed(request);
    }
}
