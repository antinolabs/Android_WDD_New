package com.library.mylibrary.apicalls;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.DetectTextResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.TextDetection;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClient;
import com.amazonaws.services.textract.model.AnalyzeDocumentRequest;
import com.amazonaws.services.textract.model.AnalyzeDocumentResult;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DetectDocumentTextRequest;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import com.amazonaws.services.textract.model.Document;
import com.amazonaws.services.textract.model.Relationship;
import com.amazonaws.services.textract.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.library.mylibrary.interfaces.CallbackInterface;
import com.library.mylibrary.ProfileMatchingActivity;
import com.library.mylibrary.interfaces.TextDetectionInterface;
import com.library.mylibrary.Constants;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DetectTextAsycTask extends AsyncTask<Void, Void, List<Block>> {

    private Bitmap ImagePickerImage;
    private List<Block> textDetectionsResult;
    private ProfileMatchingActivity profileMatchingActivity;
    SharedPreferences sharedPreferences;
    private TextDetectionInterface textDetctionInterface;
    private CallbackInterface callBackInterface;
    private Boolean checkBooleanResulDetectText = false;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String imageName;
    ByteBuffer imageBytes;
    AnalyzeDocumentResult result;

    public DetectTextAsycTask(String imageName, Bitmap cameraImageName, ProfileMatchingActivity profileMatchingActivity, CallbackInterface callBackInterface, String accessKey, String secretKey, String bucketName, int formText, int rawText, int tableText) {
        this.imageName = imageName;
        this.ImagePickerImage = cameraImageName;
        this.textDetctionInterface = (TextDetectionInterface) profileMatchingActivity;
        this.callBackInterface = callBackInterface;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
    }

    @Override
    protected List<Block> doInBackground(Void... voids) {
        AmazonTextract rekognitionClientText = new AmazonTextractClient(new BasicAWSCredentials(accessKey, secretKey));
        try {
            AnalyzeDocumentRequest request = new AnalyzeDocumentRequest()
                    .withFeatureTypes("TABLES", "FORMS")
                    .withDocument(new Document().withS3Object(new S3Object().withName(imageName).withBucket(bucketName))
                            .withBytes(imageBytes));
            result = rekognitionClientText.analyzeDocument(request);
            textDetectionsResult = result.getBlocks();
            if (textDetectionsResult != null) {
                checkBooleanResulDetectText = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textDetectionsResult;
    }


    @Override
    protected void onPostExecute(List<Block> textDetections) {
        super.onPostExecute(textDetections);
        Log.d("Bye", "onPostExecute: ");
        textDetctionInterface.textDetectionCallback(textDetections, checkBooleanResulDetectText);
        callBackInterface.onTaskComplete();
    }
}
