package top.wangudiercai.chatclient;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import top.wangudiercai.common.ObjectMapperFactory;
import top.wangudiercai.common.Result;
import top.wangudiercai.common.UploadUtil;
import top.wangudiercai.domain.Identity;
import top.wangudiercai.view.CircularImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {
    private CircularImageView imageView;
    private EditText editText;
    private Button button;
    private UploadUtil uploadUtil;
    private ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
    private Handler handler;
    private Identity identity = new Identity("http://localhost:10800/avatars/b49421a7-f29e-4c08-acb4-f312cbde9ccb.jpg", "wangudiercai");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        imageView = findViewById(R.id.avatar_image);
        editText = findViewById(R.id.name_edit);
        button = findViewById(R.id.login_button);
        uploadUtil = UploadUtil.getInstance();
        handler = new Handler((msg) -> {
            Log.i("MainActivity", "new message received");
            String s = (String) msg.getData().get("newAvatarUrl");
            DisplayImageOptions options = new DisplayImageOptions
                    .Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
            imageLoader.displayImage(s, imageView, options);
            identity.setAvatarUrl(s);
            return false;
        });
        uploadUtil.setOnUploadProcessListener(new UploadUtil.OnUploadProcessListener() {
            @Override
            public void onUploadDone(int responseCode, String message) {
                Result<String> s = null;
                try {
                    s = (Result<String>) objectMapper.readValue(message, Result.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (s.getCode() == 0) {
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("newAvatarUrl", s.getData());
                    msg.setData(bundle);
                    msg.sendToTarget();
                }
                Log.d("wangudie", "onUploadDone: " + s.getMsg());
            }

            @Override
            public void onUploadProcess(int uploadSize) {
                Log.d("wangudie", "onUploadProcess: " + uploadSize);
            }

            @Override
            public void initUpload(int fileSize) {
                Log.d("wangudie", "initUpload: " + fileSize);
            }
        });
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);

        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                identity.setName(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        button.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("name", identity.getName());
            intent.putExtra("avatarUrl", identity.getAvatarUrl());
            startActivity(intent);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            File file = new File(uriToFileApiQ(getApplicationContext(), uri));
            uploadUtil.uploadFile(file, "file", "http://www.wangudiercai.top:10800/api/upload", null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static String uriToFileApiQ(Context context, Uri uri) {
        File file = null;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                try {
                    InputStream is = contentResolver.openInputStream(uri);
                    File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
                    FileOutputStream fos = new FileOutputStream(cache);
                    FileUtils.copy(is, fos);
                    file = cache;
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }
}
