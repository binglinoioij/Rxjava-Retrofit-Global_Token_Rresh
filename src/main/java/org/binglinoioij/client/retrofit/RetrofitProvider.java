package org.binglinoioij.client.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * <p>
 * 生成Retrofit 类
 * </p>
 *
 * <b>Creation Time:</b> 2016/11/30
 *
 * @author binglin
 */
public class RetrofitProvider {

    private volatile static Retrofit retrofit;

    private static final String HUANXIN_BASE_URL = "https://a1.easemob.com/test/app/";

    public static Retrofit getHuanxinRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitProvider.class) {
                if (retrofit == null) {
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new HuanxinTokenInterceptor())
                            .addInterceptor(httpLoggingInterceptor);
                    retrofit = new Retrofit.Builder().baseUrl(HUANXIN_BASE_URL).client(httpClient.build())
                            .addConverterFactory(FastJsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
