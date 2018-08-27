package com.nicbit.traquer.stryker.Models;


public class ReaderSignInResponse {
    private String token;
    String sessionToken;
    ReaderGetProfileResponse readerGetProfileResponse;
    ReaderGetSettingsResponse readerGetSettingsResponse;


    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public ReaderGetProfileResponse getReaderGetProfileResponse() {
        return readerGetProfileResponse;
    }

    public void setReaderGetProfileResponse(ReaderGetProfileResponse readerGetProfileResponse) {
        this.readerGetProfileResponse = readerGetProfileResponse;
    }

    public ReaderGetSettingsResponse getReaderGetSettingsResponse() {
        return readerGetSettingsResponse;
    }

    public void setReaderGetSettingsResponse(ReaderGetSettingsResponse readerGetSettingsResponse) {
        this.readerGetSettingsResponse = readerGetSettingsResponse;
    }

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }

}
