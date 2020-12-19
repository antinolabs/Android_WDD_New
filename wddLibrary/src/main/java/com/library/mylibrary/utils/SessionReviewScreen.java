package com.library.mylibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.facetec.sdk.FaceTecIDScanStatus;
import com.facetec.sdk.FaceTecSDK;
import com.facetec.sdk.FaceTecSessionResult;
import com.facetec.sdk.FaceTecSessionStatus;
import com.library.mylibrary.WDDStarter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import Processors.LivenessCheckProcessor;
import Processors.Processor;

public class SessionReviewScreen {

    Processor latestProcessor;
    byte[] latestFaceScan;
    byte[] latestIDScan;
    String latestSessionId;
    FaceTecSessionStatus latestSessionStatus;
    FaceTecIDScanStatus latestIDScanStatus;
    Integer latest3dAgeEstimateGroup;
    Integer latestMatchLevel;
    Integer latestDigitalSpoofStatus;
    Integer latestFullIDStatus;
    String[] latestAuditTrailImages;
    String[] latestLowQualityAuditTrailImages;
    String[] latestIDScanFrontImages;
    JSONObject latestFaceScanSecurityChecks;

    private WDDStarter wddStarter;
    boolean isLoaded = false;

    public void load(Processor processor, Runnable callback) {
        isLoaded = true;
        latestProcessor = processor;
        updateMode();
        updateLivenessResult();
        updateAuditTrailImages();
        updateSessionDataSizes();
        callback.run();
    }

    public SessionReviewScreen(WDDStarter activity) {
        wddStarter = activity;
    }

    public void reset() {
        latestProcessor = null;
        latestFaceScan = null;
        latestIDScan = null;
        latestSessionId = null;
        latestSessionStatus = null;
        latestIDScanStatus = null;
        latest3dAgeEstimateGroup = null;
        latestMatchLevel = null;
        latestDigitalSpoofStatus = null;
        latestFullIDStatus = null;
        latestAuditTrailImages = null;
        latestLowQualityAuditTrailImages = null;
        latestIDScanFrontImages = null;
        latestFaceScanSecurityChecks = null;
    }

    String getModeFromProcessor(Processor processor) {
        String mode = "";
        if (processor instanceof LivenessCheckProcessor) {
            mode = "3D Liveness Check";
        }
        return mode;
    }

