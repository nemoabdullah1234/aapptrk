package com.nicbit.traquer.stryker.network;

import com.google.gson.JsonObject;
import com.nicbit.traquer.common.cloudinary.CloudinaryImage;
import com.nicbit.traquer.common.cloudinary.CloudinaryUpload;
import com.nicbit.traquer.common.cognito.ValidateCognitoToken;
import com.nicbit.traquer.common.geofence.GeofencDeviceListener;
import com.nicbit.traquer.common.geofence.GeofenceApiResponse;
import com.nicbit.traquer.common.linkdevice.LinkUnlinkDeviceListener;
import com.nicbit.traquer.common.newInventory.listener.DeviceRegisterListener;
import com.nicbit.traquer.common.newInventory.listener.FloorListListener;
import com.nicbit.traquer.common.newInventory.listener.ZoneListListener;
import com.nicbit.traquer.common.newInventory.location.LocationListListener;
import com.nicbit.traquer.common.newInventory.response.ApiBaseResponse;
import com.nicbit.traquer.common.newInventory.response.FloorApiResponse;
import com.nicbit.traquer.common.newInventory.response.ZoneApiResponse;
import com.nicbit.traquer.common.updatedevice.DeviceInfo;
import com.nicbit.traquer.common.updatedevice.DeviceInformationResponseListener;
import com.nicbit.traquer.stryker.BuildConfig;
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
import com.nicbit.traquer.stryker.Models.history.ReviewerDetail;
import com.nicbit.traquer.stryker.Models.inventory.UpdateInventoryScanRequest;
import com.nicbit.traquer.stryker.Models.newModels.CountryApiResponse;
import com.nicbit.traquer.stryker.Models.newModels.NotificationApiResponse;
import com.nicbit.traquer.stryker.Models.newModels.TempApiResponse;
import com.nicbit.traquer.stryker.exception.ErrorMessage;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.ApiListener;
import com.nicbit.traquer.stryker.listener.CaseDetailResponseListener;
import com.nicbit.traquer.stryker.listener.CaseHistoryDetailResponseListener;
import com.nicbit.traquer.stryker.listener.CaseHistoryResponseListener;
import com.nicbit.traquer.stryker.listener.CaseIssueResponseListener;
import com.nicbit.traquer.stryker.listener.CaseItemCommentsResponseListener;
import com.nicbit.traquer.stryker.listener.CaseItemQuantityListener;
import com.nicbit.traquer.stryker.listener.CasesResponseListener;
import com.nicbit.traquer.stryker.listener.CountriesListener;
import com.nicbit.traquer.stryker.listener.EditProfileResponseListener;
import com.nicbit.traquer.stryker.listener.ForgotPasswordResponseListener;
import com.nicbit.traquer.stryker.listener.HtmlContentListener;
import com.nicbit.traquer.stryker.listener.InventoryDetailListener;
import com.nicbit.traquer.stryker.listener.InventoryListListener;
import com.nicbit.traquer.stryker.listener.ItemDetailResponseListener;
import com.nicbit.traquer.stryker.listener.ItemResponseListener;
import com.nicbit.traquer.stryker.listener.ItemUsedStatusResponseListener;
import com.nicbit.traquer.stryker.listener.LoginResponseListener;
import com.nicbit.traquer.stryker.listener.LogoutResponseListener;
import com.nicbit.traquer.stryker.listener.NotificationListListener;
import com.nicbit.traquer.stryker.listener.PostCommentResponseListener;
import com.nicbit.traquer.stryker.listener.ReportIssueCommentsResponseListener;
import com.nicbit.traquer.stryker.listener.ReportIssueResponseListener;
import com.nicbit.traquer.stryker.listener.ReportShippingIssueResponseListener;
import com.nicbit.traquer.stryker.listener.ResetPasswordListener;
import com.nicbit.traquer.stryker.listener.SaveGcmListener;
import com.nicbit.traquer.stryker.listener.SearchItemResponseListener;
import com.nicbit.traquer.stryker.listener.SettingsResponseListener;
import com.nicbit.traquer.stryker.listener.SurgeryReportSubmitResponseListener;
import com.nicbit.traquer.stryker.listener.TempInfoListener;
import com.nicbit.traquer.stryker.listener.TrackingConfigListner;
import com.nicbit.traquer.stryker.listener.UpdateCaseFlagListener;
import com.nicbit.traquer.stryker.listener.UpdateSettingsResponseListener;
import com.nicbit.traquer.stryker.listener.UserProfileResponseListener;
import com.nicbit.traquer.stryker.listener.ValidateTokenListener;
import com.nicbit.traquer.stryker.util.PrefUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHandler {
    ApiListener apiListener = null;
    LoginResponseListener loginResponseListener;
    CasesResponseListener casesResponseListener;
    LogoutResponseListener logoutResponseListener;
    UserProfileResponseListener userProfileResponseListener;
    HtmlContentListener htmlContentListener;
    ForgotPasswordResponseListener forgotPasswordResponseListener;
    ValidateTokenListener validateTokenListener;
    ResetPasswordListener resetPasswordListener;
    UpdateCaseFlagListener updateCaseFlagListener;
    CaseHistoryResponseListener caseHistoryResponseListener;
    SettingsResponseListener settingsResponseListener;
    UpdateSettingsResponseListener updateSettingsResponseListener;
    ReportIssueResponseListener reportIssueResponseListener;
    CaseDetailResponseListener caseDetailResponseListener;
    PostCommentResponseListener postCommentResposeListener;
    ReportIssueCommentsResponseListener reportIssueCommentsResponseListener;
    CaseIssueResponseListener caseIssueResponseListener;
    NotificationListListener notificationListListener;
    SaveGcmListener saveGcmListener;
    EditProfileResponseListener editProfileResponseListener;
    CountriesListener countriesListener;
    ItemResponseListener itemResponseListener;
    SearchItemResponseListener searchItemResponseListener;
    ItemDetailResponseListener itemDetailResponseListener;
    CaseHistoryDetailResponseListener caseHistoryDetailResponseListener;
    ReportShippingIssueResponseListener reportShippingIssueResponseListener;
    ItemUsedStatusResponseListener itemUsedStatusResponseListener;
    CaseItemCommentsResponseListener caseItemCommentsResponseListener;
    TrackingConfigListner trackingConfigListner;
    InventoryListListener inventoryListListener;
    InventoryDetailListener inventoryDetailListener;
    SurgeryReportSubmitResponseListener surgeryReportSubmitResponseListener;
    CaseItemQuantityListener caseItemQuantityListener;

    DeviceInformationResponseListener deviceInformationResponseListener;
    DeviceRegisterListener deviceRegisterListener;
    FloorListListener floorListListener;
    ZoneListListener zoneListListener;
    LocationListListener locationListListener;
    LinkUnlinkDeviceListener linkUnlinkDeviceListener;
    TempInfoListener tempInfoListener;
    GeofencDeviceListener geofencDeviceListener;

    public void setTempInfoListener(TempInfoListener tempInfoListener) {
        this.tempInfoListener = tempInfoListener;
    }

    public void setGeofencDeviceListener(GeofencDeviceListener geofencDeviceListener) {
        this.geofencDeviceListener = geofencDeviceListener;


    }

    public void setLinkUnlinkDeviceListener(LinkUnlinkDeviceListener linkUnlinkDeviceListener) {
        this.linkUnlinkDeviceListener = linkUnlinkDeviceListener;
    }

    public void setLocationListListener(LocationListListener locationListListener) {
        this.locationListListener = locationListListener;
    }

    public void setZoneListListener(ZoneListListener zoneListListener) {
        this.zoneListListener = zoneListListener;
    }

    public void setCaseItemQuantityListener(CaseItemQuantityListener caseItemQuantityListener) {
        this.caseItemQuantityListener = caseItemQuantityListener;
    }

    public void setFloorListListener(FloorListListener floorListListener) {
        this.floorListListener = floorListListener;
    }

    public void setSurgeryReportSubmitResponseListener(SurgeryReportSubmitResponseListener surgeryReportSubmitResponseListener) {
        this.surgeryReportSubmitResponseListener = surgeryReportSubmitResponseListener;
    }

    public void setInventoryDetailListener(InventoryDetailListener inventoryDetailListener) {
        this.inventoryDetailListener = inventoryDetailListener;
    }

    public void setInventoryListListener(InventoryListListener inventoryListListener) {
        this.inventoryListListener = inventoryListListener;
    }

    public void setTrackingConfigListner(TrackingConfigListner trackingConfigListner) {
        this.trackingConfigListner = trackingConfigListner;
    }

    public void setCaseItemCommentsResponseListener(CaseItemCommentsResponseListener caseItemCommentsResponseListener) {
        this.caseItemCommentsResponseListener = caseItemCommentsResponseListener;
    }

    public void setReportShippingIssueResponseListener(ReportShippingIssueResponseListener reportShippingIssueResponseListener) {
        this.reportShippingIssueResponseListener = reportShippingIssueResponseListener;
    }


    public void setCountriesListener(CountriesListener countriesListener) {
        this.countriesListener = countriesListener;
    }

    public void setEditProfileResponseListener(EditProfileResponseListener editProfileResponseListener) {
        this.editProfileResponseListener = editProfileResponseListener;
    }

    public void setDeviceRegisterListener(DeviceRegisterListener deviceRegisterListener) {
        this.deviceRegisterListener = deviceRegisterListener;
    }


    public void setItemUsedStatusResponseListener(ItemUsedStatusResponseListener itemUsedStatusResponseListener) {
        this.itemUsedStatusResponseListener = itemUsedStatusResponseListener;
    }

    public void setCaseHistoryDetailResponseListener(CaseHistoryDetailResponseListener caseHistoryDetailResponseListener) {
        this.caseHistoryDetailResponseListener = caseHistoryDetailResponseListener;
    }

    public void setItemDetailResponseListener(ItemDetailResponseListener itemDetailResponseListener) {
        this.itemDetailResponseListener = itemDetailResponseListener;
    }

    public void setSearchItemResponseListener(SearchItemResponseListener searchItemResponseListener) {
        this.searchItemResponseListener = searchItemResponseListener;
    }

    public void setItemResponseListener(ItemResponseListener itemResponseListener) {
        this.itemResponseListener = itemResponseListener;
    }


    public void setSaveGcmListener(SaveGcmListener saveGcmListener) {
        this.saveGcmListener = saveGcmListener;
    }

    public void setNotificationListListener(NotificationListListener notificationListListener) {
        this.notificationListListener = notificationListListener;
    }

    public void setCaseIssueResponseListener(CaseIssueResponseListener caseIssueResponseListener) {
        this.caseIssueResponseListener = caseIssueResponseListener;
    }

    public void setPostCommentResposeListener(PostCommentResponseListener postCommentResposeListener) {
        this.postCommentResposeListener = postCommentResposeListener;
    }

    public void setReportIssueCommentsResponseListener(ReportIssueCommentsResponseListener reportIssueCommentsResponseListener) {
        this.reportIssueCommentsResponseListener = reportIssueCommentsResponseListener;
    }


    public void setCaseDetailResponseListener(CaseDetailResponseListener caseDetailResponseListener) {
        this.caseDetailResponseListener = caseDetailResponseListener;
    }

    public void setDeviceInformationResponseListener(DeviceInformationResponseListener deviceInformationResponseListener) {
        this.deviceInformationResponseListener = deviceInformationResponseListener;
    }


    public static ApiHandler getApiHandler() {
        return new ApiHandler();
    }

    public void setReportIssueResponsListener(ReportIssueResponseListener reportIssueResponseListener) {
        this.reportIssueResponseListener = reportIssueResponseListener;
    }


    public void setSettingsResponseListener(SettingsResponseListener settingsResponseListener) {
        this.settingsResponseListener = settingsResponseListener;
    }

    public void setUpdateSettingsResponseListener(UpdateSettingsResponseListener updateSettingsResponseListener) {
        this.updateSettingsResponseListener = updateSettingsResponseListener;
    }

    public void setCaseHistoryResponseListener(CaseHistoryResponseListener caseHistoryResponseListener) {
        this.caseHistoryResponseListener = caseHistoryResponseListener;
    }

    public void setUpdateCaseFlagListener(UpdateCaseFlagListener updateCaseFlagListener) {
        this.updateCaseFlagListener = updateCaseFlagListener;
    }

    public void setForgotPasswordResponseListener(ForgotPasswordResponseListener forgotPasswordResponseListener) {
        this.forgotPasswordResponseListener = forgotPasswordResponseListener;
    }

    public void setValidateTokenListener(ValidateTokenListener validateTokenListener) {
        this.validateTokenListener = validateTokenListener;
    }

    public void setResetPasswordListener(ResetPasswordListener resetPasswordListener) {
        this.resetPasswordListener = resetPasswordListener;
    }

    public void setHtmlContentListener(HtmlContentListener htmlContentListener) {
        this.htmlContentListener = htmlContentListener;
    }

    public void setLoginResponseListener(LoginResponseListener loginResponseListener) {
        this.loginResponseListener = loginResponseListener;
    }

    public void setCasesResponseListener(CasesResponseListener casesResponseListener) {
        this.casesResponseListener = casesResponseListener;
    }

    public void setApiListener(ApiListener listener) {
        this.apiListener = listener;
    }

    public void getLoginDataFromApi(LoginRequestModel loginRequestModel) {
        Call<ApiResponseModel> call = RetrofitRestClient.getInstance().doSignIn(loginRequestModel);
        call.enqueue(new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                if (response.isSuccessful()) {
                    loginResponseListener.onLoginResponse(response.body(), null);
                } else {
                    loginResponseListener.onLoginResponse(null, new NicbitException(ErrorMessage.GSON));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                if (loginResponseListener != null) {
                    loginResponseListener.onLoginResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                }
            }
        });

    }


    public void getSessionToken(String token) {
        Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getSessionToken(token);
        call.enqueue(new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                if (response.isSuccessful()) {
                    apiListener.onApiResponse(response.body(), null);
                } else {
                    apiListener.onApiResponse(null, new NicbitException(ErrorMessage.GSON));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                if (apiListener != null) {
                    apiListener.onApiResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                }
            }
        });
    }


    public void getCases() {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    String sortBy = PrefUtils.getSortBy();
                    if (!PrefUtils.getSortOrder().equals("asc"))
                        sortBy = "-" + PrefUtils.getSortBy();
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getCases(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), sortBy, PrefUtils.getSortOrder());
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                casesResponseListener.onCasesResponse(response.body(), null);
                            } else {
                                casesResponseListener.onCasesResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (casesResponseListener != null) {
                                casesResponseListener.onCasesResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    casesResponseListener.onCasesResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }


    public void setUserProfileResponseListener(UserProfileResponseListener userProfileResponseListener) {
        this.userProfileResponseListener = userProfileResponseListener;
    }

    public void setLogoutResponseListener(LogoutResponseListener logoutResponseListener) {
        this.logoutResponseListener = logoutResponseListener;
    }

    public void getUserProfileDataFromApi() {
        Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getUserProfile(PrefUtils.getSessionToken());
        call.enqueue(new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                if (response.isSuccessful()) {
                    userProfileResponseListener.onUserProfileResponse(response.body(), null);
                } else {
                    userProfileResponseListener.onUserProfileResponse(null, new NicbitException(ErrorMessage.GSON));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                if (userProfileResponseListener != null) {
                    userProfileResponseListener.onUserProfileResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                }
            }
        });
    }


    public void logout() {
        Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getLogout(PrefUtils.getSessionToken());
        call.enqueue(new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                if (response.isSuccessful()) {
                    logoutResponseListener.onLogoutResponse(response.body(), null);
                } else {
                    logoutResponseListener.onLogoutResponse(null, new NicbitException(ErrorMessage.GSON));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                if (logoutResponseListener != null) {
                    logoutResponseListener.onLogoutResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                }
            }
        });
    }

    public void linkDevice(final JsonObject code) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().linkDevice(BuildConfig.ENVIRONMENT, code);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                linkUnlinkDeviceListener.onDeviceLink(response.body(), null);
                            } else {
                                linkUnlinkDeviceListener.onDeviceLink(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (linkUnlinkDeviceListener != null) {
                                linkUnlinkDeviceListener.onDeviceLink(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    linkUnlinkDeviceListener.onDeviceLink(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void unLinkDevice(final JsonObject code) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().unLinkDevice(BuildConfig.ENVIRONMENT, code);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                linkUnlinkDeviceListener.onDeviceUnlink(response.body(), null);
                            } else {
                                linkUnlinkDeviceListener.onDeviceUnlink(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (linkUnlinkDeviceListener != null) {
                                linkUnlinkDeviceListener.onDeviceUnlink(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    linkUnlinkDeviceListener.onDeviceUnlink(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void getHtml(final String type) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getcontent(PrefUtils.getSessionToken(), type);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                htmlContentListener.onHtmlReceive(response.body(), null);
                            } else {
                                htmlContentListener.onHtmlReceive(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (htmlContentListener != null) {
                                htmlContentListener.onHtmlReceive(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    htmlContentListener.onHtmlReceive(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void getCaseDetails(final String token, final String caseNo) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getCaseDetails(BuildConfig.ENVIRONMENT, token, caseNo);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                caseDetailResponseListener.onCaseDetailResponse(response.body(), null);
                            } else {
                                caseDetailResponseListener.onCaseDetailResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (caseDetailResponseListener != null) {
                                caseDetailResponseListener.onCaseDetailResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    caseDetailResponseListener.onCaseDetailResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void updateDeviceInformation(final DeviceInfo deviceInfor) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().updateDevice(deviceInfor);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                deviceInformationResponseListener.onDeviceUpdate(response.body(), null);
                            } else {
                                deviceInformationResponseListener.onDeviceUpdate(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (deviceInformationResponseListener != null) {
                                deviceInformationResponseListener.onDeviceUpdate(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    deviceInformationResponseListener.onDeviceUpdate(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void registerDevice(final DeviceInfo deviceInfor) {
        Call<ApiBaseResponse> call = RetrofitRestClient.getInstance().registerDevice(BuildConfig.ENVIRONMENT, deviceInfor);
        call.enqueue(new Callback<ApiBaseResponse>() {
            @Override
            public void onResponse(Call<ApiBaseResponse> call, Response<ApiBaseResponse> response) {
                if (response.isSuccessful()) {
                    deviceRegisterListener.onDeviceUpdate(response.body(), null);
                } else {
                    deviceRegisterListener.onDeviceUpdate(null, new NicbitException(ErrorMessage.GSON));
                }
            }

            @Override
            public void onFailure(Call<ApiBaseResponse> call, Throwable t) {
                if (deviceRegisterListener != null) {
                    deviceRegisterListener.onDeviceUpdate(null, new NicbitException(ErrorMessage.CONNECTION));
                }
            }
        });
    }


    public void updateCaseFlag(final UpdateCaseModel updateCaseModel) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().updateCaseFlag(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), updateCaseModel);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                updateCaseFlagListener.onUpdateCaseFlagResponse(response.body(), null);
                            } else {
                                updateCaseFlagListener.onUpdateCaseFlagResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (updateCaseFlagListener != null) {
                                updateCaseFlagListener.onUpdateCaseFlagResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    updateCaseFlagListener.onUpdateCaseFlagResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }


    public void doForgotPasswordRequest(String email) {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail(email);
        getForgotPasswordDataFromApi(forgotPasswordRequest);

    }

    private void getForgotPasswordDataFromApi(final ForgotPasswordRequest forgotPasswordRequest) {
        Call<ApiResponseModel> call = RetrofitRestClient.getInstance().requestForgotPassword(forgotPasswordRequest);
        call.enqueue(new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                if (forgotPasswordResponseListener != null) {
                    forgotPasswordResponseListener.onForgotPasswordResponse(response.body(), null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                if (forgotPasswordResponseListener != null) {
                    forgotPasswordResponseListener.onForgotPasswordResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                }

            }
        });
    }

    public void doValidateTokenRequest(String email, String token) {
        ValidateForgotPasswordTokenRequest validateForgotPasswordTokenRequest = new ValidateForgotPasswordTokenRequest();
        validateForgotPasswordTokenRequest.setEmail(email);
        validateForgotPasswordTokenRequest.setToken(token);
        getValidateTokenDataFromApi(validateForgotPasswordTokenRequest);
    }

    private void getValidateTokenDataFromApi(final ValidateForgotPasswordTokenRequest validateForgotPasswordTokenRequest) {
        Call<ApiResponseModel> call = RetrofitRestClient.getInstance().validateForgotToken(validateForgotPasswordTokenRequest);
        call.enqueue(new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                if (validateTokenListener != null) {
                    validateTokenListener.onValidateTokenResponse(response.body(), null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                if (validateTokenListener != null) {
                    validateTokenListener.onValidateTokenResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                }


            }
        });
    }

    public void doResetPasswordRequest(String email, String token, String password) {
        ResetPasswordModel resetPasswordModel = new ResetPasswordModel();
        resetPasswordModel.setEmail(email);
        resetPasswordModel.setToken(token);
        resetPasswordModel.setPassword(password);
        getResetPasswordDataFromApi(resetPasswordModel);
    }

    private void getResetPasswordDataFromApi(final ResetPasswordModel resetPasswordModel) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().resetPassword(resetPasswordModel);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (resetPasswordListener != null) {
                                resetPasswordListener.onResetPasswordResponse(response.body(), null);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (resetPasswordListener != null) {
                                resetPasswordListener.onResetPasswordResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }


                        }
                    });
                } else {
                    resetPasswordListener.onResetPasswordResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }


    public void getCasesHistory(final String sortBy, final String sortOrder) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getCasesHistory(PrefUtils.getSessionToken(), sortBy, sortOrder);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                caseHistoryResponseListener.onCaseHistoryResponse(response.body(), null);
                            } else {
                                caseHistoryResponseListener.onCaseHistoryResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (caseHistoryResponseListener != null) {
                                caseHistoryResponseListener.onCaseHistoryResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    caseHistoryResponseListener.onCaseHistoryResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void searchCases(final String query) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().searchCases(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), query);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                apiListener.onApiResponse(response.body(), null);
                            } else {
                                apiListener.onApiResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (apiListener != null) {
                                apiListener.onApiResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    apiListener.onApiResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void getSettings() {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getSettings(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken());
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                settingsResponseListener.onSettingsResponseReceive(response.body(), null);
                            } else {
                                settingsResponseListener.onSettingsResponseReceive(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (settingsResponseListener != null) {
                                settingsResponseListener.onSettingsResponseReceive(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    settingsResponseListener.onSettingsResponseReceive(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void updateSettings(final UpdateSettingsRequest updateSettingsRequest) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().updateSettings(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), updateSettingsRequest);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (updateSettingsResponseListener != null) {
                                if (response.isSuccessful()) {
                                    updateSettingsResponseListener.onUpdateSettingsResponseReceive(response.body(), null);
                                } else {
                                    updateSettingsResponseListener.onUpdateSettingsResponseReceive(null, new NicbitException(ErrorMessage.GSON));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (updateSettingsResponseListener != null) {
                                updateSettingsResponseListener.onUpdateSettingsResponseReceive(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    updateSettingsResponseListener.onUpdateSettingsResponseReceive(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }


    public void postIssueComment(final PostIssueComment postIssueComment) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().postIssueComment(PrefUtils.getSessionToken(), postIssueComment);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                postCommentResposeListener.onPostIssueCommentResponse(response.body(), null);
                            } else {
                                postCommentResposeListener.onPostIssueCommentResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (postCommentResposeListener != null) {
                                postCommentResposeListener.onPostIssueCommentResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    postCommentResposeListener.onPostIssueCommentResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }


    public void getIssueComments(final String caseNo, final String shippingNo, final String issueId) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getIssueComments(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), caseNo, shippingNo, issueId);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                reportIssueCommentsResponseListener.onIssueCommentsResponse(response.body(), null);
                            } else {
                                reportIssueCommentsResponseListener.onIssueCommentsResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (reportIssueCommentsResponseListener != null) {
                                reportIssueCommentsResponseListener.onIssueCommentsResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    reportIssueCommentsResponseListener.onIssueCommentsResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }


    public void getCaseIssue(final String token, final String caseNo) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getCaseIssue(token, caseNo);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                caseIssueResponseListener.onCaseIssueResponse(response.body(), null);
                            } else {
                                caseIssueResponseListener.onCaseIssueResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (caseIssueResponseListener != null) {
                                caseIssueResponseListener.onCaseIssueResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    caseIssueResponseListener.onCaseIssueResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void getNotifications() {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<NotificationApiResponse> call = RetrofitRestClient.getInstance().getNotifications(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), 1);
                    call.enqueue(new Callback<NotificationApiResponse>() {
                        @Override
                        public void onResponse(Call<NotificationApiResponse> call, Response<NotificationApiResponse> response) {
                            if (response.isSuccessful()) {
                                notificationListListener.onNotificationListReceive(response.body(), null);
                            } else {
                                notificationListListener.onNotificationListReceive(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationApiResponse> call, Throwable t) {
                            if (notificationListListener != null) {
                                notificationListListener.onNotificationListReceive(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    notificationListListener.onNotificationListReceive(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void doConsumerGcm(String gcmId) {
        SaveConsumerGcm model = new SaveConsumerGcm();
        model.setChannelId(gcmId);
        model.setOs("android");
        saveConsumerGcm(model);
    }

    public void saveConsumerGcm(final SaveConsumerGcm model) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().consumerGcm(PrefUtils.getSessionToken(), model);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {

                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }

    public void doTimeZone(String timeZone) {
        EditProfileRequest model = new EditProfileRequest();
        model.setTimezone(timeZone);
        setTimeZone(model);
    }

    public void setTimeZone(final EditProfileRequest timeZone) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().editProfile(PrefUtils.getSessionToken(), timeZone);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                apiListener.onApiResponse(response.body(), null);
                            } else {
                                apiListener.onApiResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (apiListener != null) {
                                apiListener.onApiResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    apiListener.onApiResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void doEditRequest(Integer isImageRemove, String city, String password, String firstName, String lastName, String mobileNo, String countryCode, File file) {


        final CognitoEditProfileRequest editProfileRequest = new CognitoEditProfileRequest();
        editProfileRequest.setMobileNumber(mobileNo);
        editProfileRequest.setGiven_name(firstName);
        editProfileRequest.setFamily_name(lastName);
        editProfileRequest.setCountryCode(countryCode);
        editProfileRequest.setDeleteProfileImage(isImageRemove);
        editProfileRequest.setEmail(PrefUtils.getEmail());
        editProfileRequest.setMobileCode(countryCode);
        if (file != null) {
            List<String> imagePaths = new ArrayList<>();
            imagePaths.add(file.getAbsolutePath());
            new CloudinaryUpload(null, imagePaths, new CloudinaryUpload.CloudanaryCallBack() {
                @Override
                public void onUploadImages(List<CloudinaryImage> imageList, String error) {
                    if (imageList != null && imageList.size() > 0) {
                        editProfileRequest.setPicture(imageList.get(0).getUrl());
                        PrefUtils.setUserImageUrl(imageList.get(0).getUrl());
                        getUpdateDataFromApi(editProfileRequest);

                    } else {
                        editProfileRequest.setPicture(PrefUtils.getUserImageUrl());
                        getUpdateDataFromApi(editProfileRequest);
                    }

                }
            }).uploadImage();
        } else {
            if (isImageRemove == 1) {
                PrefUtils.setUserImageUrl("");
                editProfileRequest.setPicture("");
            } else
                editProfileRequest.setPicture(PrefUtils.getUserImageUrl());

            getUpdateDataFromApi(editProfileRequest);
        }

     /*   EditProfileRequest editProfileRequest = new EditProfileRequest();
        editProfileRequest.setcity(city);
        editProfileRequest.setPassword(password);
        editProfileRequest.setMobileNo(mobileNo);
        editProfileRequest.setFirstName(firstName);
        editProfileRequest.setLastName(lastName);
        editProfileRequest.setCountryCode(countryCode);
        editProfileRequest.setDeleteProfileImage(isImageRemove);
        getUpdateDataFromApi(editProfileRequest, file);*/
    }

    public void getUpdateDataFromApi(/*EditProfileRequest editProfileRequest*/ final CognitoEditProfileRequest editProfileRequest) {


        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().updateProfileCognito(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), editProfileRequest);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                editProfileResponseListener.onEditProfileResponse(response.body(), null);
                            } else {
                                ApiResponseModel apiResponseModel = new ApiResponseModel();
                                apiResponseModel.setCode(response.code());
                                editProfileResponseListener.onEditProfileResponse(apiResponseModel, null);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (editProfileResponseListener != null) {
                                editProfileResponseListener.onEditProfileResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });

                } else {
                    editProfileResponseListener.onEditProfileResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
        /* ApiService service =
                MultipartRetrofitRestClient.getInstance();
        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("profileImage", file.getName(), requestFile);
        }

        RequestBody city =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), editProfileRequest.getcity());

        RequestBody password =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), editProfileRequest.getPassword());

        RequestBody firstName =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), editProfileRequest.getFirstName());

        RequestBody lastName =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), editProfileRequest.getLastName());

        RequestBody mobileNo =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), editProfileRequest.getMobileNo());

        RequestBody countryCode =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), editProfileRequest.getCountryCode());
        RequestBody deleteProfileImage =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), editProfileRequest.getDeleteProfileImage() + "");


        // finally, execute the request
        Call<ApiResponseModel> call = service.updateProfile(PrefUtils.getSessionToken(), city, password, firstName, lastName, mobileNo, countryCode, deleteProfileImage, body);
        call.enqueue(new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                if (response.isSuccessful()) {
                    editProfileResponseListener.onEditProfileResponse(response.body(), null);
                } else {
                    editProfileResponseListener.onEditProfileResponse(null, new NicbitException(ErrorMessage.GSON));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                if (editProfileResponseListener != null) {
                    editProfileResponseListener.onEditProfileResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                }
            }
        });*/
    }


    public void reportShippingIssue(final ReportIssueRequest reportIssueRequest) {
      /*  ApiService service = MultipartRetrofitRestClient.getInstance();
        ArrayList<MultipartBody.Part> list = new ArrayList<>();
        for (SelectedImage selectedImage : images) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), selectedImage.getFile());
            MultipartBody.Part body = MultipartBody.Part.createFormData("images[]", selectedImage.getFile().getName(), requestFile);
            list.add(body);
        }

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("caseNo", createPartFromString(reportIssueRequest.getCaseNo()));
        map.put("shippingNo", createPartFromString(reportIssueRequest.getShippingNo()));
        map.put("comment", createPartFromString(reportIssueRequest.getComment()));
        map.put("skuIds", createPartFromString(reportIssueRequest.getSkuIds()));*/
        // finally, execute the request
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().reportShippingIssue(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), reportIssueRequest);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                reportShippingIssueResponseListener.onReportShippingIssueResponse(response.body(), null);
                            } else {
                                reportShippingIssueResponseListener.onReportShippingIssueResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (reportShippingIssueResponseListener != null) {
                                reportShippingIssueResponseListener.onReportShippingIssueResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    reportShippingIssueResponseListener.onReportShippingIssueResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    private RequestBody createPartFromString(String descriptionString) {
        String MULTIPART_FORM_DATA = "multipart/form-data";
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }


    public void getCountries() {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<CountryApiResponse> call = RetrofitRestClient.getInstance().getCountries(BuildConfig.ENVIRONMENT);
                    call.enqueue(new Callback<CountryApiResponse>() {
                        @Override
                        public void onResponse(Call<CountryApiResponse> call, Response<CountryApiResponse> response) {
                            if (response.isSuccessful()) {
                                countriesListener.onCountriesResponse(response.body(), null);
                            } else {
                                countriesListener.onCountriesResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<CountryApiResponse> call, Throwable t) {
                            if (countriesListener != null) {
                                countriesListener.onCountriesResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    countriesListener.onCountriesResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });

    }


    public void getItems(final String query) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getItems(PrefUtils.getSessionToken(), query);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                itemResponseListener.onItemResponse(response.body(), null);
                            } else {
                                itemResponseListener.onItemResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (itemResponseListener != null) {
                                itemResponseListener.onItemResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    itemResponseListener.onItemResponse(null, new NicbitException(ErrorMessage.CONNECTION));

                }
            }
        });
    }


    public void getSearchItems(final String itemId) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getSearchItems(PrefUtils.getSessionToken(), itemId);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                searchItemResponseListener.onSearchItemResponse(response.body(), null);
                            } else {
                                searchItemResponseListener.onSearchItemResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (searchItemResponseListener != null) {
                                searchItemResponseListener.onSearchItemResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    searchItemResponseListener.onSearchItemResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }


    public void getItemDetail(final String caseNo, final String skuId) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getItemDetails(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), caseNo, skuId);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                itemDetailResponseListener.onItemDetailResponse(response.body(), null);
                            } else {
                                itemDetailResponseListener.onItemDetailResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (itemDetailResponseListener != null) {
                                itemDetailResponseListener.onItemDetailResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    itemDetailResponseListener.onItemDetailResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void getCaseHistoryDetails(final String caseNo) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getCaseHistoryDetails(PrefUtils.getSessionToken(), caseNo);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                caseHistoryDetailResponseListener.onCaseHistoryDetailResponse(response.body(), null);
                            } else {
                                caseHistoryDetailResponseListener.onCaseHistoryDetailResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (caseHistoryDetailResponseListener != null) {
                                caseHistoryDetailResponseListener.onCaseHistoryDetailResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    caseHistoryDetailResponseListener.onCaseHistoryDetailResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void setItemUsedStatus(final ItemUsedRequest request) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().setItemUsedStatus(PrefUtils.getSessionToken(), request);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                itemUsedStatusResponseListener.onItemUsedStatusResponse(response.body(), null);
                            } else {
                                itemUsedStatusResponseListener.onItemUsedStatusResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (itemUsedStatusResponseListener != null) {
                                itemUsedStatusResponseListener.onItemUsedStatusResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    itemUsedStatusResponseListener.onItemUsedStatusResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }

            }
        });
    }

    public void getItemComments(final String caseNo, final String skuId) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getCaseItemComments(PrefUtils.getSessionToken(), caseNo, skuId);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                caseItemCommentsResponseListener.onItemCommentsResponse(response.body(), null);
                            } else {
                                caseItemCommentsResponseListener.onItemCommentsResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (caseItemCommentsResponseListener != null) {
                                caseItemCommentsResponseListener.onItemCommentsResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    caseItemCommentsResponseListener.onItemCommentsResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void getCompletedCases() {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getCompletedCases(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken());
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                caseHistoryResponseListener.onCaseHistoryResponse(response.body(), null);
                            } else {
                                caseHistoryResponseListener.onCaseHistoryResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (caseHistoryResponseListener != null) {
                                caseHistoryResponseListener.onCaseHistoryResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    caseHistoryResponseListener.onCaseHistoryResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void getTrackingConfig(final String app) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getTrackingConfigurations(PrefUtils.getSessionToken(), app);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful() && trackingConfigListner != null) {
                                trackingConfigListner.onTrackingConfig(response.body(), null);
                            } else {
                                trackingConfigListner.onTrackingConfig(null, new NicbitException(ErrorMessage.GSON));
                            }


                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (trackingConfigListner != null) {
                                trackingConfigListner.onTrackingConfig(null, new NicbitException(ErrorMessage.CONNECTION));
                            }


                        }
                    });
                } else {
                    trackingConfigListner.onTrackingConfig(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });

    }

    public void reportItemComment(final ReportIssueRequest reportIssueRequest) {
      /*  ApiService service = MultipartRetrofitRestClient.getInstance();
        ArrayList<MultipartBody.Part> list = new ArrayList<>();
        for (SelectedImage selectedImage : images) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), selectedImage.getFile());
            MultipartBody.Part body = MultipartBody.Part.createFormData("images[]", selectedImage.getFile().getName(), requestFile);
            list.add(body);
        }

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("caseNo", createPartFromString(reportIssueRequest.getCaseNo()));
        map.put("skuId", createPartFromString(reportIssueRequest.getSkuId()));
        map.put("comment", createPartFromString(reportIssueRequest.getComment()));
*/

        // finally, execute the request
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().reportItemComments(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), reportIssueRequest);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                reportShippingIssueResponseListener.onReportShippingIssueResponse(response.body(), null);
                            } else {
                                reportShippingIssueResponseListener.onReportShippingIssueResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (reportShippingIssueResponseListener != null) {
                                reportShippingIssueResponseListener.onReportShippingIssueResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    reportShippingIssueResponseListener.onReportShippingIssueResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void SearchNearLocation(final double lat, final double lang, final Integer locationId) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().searchNearLocations(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), lat, lang, locationId);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful() && inventoryListListener != null) {
                                inventoryListListener.onSearchLocationResponse(response.body(), null);
                            } else {
                                inventoryListListener.onSearchLocationResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (inventoryListListener != null) {
                                inventoryListListener.onSearchLocationResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }

                        }
                    });
                } else {
                    inventoryListListener.onSearchLocationResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void getFLoorList(final String locationId) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<FloorApiResponse> call = RetrofitRestClient.getInstance().getFloors(BuildConfig.ENVIRONMENT, locationId);
                    call.enqueue(new Callback<FloorApiResponse>() {
                        @Override
                        public void onResponse(Call<FloorApiResponse> call, Response<FloorApiResponse> response) {
                            if (response.isSuccessful()) {
                                floorListListener.onFloorListResponse(response.body(), null);
                            } else {
                                floorListListener.onFloorListResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<FloorApiResponse> call, Throwable t) {
                            if (floorListListener != null) {
                                floorListListener.onFloorListResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }

                        }
                    });
                } else {
                    floorListListener.onFloorListResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void getZones(final String floorID) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ZoneApiResponse> call = RetrofitRestClient.getInstance().getZones(BuildConfig.ENVIRONMENT, floorID);
                    call.enqueue(new Callback<ZoneApiResponse>() {
                        @Override
                        public void onResponse(Call<ZoneApiResponse> call, Response<ZoneApiResponse> response) {
                            if (response.isSuccessful()) {
                                zoneListListener.onZoneListResponse(response.body(), null);
                            } else {
                                zoneListListener.onZoneListResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ZoneApiResponse> call, Throwable t) {
                            if (zoneListListener != null) {
                                zoneListListener.onZoneListResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }

                        }
                    });
                } else {
                    zoneListListener.onZoneListResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }


    public void getLocation(final double lat, final double lang, final String locationId) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getLocation(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), lat, lang, locationId);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                locationListListener.onLocationListResponse(response.body(), null);
                            } else {
                                locationListListener.onLocationListResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (locationListListener != null) {
                                locationListListener.onLocationListResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }

                        }
                    });
                } else {
                    locationListListener.onLocationListResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }


    public void getInventoryDetails(final String skuId) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getItemInventory(BuildConfig.ENVIRONMENT, skuId, PrefUtils.getSessionToken());
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful() && inventoryDetailListener != null) {
                                inventoryDetailListener.onInventoryDetailResponse(response.body(), null);
                            } else {
                                inventoryDetailListener.onInventoryDetailResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (inventoryDetailListener != null) {
                                inventoryDetailListener.onInventoryDetailResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }

                        }
                    });
                } else {
                    inventoryDetailListener.onInventoryDetailResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void updateItemDetails(final UpdateInventoryRequestModel updateInventoryRequestModel) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().updateInventory(PrefUtils.getSessionToken(), updateInventoryRequestModel);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful() && inventoryDetailListener != null) {
                                inventoryDetailListener.onInventoryUpdate(response.body(), null);
                            } else {
                                inventoryDetailListener.onInventoryUpdate(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (inventoryDetailListener != null) {
                                inventoryDetailListener.onInventoryUpdate(null, new NicbitException(ErrorMessage.CONNECTION));
                            }

                        }
                    });
                } else {
                    inventoryDetailListener.onInventoryUpdate(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void submitSurgeryReport(final ReviewerDetail reviewerDetail) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    ApiService service = MultipartRetrofitRestClient.getInstance();
                    MultipartBody.Part body = null;
                    if (reviewerDetail.getSignatureFile() != null) {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), reviewerDetail.getSignatureFile());
                        body = MultipartBody.Part.createFormData("surgerysignature", reviewerDetail.getSignatureFile().getName(), requestFile);
                    }

                    HashMap<String, RequestBody> map = new HashMap<>();
                    map.put("caseNo", createPartFromString(reviewerDetail.getCaseNo()));
                    map.put("reviewerFirstName", createPartFromString(reviewerDetail.getReviewerFirstName()));
                    map.put("reviewerLastName", createPartFromString(reviewerDetail.getReviewerLastName()));
                    map.put("reviewerMobile", createPartFromString(reviewerDetail.getReviewerMobile()));
                    map.put("reviewerCountryCode", createPartFromString(reviewerDetail.getReviewerCountryCode()));
                    // finally, execute the request
                    Call<ApiResponseModel> call = service.submitSurgeryReport(PrefUtils.getSessionToken(), map, body);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                surgeryReportSubmitResponseListener.onSurgeryReportSubmitResponse(response.body(), null);
                            } else {
                                surgeryReportSubmitResponseListener.onSurgeryReportSubmitResponse(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (surgeryReportSubmitResponseListener != null) {
                                surgeryReportSubmitResponseListener.onSurgeryReportSubmitResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    surgeryReportSubmitResponseListener.onSurgeryReportSubmitResponse(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });


    }

    public void getCaseItemQuantity(final Integer skuId, final String caseNo) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getCaseItemQuantity(BuildConfig.ENVIRONMENT, caseNo, PrefUtils.getSessionToken(), skuId);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful() && caseItemQuantityListener != null) {
                                caseItemQuantityListener.onCaseItemQuantityReceive(response.body(), null);
                            } else {
                                caseItemQuantityListener.onCaseItemQuantityReceive(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (caseItemQuantityListener != null) {
                                caseItemQuantityListener.onCaseItemQuantityReceive(null, new NicbitException(ErrorMessage.CONNECTION));
                            }

                        }
                    });
                } else {
                    caseItemQuantityListener.onCaseItemQuantityReceive(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void updateCaseItemQuantity(final UpdateInventoryRequestModel updateInventoryRequestModel) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().updateCaseItemQuantity(PrefUtils.getSessionToken(), updateInventoryRequestModel);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful() && caseItemQuantityListener != null) {
                                caseItemQuantityListener.onCaseItemQuantityUpdated(response.body(), null);
                            } else {
                                caseItemQuantityListener.onCaseItemQuantityUpdated(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (inventoryDetailListener != null) {
                                inventoryDetailListener.onInventoryUpdate(null, new NicbitException(ErrorMessage.CONNECTION));
                            }

                        }
                    });
                } else {
                    inventoryDetailListener.onInventoryUpdate(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void updateInventoryScanData(final UpdateInventoryScanRequest updateInventoryRequest) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().updateInventoryScanData(PrefUtils.getSessionToken(), updateInventoryRequest);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                            if (response.isSuccessful()) {
                                inventoryListListener.onInventoryScanDataUpdated(response.body(), null);
                            } else {
                                inventoryListListener.onInventoryScanDataUpdated(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                            if (inventoryListListener != null) {
                                inventoryListListener.onInventoryScanDataUpdated(null, new NicbitException(ErrorMessage.CONNECTION));
                            }

                        }
                    });
                } else {
                    inventoryListListener.onInventoryScanDataUpdated(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void removeNotification(final RemoveNotificationRequest removeNotificationRequest) {
        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<NotificationApiResponse> call = RetrofitRestClient.getInstance().removeNotifications(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), removeNotificationRequest);
                    call.enqueue(new Callback<NotificationApiResponse>() {
                        @Override
                        public void onResponse(Call<NotificationApiResponse> call, Response<NotificationApiResponse> response) {
                            if (response.isSuccessful()) {
                                notificationListListener.onNotificationRemove(response.body(), null);
                            } else {
                                notificationListListener.onNotificationRemove(null, new NicbitException(ErrorMessage.GSON));
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationApiResponse> call, Throwable t) {
                            if (notificationListListener != null) {
                                notificationListListener.onNotificationRemove(null, new NicbitException(ErrorMessage.CONNECTION));
                            }
                        }
                    });
                } else {
                    notificationListListener.onNotificationRemove(null, new NicbitException(ErrorMessage.SYNC_TOKEN_ERROR));

                }
            }
        });
    }

    public void updatePhoneStatus(final PhoneStatusRequest phoneStatusRequest) {

        new ValidateCognitoToken(new ValidateCognitoToken.UserTokenListener() {
            @Override
            public void onValidateToken(boolean isValidate) {
                if (isValidate) {
                    Call<ApiResponseModel> call = RetrofitRestClient.getInstance().phoneStatus(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), phoneStatusRequest);
                    call.enqueue(new Callback<ApiResponseModel>() {
                        @Override
                        public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {


                        }

                        @Override
                        public void onFailure(Call<ApiResponseModel> call, Throwable t) {


                        }
                    });

                }

            }
        });

    }

    public void getInventoryDetailsByUid(String uid) {
        Call<ApiResponseModel> call = RetrofitRestClient.getInstance().getItemInventoryByUid(BuildConfig.ENVIRONMENT, PrefUtils.getSessionToken(), uid, "nfcTag");
        call.enqueue(new Callback<ApiResponseModel>() {
            @Override
            public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                if (response.isSuccessful() && inventoryDetailListener != null) {
                    inventoryDetailListener.onInventoryDetailResponse(response.body(), null);
                } else {
                    inventoryDetailListener.onInventoryDetailResponse(null, new NicbitException(ErrorMessage.GSON));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                if (inventoryDetailListener != null) {
                    inventoryDetailListener.onInventoryDetailResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                }

            }
        });
    }

    public void getTempInfo(String skuId) {
        Call<TempApiResponse> call = RetrofitRestClient.getInstance().getTempInfo(BuildConfig.ENVIRONMENT, skuId, PrefUtils.getSessionToken());
        call.enqueue(new Callback<TempApiResponse>() {
            @Override
            public void onResponse(Call<TempApiResponse> call, Response<TempApiResponse> response) {
                if (response.isSuccessful() && tempInfoListener != null) {
                    tempInfoListener.onTempInfoResponse(response.body(), null);
                } else {
                    tempInfoListener.onTempInfoResponse(null, new NicbitException(ErrorMessage.GSON));
                }
            }

            @Override
            public void onFailure(Call<TempApiResponse> call, Throwable t) {
                if (tempInfoListener != null) {
                    tempInfoListener.onTempInfoResponse(null, new NicbitException(ErrorMessage.CONNECTION));
                }

            }
        });
    }

    public void getGeofence() {
        Call<GeofenceApiResponse> call = RetrofitRestClient.getInstance().getGeofences(BuildConfig.ENVIRONMENT);
        call.enqueue(new Callback<GeofenceApiResponse>() {
            @Override
            public void onResponse(Call<GeofenceApiResponse> call, Response<GeofenceApiResponse> response) {
                if (response.isSuccessful()) {
                    geofencDeviceListener.onGeofenceRecived(response.body(), null);
                } else {
                    geofencDeviceListener.onGeofenceRecived(null, new NicbitException(ErrorMessage.GSON));
                }
            }

            @Override
            public void onFailure(Call<GeofenceApiResponse> call, Throwable t) {
                if (geofencDeviceListener != null) {
                    geofencDeviceListener.onGeofenceRecived(null, new NicbitException(ErrorMessage.CONNECTION));
                }
            }
        });


    }

}

