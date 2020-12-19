package com.library.mylibrary.apicalls;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.library.mylibrary.interfaces.CallbackInterface;
import com.library.mylibrary.ProfileMatchingActivity;
import com.library.mylibrary.interfaces.CompareFaceInterface;
import com.library.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;

public class CompareFaceLabelAsycTask extends AsyncTask<Void,Void, List<CompareFacesMatch>> {

    private List<CompareFacesMatch> compareFacesMatchList= new ArrayList<>();
    private String cameraImageName, imagePickerName;
    private CompareFaceInterface compareFaceInterface;
    private CallbackInterface callBackInterface;
    private Boolean checkedValue = false;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    public CompareFaceLabelAsycTask(String cameraImageName, String imagePickerName, ProfileMatchingActivity profileMatchingActivity, CallbackInterface callBackInterface, String accesskey, String secretKey, String wddOnboardingBucket) {
        this.cameraImageName=cameraImageName;
        this.imagePickerName=imagePickerName;
        this.compareFaceInterface= (CompareFaceInterface) profileMatchingActivity;
        this.callBackInterface=callBackInterface;
        this.accessKey=accesskey;
        this.secretKey=secretKey;
        this.bucketName=wddOnboardingBucket;
    }

    @Override
    protected List<CompareFacesMatch> doInBackground(Void... voids) {
        AmazonRekognition rekognitionClient = new AmazonRekognitionClient(new BasicAWSCredentials(accessKey, secretKey));
        CompareFacesRequest compareFacesRequest = new CompareFacesRequest().withSourceImage(new Image()
                .withS3Object(new S3Object()
                        .withName(cameraImageName).withBucket(bucketName))).withTargetImage(new Image()
                .withS3Object(new S3Object()
                        .withName(imagePickerName).withBucket(bucketName))).withSimilarityThreshold((80F));
        try {
            CompareFacesResult result = rekognitionClient.compareFaces(compareFacesRequest);
            List<CompareFacesMatch> lists = result.getFaceMatches();
            compareFacesMatchList=lists;
            if(lists!=null)
            {
                checkedValue=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compareFacesMatchList;
    }

    @Override
    protected void onPostExecute(List<CompareFacesMatch> list) {
        super.onPostExecute(list);
        compareFaceInterface.compareFaceCallback(list,checkedValue);
        callBackInterface.onTaskComplete();

    }

}
