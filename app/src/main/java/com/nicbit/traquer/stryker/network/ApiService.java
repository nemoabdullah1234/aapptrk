package com.nicbit.traquer.stryker.network;


import com.google.gson.JsonObject;
import com.nicbit.traquer.common.geofence.GeofenceApiResponse;
import com.nicbit.traquer.common.newInventory.response.ApiBaseResponse;
import com.nicbit.traquer.common.newInventory.response.FloorApiResponse;
import com.nicbit.traquer.common.newInventory.response.ZoneApiResponse;
import com.nicbit.traquer.common.updatedevice.DeviceInfo;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.CognitoEditProfileRequest;
import com.nicbit.traquer.stryker.Models.EditProfileRequest;
import com.nicbit.traquer.stryker.Models.ItemUsedRequest;
import com.nicbit.traquer.stryker.Models.LoginRequestModel;
import com.nicbit.traquer.stryker.Models.PhoneStatusRequest;
import com.nicbit.traquer.stryker.Models.PostIssueComment;
import com.nicbit.traquer.stryker.Models.RemoveNotificationRequest;
import com.nicbit.traquer.stryker.Models.ReportIssueRequest;
import com.nicbit.traquer.stryker.Models.SaveConsumerGcm;
import com.nicbit.traquer.stryker.Models.UpdateCaseModel;
import com.nicbit.traquer.stryker.Models.UpdateInventoryRequestModel;
import com.nicbit.traquer.stryker.Models.UpdateSettingsRequest;
import com.nicbit.traquer.stryker.Models.forgotPassword.ForgotPasswordRequest;
import com.nicbit.traquer.stryker.Models.forgotPassword.ResetPasswordModel;
import com.nicbit.traquer.stryker.Models.forgotPassword.ValidateForgotPasswordTokenRequest;
import com.nicbit.traquer.stryker.Models.inventory.UpdateInventoryScanRequest;
import com.nicbit.traquer.stryker.Models.newModels.CountryApiResponse;
import com.nicbit.traquer.stryker.Models.newModels.NotificationApiResponse;
import com.nicbit.traquer.stryker.Models.newModels.TempApiResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("signIn")
    Call<ApiResponseModel> doSignIn(
            @Body LoginRequestModel loginRequestModel
    );

    @POST("signOut")
    Call<ApiResponseModel> getLogout(
            @Header("sid") String token
    );

    @POST("generateSession")
    Call<ApiResponseModel> getSessionToken(
            @Header("sid") String token
    );

    @GET("getContentPage")
    Call<ApiResponseModel> getcontent(
            @Header("sid") String token,
            @Query("ctype") String page
    );


    @GET("getProfile")
    Call<ApiResponseModel> getUserProfile(
            @Header("sid") String token
    );

    @GET("/orders/{environment}/salesrep")
    Call<ApiResponseModel> getCases(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("sort") String sortBy,
            @Query("sortOrder") String sortOrder

    );

    @GET("/orders/{environment}/salesrep/{caseNo}")
    Call<ApiResponseModel> getCaseDetails(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Path("caseNo") String caseNo

    );

    @POST("saveDeviceInformation")
    Call<ApiResponseModel> updateDevice(@Body DeviceInfo deviceInfor);

    @POST("things/{environment}/devices")
    Call<ApiBaseResponse> registerDevice(@Path("environment") String environment, @Body DeviceInfo deviceInfor);


    @POST("requestForgotPassword")
    Call<ApiResponseModel> requestForgotPassword(
            @Body ForgotPasswordRequest forgotPasswordModel
    );


    @POST("validateForgotToken")
    Call<ApiResponseModel> validateForgotToken(
            @Body ValidateForgotPasswordTokenRequest validateForgotPasswordTokenRequest
    );

    @POST("resetPassword")
    Call<ApiResponseModel> resetPassword(
            @Body ResetPasswordModel resetPasswordModel
    );

    @PUT("orders/{environment}/salesrep/favourite")
    Call<ApiResponseModel> updateCaseFlag(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body UpdateCaseModel updateCaseModel
    );

    @PUT("things/{environment}/device/link")
    Call<ApiResponseModel> linkDevice(
            @Path("environment") String environment,
            @Body JsonObject appCode
    );

    @PUT("things/{environment}/device/unlink")
    Call<ApiResponseModel> unLinkDevice(
            @Path("environment") String environment,
            @Body JsonObject appCode
    );


    @GET("getCasesHistory")
    Call<ApiResponseModel> getCasesHistory(
            @Header("sid") String token,
            @Query("sortBy") String sortBy, @Query("sortOrder") String sortOrder
    );


    @GET("orders/{environment}/searchOrderAndProducts")
    Call<ApiResponseModel> searchCases(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("query") String query
    );

    @GET("settings/{environment}")
    Call<ApiResponseModel> getSettings(
            @Path("environment") String environment,
            @Header("sid") String token
    );

    @PUT("settings/{environment}")
    Call<ApiResponseModel> updateSettings(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body UpdateSettingsRequest updateSettingsRequest
    );


    @GET("issues/{environment}")
    Call<ApiResponseModel> getIssueComments(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("caseNo") String caseNo,
            @Query("shippingNo") String shippingNo,
            @Query("issueId") String issueId
    );

    @POST("postIssueComment")
    Call<ApiResponseModel> postIssueComment(
            @Header("sid") String token,
            @Body PostIssueComment postIssueComment
    );

    @GET("getCaseIssues")
    Call<ApiResponseModel> getCaseIssue(
            @Header("sid") String token,
            @Query("caseNo") String caseNo
    );

    @GET("notifications/{environment}")
    Call<NotificationApiResponse> getNotifications(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("mobile") int mobile
    );


    @POST("saveConsumerGcm")
    Call<ApiResponseModel> consumerGcm(
            @Header("sid") String token,
            @Body SaveConsumerGcm gcmId
    );

    @POST("updateProfile")
    Call<ApiResponseModel> editProfile(
            @Header("sid") String token,
            @Body EditProfileRequest timeZone
    );

    @GET("common/{environment}/countries")
    Call<CountryApiResponse> getCountries(
            @Path("environment") String environment
    );

    @GET("getItems")
    Call<ApiResponseModel> getItems(
            @Header("sid") String token,
            @Query("query") String query
    );

    @GET("searchItems")
    Call<ApiResponseModel> getSearchItems(
            @Header("sid") String token,
            @Query("itemId") String itemId
    );

    @GET("orders/{environment}/salesrep/products")
    Call<ApiResponseModel> getItemDetails(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("caseNo") String caseNo,
            @Query("skuId") String skuId

    );

    @GET("getCaseHistoryDetails")
    Call<ApiResponseModel> getCaseHistoryDetails(
            @Header("sid") String token,
            @Query("caseNo") String caseNo
    );

    @Multipart
    @POST("updateProfile")
    Call<ApiResponseModel> updateProfile(
            @Header("sid") String token,
            @Part("city") RequestBody city,
            @Part("password") RequestBody password,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("mobile") RequestBody mobileNo,
            @Part("countryCode") RequestBody countryCode,
            @Part("deleteProfileImage") RequestBody deleteProfileImage,
            @Part MultipartBody.Part file);

    @POST("setItemUsedStatus")
    Call<ApiResponseModel> setItemUsedStatus(
            @Header("sid") String token,
            @Body ItemUsedRequest request
    );


    @POST("issues/{environment}")
    Call<ApiResponseModel> reportShippingIssue(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body ReportIssueRequest reportIssueRequest
    );

    @PUT("users/{environment}/profile")
    Call<ApiResponseModel> updateProfileCognito(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body CognitoEditProfileRequest reportIssueRequest
    );

    @POST("issues/{environment}")
    Call<ApiResponseModel> reportItemComments(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body ReportIssueRequest reportIssueRequest

    );

    @GET("getCaseItemComments")
    Call<ApiResponseModel> getCaseItemComments(
            @Header("sid") String token,
            @Query("caseNo") String caseNo,
            @Query("skuId") String skuId
    );

    @GET("getTrackingConfigurations")
    Call<ApiResponseModel> getTrackingConfigurations(
            @Header("sid") String token,
            @Query("app") String appName

    );

    @GET("orders/{environment}/salesrep/completed")
    Call<ApiResponseModel> getCompletedCases(
            @Path("environment") String environment,
            @Header("sid") String token

    );

    @PUT("things/{environment}/deviceStatus")
    Call<ApiResponseModel> phoneStatus(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body PhoneStatusRequest phoneStatusRequest
    );

    ///
    @GET("locations/{environment}/inventory")
    Call<ApiResponseModel> searchNearLocations(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("locationId") int locationId
    );

    @GET("locations/{environment}/{locationId}/floors/inventory")
    Call<FloorApiResponse> getFloors(
            @Path("environment") String environment,
            @Path("locationId") String locationId

    );

    @GET("locations/{environment}/floors/{floorID}/products")
    Call<ZoneApiResponse> getZones(@Path("environment") String environment,
                                   @Path("floorID") String floorID
    );

    @GET("locations/{environment}/inventory")
    Call<ApiResponseModel> getLocation(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("locationId") String locationId


    );

    @GET("products/{environment}/inventory/{skuId}")
    Call<ApiResponseModel> getItemInventory(
            @Path("environment") String environment,
            @Path("skuId") String skuId,
            @Header("sid") String token

    );

    @GET("products/{environment}/inventory/thing/{uid}")
    Call<ApiResponseModel> getItemInventoryByUid(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Path("uid") String uid,
            @Query("type") String type

    );

    @GET("things/{environment}/temptags/scanhistory/{skuId}")
    Call<TempApiResponse> getTempInfo(
            @Path("environment") String environment,
            @Path("skuId") String skuId,
            @Header("sid") String token

    );

    @POST("updateItemInventory")
    Call<ApiResponseModel> updateInventory(
            @Header("sid") String token,
            @Body UpdateInventoryRequestModel updateInventoryRequestModel

    );

    @GET("locations/{environment}/list/")
    Call<GeofenceApiResponse> getGeofences(
            @Path("environment") String environment
    );

    @Multipart
    @POST("submitSurgeryReport")
    Call<ApiResponseModel> submitSurgeryReport(
            @Header("sid") String token,
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file
    );


    @GET("products/{environment}/inventory/{caseNo}")
    Call<ApiResponseModel> getCaseItemQuantity(
            @Path("environment") String environment,
            @Path("caseNo") String caseNo,
            @Header("sid") String token,
            @Query("skuId") int skuId

    );

    @POST("updateCaseItemQuantity")
    Call<ApiResponseModel> updateCaseItemQuantity(
            @Header("sid") String token,
            @Body UpdateInventoryRequestModel updateInventoryRequestModel

    );

    @POST("updateInventoryScanData")
    Call<ApiResponseModel> updateInventoryScanData(
            @Header("sid") String token,
            @Body UpdateInventoryScanRequest updateInventoryRequest

    );

    @PUT("notifications/{environment}/archive")
    Call<NotificationApiResponse> removeNotifications(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body RemoveNotificationRequest removeNotificationRequest
    );
}