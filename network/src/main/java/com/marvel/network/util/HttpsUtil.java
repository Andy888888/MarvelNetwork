package com.marvel.network.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 描述:Https SSL信任及相关工具.
 * <p>
 *
 * @author yanwenqiang.
 * @date 2019/1/23
 */
public class HttpsUtil {
    private final static String DEFAULT_PASS = "123456";
    private File mBksFile;
    private File[] mCerFile;
    private String mPass;
    private InputStream mBks;
    private InputStream[] mCers;
    private X509TrustManager x509;

    public HttpsUtil(File... files) {
        this(null, files);
    }

    public HttpsUtil(File bksFile, File... cerFile) {
        this(DEFAULT_PASS, bksFile, cerFile);
    }

    public HttpsUtil(String pass, File bksFile, File... cerFile) {
        mBksFile = bksFile;
        mCerFile = cerFile;
        mPass = pass;
        initBksAndCer();
    }

    private void initBksAndCer() {
        try {
            mPass = mPass == null ? DEFAULT_PASS : mPass;
            if (mBksFile != null) {
                mBks = new FileInputStream(mBksFile);
            }
            ArrayList<InputStream> cers = new ArrayList<>();
            for (File file : mCerFile) {
                cers.add(new FileInputStream(file));
            }
            if (!cers.isEmpty()) {
                mCers = cers.toArray(new InputStream[]{});
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取SSLSocketFactory
    public SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext tls = SSLContext.getInstance("TLS");
            tls.init(mBks == null ? null : getKeyManager(), getTrustManager(), new SecureRandom());
            return tls.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyManager[] getKeyManager() {
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(mBks, mPass.toCharArray());
            KeyManagerFactory keyManager = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm());
            keyManager.init(keyStore, mPass.toCharArray());
            mBks.close();
            return keyManager.getKeyManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException |
                IOException | CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取TrustManager
    public TrustManager[] getTrustManager() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int alias = 0;
            if (mCers != null) {
                for (InputStream is : mCers) {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    Certificate certificate = cf.generateCertificate(is);
                    keyStore.setCertificateEntry(Integer.toString(alias++), certificate);
                    is.close();
                }
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(keyStore);
                chooseX509(tmf);
                return tmf.getTrustManagers();
            } else {
                TrustManager[] trustManagers = {new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }};
                x509 = (X509TrustManager) trustManagers[0];
                return trustManagers;
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | IOException |
                CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void chooseX509(TrustManagerFactory tmf) {
        for (TrustManager trustManager : tmf.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                x509 = (X509TrustManager) trustManager;
                break;
            }
        }
    }

    public X509TrustManager getX509() {
        return x509;
    }

    public HostnameVerifier hostNameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }
}
