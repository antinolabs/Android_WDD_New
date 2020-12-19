package com.library.mylibrary.apicalls;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Instance;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.Parent;
import com.amazonaws.services.rekognition.model.S3Object;
import com.library.mylibrary.interfaces.CallbackInterface;
import com.library.mylibrary.ProfileMatchingActivity;
import com.library.mylibrary.interfaces.DetectLabelCallback;
import com.library.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DetectLabelAsycTask extends AsyncTask<Void,Void, List<Label>> {
    List<Label> labels= new ArrayList<>();
    private String imagePickeImage;
    private Boolean checkBooleanResulDetectLabel=false;
    public DetectLabelCallback detectLabelsRequest;
    private CallbackInterface callBackInterface;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    public DetectLabelAsycTask(String imagePickerImage, ProfileMatchingActivity profileMatchingActivity, CallbackInterface commonCallBackInterface, String accesskey, String secretKey, String wddOnboardingBucket) {
        this.imagePickeImage=imagePickerImage;
        this.detectLabelsRequest = (DetectLabelCallback) profileMatchingActivity;
        this.callBackInterface = commonCallBackInterface;
        this.accessKey=accesskey;
        this.secretKey=secretKey;
        this.bucketName=wddOnboardingBucket;

    }

    @Override
    protected List<Label> doInBackground(Void... voids) {
        try {
            AmazonRekognition rekognitionClient = new AmazonRekognitionClient(new BasicAWSCredentials(accessKey, secretKey));
            DetectLabelsRequest request = new DetectLabelsRequest()
                    .withImage(new Image().withS3Object(new S3Object().withName(imagePickeImage).withBucket(bucketName)))
                    .withMaxLabels(10).withMinConfidence(75F);
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            labels = result.getLabels();
            if(labels!=null)
            {
                checkBooleanResulDetectLabel=true;
            }
            Log.d(TAG, "run: " + labels.size());
            for (Label label : labels) {
                Log.d(TAG, label.getName());
                Log.d(TAG, label.getConfidence().toString() + "\n");
                List<Instance> instances = label.getInstances();
                Log.d(TAG, label.getName());
                if (instances.isEmpty()) {
                    Log.d(TAG, "None");
                } else {
                    for (Instance instance : instances) {
                        Log.d(TAG, instance.getConfidence().toString());
                        Log.d(TAG, instance.getBoundingBox().toString());
                    }
                }
                Log.d(TAG, label.getName() + ":");
                List<Parent> parents = label.getParents();
                if (parents.isEmpty()) {
                    Log.d(TAG, "  None");
                } else {
                    for (Parent parent : parents) {
                        Log.d(TAG, parent.getName());
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return  labels;
    }

    @Override
    protected void onPostExecute(List<Label> labels) {
        super.onPostExecute(labels);
        detectLabelsRequest.detectLabelCallBack(labels,checkBooleanResulDetectLabel);
        callBackInterface.onTaskComplete();

    }
}
