package edu.jhuapl.trinity.utils;

/*-
 * #%L
 * trinity-1.0.0-SNAPSHOT
 * %%
 * Copyright (C) 2021 - 2023 The Johns Hopkins University Applied Physics Laboratory LLC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javafx.scene.image.Image;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

public class HttpsUtils {

    public static Image getImage(String urlPath) {
        try {
            // Create a URL object for the image
            URL url = new URL(urlPath);

            // Open a connection to the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Get the input stream from the connection
            InputStream in = conn.getInputStream();

            // Create an image object from the input stream
            return new Image(in);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Image("");
    }

    public static Image getImageNoSSL(String urlPath) {
        try {
            URL url = new URL(urlPath);
            // Open a connection to the URL
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            // Create an SSL context that uses the trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());


            // Set the SSL socket factory on the HTTPS connection
            conn.setSSLSocketFactory(sslContext.getSocketFactory());

            conn.setRequestMethod("GET");

            // Get the input stream from the connection
            InputStream in = conn.getInputStream();

            // Create an image object from the input stream
            Image image = new Image(in);
            return image;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Image("");
    }
}
