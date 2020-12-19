/*In this Activity we get the Camera image, file type and name from WDDStarter Activity.
  After that we get the same thing with image picker.
  then Upload the image in S3 bucket.
  For Uploading part  we use AWS Cognitio services and S3 bucket service

  Created By :- Prakhar Pandey
  Date:- 6/3/2020
 */
package com.library.mylibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.library.mylibrary.camera.FunCamera;
import com.library.mylibrary.imagecropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.library.mylibrary.Constants.BUCKET_NAME;
import static com.library.mylibrary.imagecropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.library.mylibrary.imagecropper.CropImage.getActivityResult;


public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Upload Activity";
    private Button buttonUpload, buttonPicker;
    private ImageView imageViewPickImage, imageViewCameraImage;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private static final int REQUEST_IMAGE_CAPTURE = 1176, ALL_PERMISSIONS_RESULT_CAMERA = 1107, THUMBNAIL_SIZE = 200;
    private File fileCameraWDD, fileCameraUpload;
    public static Bitmap bitmapCameraWdd,bitmapCameraUpload;
    private Boolean stateCheckImage1 = false, stateCheckImage2 = false;
    private TransferUtility transferUtility;
    public static String cameraPathWDD, cameraPathUpload;
    private String accessKey, secretKey, cognitoPoolId, wddOnboardingBucket;
    private int selectedColor;
    private ProgressDialog progressDialogUpload;
    private LinearLayout backgroundHeader;
    private File camerafileLocationUpload;
    private String pathOrigin;
    private  int tableText=0,formText=0,rawText=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        //Getting the image of Camera from WDDStarterActivity
        bitmapCameraWdd = WDDStarter.bitmap;
        //Getting the filetype of Camera frm WddStarterActivity
        fileCameraWDD = (File) getIntent().getSerializableExtra(Constants.FILETYPE);
        accessKey = getIntent().getStringExtra(Constants.ACCESS_KEY);
        secretKey = getIntent().getStringExtra(Constants.SECRET_KEY);
        cognitoPoolId = getIntent().getStringExtra(Constants.COGNITO_POOL_ID);
        wddOnboardingBucket = getIntent().getStringExtra(Constants.BUCKET_NAME);
        tableText= getIntent().getIntExtra(Constants.TABLETEXT,0);
        formText= getIntent().getIntExtra(Constants.FORMTEXT,0);
        rawText=getIntent().getIntExtra(Constants.RAWTEXT,0);
        selectedColor = getIntent().getIntExtra(Constants.COLORTYPE, 0);
        Log.d(TAG, "onCreate1: " + accessKey + secretKey + cognitoPoolId + wddOnboardingBucket);
        //GetName of Camera.
        cameraPathWDD = fileCameraWDD.getName();
        init();
       if(bitmapCameraWdd!=null)
           imageViewCameraImage.setImageBitmap(bitmapCameraWdd);

        createTransferUtility();
        onclick();

        if (selectedColor == 0) {
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable((getColor(android.R.color.holo_blue_bright))));
        } else {
            getSupportActionBar().setBackgroundDrawable(new
                    ColorDrawable((selectedColor)));
        }
    }

    public void createTransferUtility() {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                cognitoPoolId, // Identity pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);
        transferUtility = TransferUtility.builder().s3Client(s3Client).context(getApplicationContext()).build();
    }

    private void onclick() {
        buttonPicker.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }


    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    public void openCamera() {
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT_CAMERA);
            } else {
                FunCamera.capturePhoto(UploadActivity.this, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    private void init() {
        buttonPicker = findViewById(R.id.wdd_image_camera_btn2);
        buttonUpload = findViewById(R.id.wdd_upload_btn);
        imageViewPickImage = findViewById(R.id.wdd_image_chooser_pick_image);
        imageViewCameraImage = findViewById(R.id.wdd_camera_pick_image);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmapCameraUpload = getThumbnail(resultUri);
                    imageViewPickImage.setImageBitmap(bitmapCameraUpload);
                } catch (Exception e) {

                }
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null) {
                pathOrigin = data.getStringExtra(FunCamera.DATA_ORIGIN);
                Log.d(TAG, "onActivityResult: Path:" + pathOrigin);
                try {
                    camerafileLocationUpload = new File(pathOrigin);
                    cameraPathUpload = camerafileLocationUpload.getName();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri apkURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", camerafileLocationUpload);
                        performCrop(apkURI);
                    } else {

                        performCrop(Uri.fromFile(camerafileLocationUpload));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }


    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    private void performCrop(Uri picUri) {
        CropImage.activity(picUri)
                .start(this);
    }

//    String getFileNameFromUri(Uri uri) {
//        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
//        int nameIndex = 0;
//        if (returnCursor != null) {
//            nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//            returnCursor.moveToFirst();
//            String name = returnCursor.getString(nameIndex);
//            returnCursor.close();
//            return name;
//        } else {
//            return "";
//        }
//    }

//    File createFileFromUri(Uri uri, String objectKey) throws IOException {
//        InputStream is = getContentResolver().openInputStream(uri);
//        File file = new File(getCacheDir(), objectKey);
//        file.createNewFile();
//        FileOutputStream fos = new FileOutputStream(file);
//        byte[] buf = new byte[2046];
//        int read = -1;
//        while ((read = is.read(buf)) != -1) {
//            fos.write(buf, 0, read);
//        }
//        fos.flush();
//        fos.close();
//        return file;
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.wdd_image_camera_btn2) {
            buttonPicker.setVisibility(View.INVISIBLE);
            buttonUpload.setVisibility(View.VISIBLE);
            openCamera();
        } else if (v.getId() == R.id.wdd_upload_btn) {
            Log.d(TAG, "progress bar is shown: " + stateCheckImage1 + stateCheckImage2);
            upload(fileCameraWDD, camerafileLocationUpload, cameraPathWDD, cameraPathUpload);
            Log.d(TAG, "progress bar is shown: " + stateCheckImage1 + stateCheckImage2);
            progressDialogUpload = new ProgressDialog(UploadActivity.this);
            progressDialogUpload.setMessage("Uploading file... ");
            progressDialogUpload.setIndeterminate(true);
            progressDialogUpload.setCancelable(false);
            progressDialogUpload.show();


        }
    }


    void upload(File fileCamera, File fileImagePicker, final String objectKey1, String objectKey2) {
        Log.d(TAG, "onStateChanged: " + "upload method called");
        TransferObserver transferObserver = transferUtility.upload(
                wddOnboardingBucket,
                objectKey1,
                fileCamera, CannedAccessControlList.PublicRead
        );
        transferObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED.equals(state)) {
                    progressDialogUpload.dismiss();
                    Toast.makeText(UploadActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(UploadActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        TransferObserver transferObserverTargetImage = transferUtility.upload(
                wddOnboardingBucket,
                objectKey2,
                fileImagePicker, CannedAccessControlList.PublicRead
        );
        transferObserverTargetImage.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED.equals(state)) {
                    progressDialogUpload.dismiss();
                    Log.d(TAG, "onCreate2: " + accessKey + " " + secretKey + " " + cognitoPoolId + " " + wddOnboardingBucket);
                    startActivity(new Intent(UploadActivity.this, ProfileMatchingActivity.class)
                            .putExtra(Constants.ACCESS_KEY, accessKey)
                            .putExtra(Constants.SECRET_KEY, secretKey)
                            .putExtra(BUCKET_NAME, wddOnboardingBucket)
                            .putExtra(Constants.TABLETEXT,tableText)
                            .putExtra(Constants.FORMTEXT,formText)
                            .putExtra(Constants.RAWTEXT,rawText)
                            .putExtra(Constants.COLORTYPE, selectedColor));
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }


            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(UploadActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}


