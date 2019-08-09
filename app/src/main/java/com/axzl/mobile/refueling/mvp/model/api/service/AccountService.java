package com.axzl.mobile.refueling.mvp.model.api.service;

import com.axzl.mobile.refueling.mvp.model.entity.GasStationsResponse;
import com.jess.arms.cj.GsonRequest;
import com.jess.arms.cj.GsonResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 包名： PACKAGE_NAME
 * 对象名： AccountService
 * 描述：账户相关接口
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/24 10:03
 */

public interface AccountService {

    /*-----------------------------------------------------------------------用户基本-----------------------------------------------------------------------*/
    //登录
//    @POST("member/login")
//    Observable<GsonResponse<LoginResponse>> toLogin(@Body GsonRequest<Map<String, Object>> request);
//
//    //验证Token有效性
//    @POST("member/validToken")
//    Observable<GsonResponse<LoginResponse>> validToken(@Body GsonRequest<Map<String, Object>> request);
//
//    //修改密码
//    @POST("member/updatePwd")
//    Observable<GsonResponse<CommonResponse>> changePassword(@Body GsonRequest<Map<String, Object>> request);
//
//    //发送短信验证码
//    @POST("member/forgetPassword")
//    Observable<GsonResponse<CommonResponse>> sendSms(@Body GsonRequest<Map<String, Object>> request);
//
//    //重置密码
//    @POST("member/resetPwd")
//    Observable<GsonResponse<CommonResponse>> forgotPassword(@Body GsonRequest<Map<String, Object>> request);
//
//    //上传JPush注册ID并绑定到用户
//    @POST("member/uploadJpushRegId")
//    Observable<GsonResponse<CommonResponse>> uploadJPushRegId(@Body GsonRequest<Map<String, Object>> request);
//
//    //上传照片 member/upload
//    @Multipart                                //标记一个请求是multipart/form-data类型
//    @POST("fileUploadController/uploadImage")
//    Observable<GsonResponse<UploadPicResponse>> upload(@Part("type") RequestBody type, @Part("key") RequestBody key, @Part MultipartBody.Part file);
//
//    //注册上传多组文件
//    @Multipart                                //标记一个请求是multipart/form-data类型
//    @POST("fileUploadController/uploadFiles")
//    Observable<GsonResponse<RegisterUploadResponse>> uploadFilesWithParts(@Part("phone") RequestBody phone, @Part MultipartBody.Part businessLicense, @Part MultipartBody.Part dgBusinessLicense);

    /*-----------------------------------------------------------------------我要加油-----------------------------------------------------------------------*/

    // 搜索附近加油站
    @POST("/getSearchForStations")
    Observable<GsonResponse<GasStationsResponse>> getSearchForStations(@Body GsonRequest<Map<String, Object>> request);

    // 下载图片
    @GET()
    Observable<ResponseBody> download(@Url String url);
}
