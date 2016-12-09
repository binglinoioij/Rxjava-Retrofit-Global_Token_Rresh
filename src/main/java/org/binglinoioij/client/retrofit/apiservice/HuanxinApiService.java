package org.binglinoioij.client.retrofit.apiservice;

import org.binglinoioij.client.dto.ChatMessage;
import org.binglinoioij.client.dto.HuanxinFreshTokenDto;
import org.binglinoioij.client.dto.HuanxinRegisterDto;
import org.binglinoioij.client.dto.HuanxinTokenDto;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * <p>
 *
 * </p>
 *
 * <b>Creation Time:</b> 2016/11/30
 *
 * @author binglin
 */
public interface HuanxinApiService {
    @GET("users/{username}")
    Call<String> getUser(@Path("username") Integer uid);

    @GET("users/{username}")
    Observable<String> getUserRx(@Path("username") Integer uid);

    @GET("users/{username}")
    Observable<Map<String, Object>> getUserRxMap(@Path("username") Integer uid);

    @POST("users")
    Observable<Map<String, Object>> register(@Body HuanxinRegisterDto huanxinRegisterDto);

    @POST("token")
    Call<HuanxinTokenDto> freshToken(@Body HuanxinFreshTokenDto freshTokenDto);

    @POST("token")
    Observable<HuanxinTokenDto> freshTokenRx(@Body HuanxinFreshTokenDto freshTokenDto);

    @PUT("users/{username}/password")
    Observable<Map<String, Object>> resetPassword(@Path("username") Integer uid, @Body Map<String, Object> newPassword);

    @POST("messages")
    Observable<Map<String, Object>> sentMesssage(@Body ChatMessage chatMessage);

}
