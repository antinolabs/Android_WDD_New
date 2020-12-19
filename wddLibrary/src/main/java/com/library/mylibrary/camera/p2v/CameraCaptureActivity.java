package com.library.mylibrary.camera.p2v;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.library.mylibrary.R;
import com.library.mylibrary.camera.FunCamera;
import com.library.mylibrary.imageCompressor.CompressionPredicate;
import com.library.mylibrary.imageCompressor.NativeCompressor;
import com.library.mylibrary.imageCompressor.OnCompressListener;
import com.library.mylibrary.imageCompressor.OnRenameListener;

import java.io.File;
import java.util.List;




public class CameraCaptureActivity extends AppCompatActivity implements CameraCaptureInterface {
    public static final String MODE = "MODE";
    public static final String DURATION = "DURATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);
        /**
         * 传过来的配置
         */
        int mode = getIntent().getIntExtra(MODE, 0);
        long duration = getIntent().getLongExtra(DURATION, 0);
        if (mode == 0 || duration == 0) {
            throw new RuntimeException("mode or duration can`t be zero!");
        }

        /**
         * 进入默认的fragment 进行预览
         */
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.camera_capture_main_framelayout, CameraCaptureRecordFragment.newInstance(mode, duration))
                    .commit();
        }
    }


    @Override
    public void onBackPressed() {
        try {
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            ((BaseFragment) fragmentList.get(fragmentList.size() - 1)).finish();
        } catch (Exception e) {
            finish();
        }
    }

    // ——————————————————————————————————————————————————————————————————
    @Override
    public void returnPath(int type, final String path) {

        if (type == Util.Const.类型_照片) {
            // 开始压缩
            final ProgressDialog dialog = ProgressDialog.show(CameraCaptureActivity.this, "Please wait", "Compressing picture", false, false);
            NativeCompressor.with(this)
                    .load(path)
                    .ignoreBy(5000000)
                    .setTargetDir(getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath())
                    .setRenameListener(new OnRenameListener() {
                        @Override
                        public String rename(String filePath) {
                            return Util.randomName() + ".jpg";
                        }
                    })
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String s) {
                            return !TextUtils.isEmpty(s);
                        }
                    })
                    .setCompressListener(new OnCompressListener() {

                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            if (dialog != null) dialog.dismiss();
                            // 压缩成功
                            Intent intent = new Intent();
                            intent.putExtra(FunCamera.DATA, file.getAbsolutePath());
                            intent.putExtra(FunCamera.DATA_ORIGIN, path);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (dialog != null) dialog.dismiss();
                            // 压缩失败 返回原图
                            Intent intent = new Intent();
                            intent.putExtra(FunCamera.DATA, path);
                            intent.putExtra(FunCamera.DATA_ORIGIN, path);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    })
                    .launch();

        } else if (type == Util.Const.类型_视频) {
            // 直接返回
            Intent intent = new Intent();
            intent.putExtra(FunCamera.DATA, path);
            intent.putExtra(FunCamera.DATA_ORIGIN, path);
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    // ——————————————————————————————————————————————————————————————————


}