    public void setLatestServerResult(JSONObject responseJSON) {
        try {
            if (responseJSON.has("faceScanSecurityChecks")) {
                latestFaceScanSecurityChecks = responseJSON.getJSONObject("faceScanSecurityChecks");
            }
            if (responseJSON.has("matchLevel")) {
                latestMatchLevel = responseJSON.getInt("matchLevel");
            }
            if (responseJSON.has("ageEstimateGroupEnumInt")) {
                latest3dAgeEstimateGroup = responseJSON.getInt("ageEstimateGroupEnumInt");
            }
            if (responseJSON.has("digitalIDSpoofStatusEnumInt")) {
                latestDigitalSpoofStatus = responseJSON.getInt("digitalIDSpoofStatusEnumInt");
            }
            if (responseJSON.has("fullIDStatusEnumInt")) {
                latestFullIDStatus = responseJSON.getInt("fullIDStatusEnumInt");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("FaceTecSDKSampleApp", "Exception raised while attempting to parse JSON result for Session Review Screen.");
        }
    }

    boolean didLivenessPass() {
        boolean livenessPassed = false;
        try {
            if (latestFaceScanSecurityChecks != null && latestFaceScanSecurityChecks.has("faceScanLivenessCheckSucceeded")) {
                livenessPassed = latestFaceScanSecurityChecks.getBoolean("faceScanLivenessCheckSucceeded");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("FaceTecSDKSampleApp", "Exception raised while attempting to parse JSON result for Session Review Screen.");
        }
        return livenessPassed;
    }

    void updateLivenessResult() {
        String livenessResultDescription = "";
        if (didLivenessPass()) {
            livenessResultDescription = "Liveness Passed";
        } else {
            livenessResultDescription = "Liveness Not Proven";
        }

//        String livenessResultText = createBoldHTMLTextWithTitle(sampleAppActivity.getString(R.string.srs_liveness_result_title), livenessResultDescription);
//        setHTMLTextForTextView(sampleAppActivity.activityMainBinding.srsLivenessResultTextView, livenessResultText);
    }


    void updateAuditTrailImages() {
        if (latestAuditTrailImages != null && latestAuditTrailImages.length > 0) {
            try {
                byte[] decodedString = Base64.decode(latestAuditTrailImages[0], Base64.DEFAULT);
                if (!latestProcessor.isSuccess() && latestLowQualityAuditTrailImages != null && latestLowQualityAuditTrailImages.length > 0) {
                    decodedString = Base64.decode(latestAuditTrailImages[0], Base64.DEFAULT);
                }
                Bitmap auditImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                File bitmapfile = new File(Environment.getExternalStorageDirectory()
                        + "/Android/data/"
                        + wddStarter.getApplicationContext().getPackageName()
                        + "/Files");
                if (!bitmapfile.exists()){
                    if (! bitmapfile.mkdirs()){

                    }
                }
                // Create a media file name
                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
                File mediaFile;
                String mImageName="MI_"+ timeStamp +".jpg";
                mediaFile = new File(bitmapfile.getPath() + File.separator + mImageName);
                 try {
                     if(mediaFile!=null) {
                         FileOutputStream fos = new FileOutputStream(mediaFile);
                    auditImage.compress(Bitmap.CompressFormat.PNG, 90, fos);
                         fos.close();
                     }
                } catch (FileNotFoundException e) {
                   // Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                   // Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
                if(mediaFile!=null)
                    wddStarter.getThumbnail(auditImage,mediaFile);

            }catch (Exception e)
            {
                e.getMessage();
            }
        } else {

        }
    }

    String getSizeInKb(byte[] data) {
        return ((int) Math.ceil((double) data.length / 1000.0)) + "kb";
    }

    void updateSessionDataSizes() {
        String faceScanSizeDescription = "Unavailable";
        String idScanSizeDescription = "Unavailable";

        if (latestFaceScan != null) {
            faceScanSizeDescription = getSizeInKb(latestFaceScan);
        }
        else {
            //  sampleAppActivity.activityMainBinding.srsIDScanSizeTextView.setVisibility(View.GONE);
        }

//        String faceScanSizeText = createBoldHTMLTextWithTitle(sampleAppActivity.getString(R.string.srs_facescan_size_title), faceScanSizeDescription);
//        setHTMLTextForTextView(sampleAppActivity.activityMainBinding.srsFaceScanSizeTextView, faceScanSizeText);

//        String idScanSizeText = createBoldHTMLTextWithTitle(sampleAppActivity.getString(R.string.srs_idscan_size_title), idScanSizeDescription);
//        setHTMLTextForTextView(sampleAppActivity.activityMainBinding.srsIDScanSizeTextView, idScanSizeText);
    }

    public void loadAndShow(final Processor processor, final Runnable callback) {
        if (FaceTecSDK.getCameraPermissionStatus(wddStarter) != FaceTecSDK.CameraPermissionStatus.GRANTED) {
            // sampleAppActivity.utils.fadeInMainUI();
            if (callback != null) {
                callback.run();
            }
            return;
        }
        if (FaceTecSDK.isLockedOut(wddStarter) && isLoaded) {
            //  sampleAppActivity.utils.displayStatus("This device is currently locked out due to too many unsuccessful Session attempts. Please try again in a few minutes, or try on another device.");
            // sampleAppActivity.utils.fadeInMainUI();
            if (callback != null) {
                callback.run();
            }
            return;
        }

        wddStarter.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // sampleAppActivity.activityMainBinding.themeTransitionImageView.animate().alpha(0f).setDuration(300);
                load(processor, new Runnable() {
                    @Override
                    public void run() {
                        wddStarter.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.run();
                                }
                            }
                        });
                    }
                });
            }
        });
    }


    void updateMode() {
        String modeDescription = getModeFromProcessor(latestProcessor);

//        String modeText = createBoldHTMLTextWithTitle(sampleAppActivity.getString(R.string.srs_mode_title), modeDescription);
//        setHTMLTextForTextView(sampleAppActivity.activityMainBinding.srsModeTextView, modeText);
    }


    public void setLatestSessionResult(FaceTecSessionResult sessionResult) {
        if(sessionResult.getSessionId() != null) {
            latestSessionId = sessionResult.getSessionId();
        }
        if(sessionResult.getStatus() != null) {
            latestSessionStatus = sessionResult.getStatus();
        }
        if(sessionResult.getFaceScan() != null) {
            latestFaceScan = sessionResult.getFaceScan();
        }
        if(sessionResult.getAuditTrailCompressedBase64().length > 0) {
            latestAuditTrailImages = sessionResult.getAuditTrailCompressedBase64();
        }
        if(sessionResult.getLowQualityAuditTrailCompressedBase64().length > 0) {
            latestLowQualityAuditTrailImages = sessionResult.getLowQualityAuditTrailCompressedBase64();
        }
    }
}
