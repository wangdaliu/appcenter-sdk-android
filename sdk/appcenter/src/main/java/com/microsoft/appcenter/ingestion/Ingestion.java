package com.microsoft.appcenter.ingestion;

import android.support.annotation.NonNull;

import com.microsoft.appcenter.http.ServiceCall;
import com.microsoft.appcenter.http.ServiceCallback;
import com.microsoft.appcenter.ingestion.models.LogContainer;

import java.io.Closeable;
import java.util.UUID;

/**
 * Interface to send logs to the Ingestion service.
 */
public interface Ingestion extends Closeable {

    /**
     * Send logs to the Ingestion service.
     *
     * @param appSecret       a unique and secret key used to identify the application.
     * @param installId       install identifier.
     * @param logContainer    payload.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @return the {@link ServiceCall} object
     * @throws IllegalArgumentException thrown if callback is null
     */
    ServiceCall sendAsync(String appSecret, UUID installId, LogContainer logContainer, ServiceCallback serviceCallback) throws IllegalArgumentException;

    /**
     * Update log URL.
     *
     * @param logUrl log URL.
     */
    void setLogUrl(String logUrl);

    /**
     * Make ingestion active again after closing.
     */
    void reopen();

    /**
     * Sets the value of authorization token.
     *
     * @param authToken value of authorization token.
     */
    void setAuthToken(@NonNull String authToken);

    /**
     * Get the value of authorization token.
     *
     * @return value of authorization token.
     */
    String getAuthToken();
}
