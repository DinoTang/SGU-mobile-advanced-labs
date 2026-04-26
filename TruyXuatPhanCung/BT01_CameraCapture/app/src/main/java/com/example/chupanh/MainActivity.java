package com.example.chupanh;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private ImageView imgPreview;
    private Button btnCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgPreview = findViewById(R.id.imgPreview);
        btnCapture = findViewById(R.id.btnCapture);

        // Đăng ký nhận kết quả sau khi chụp ảnh
        ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Lấy ảnh thumbnail (ảnh nhỏ) trả về từ Camera
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imgPreview.setImageBitmap(imageBitmap);
                        saveImageToInternalStorage(imageBitmap);
                        Toast.makeText(this, "Đã chụp ảnh thành công!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btnCapture.setOnClickListener(v -> {
            // Kiểm tra quyền Camera trước khi mở
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                // Mở ứng dụng Camera
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(takePictureIntent);
            }
        });
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        // 1. Tạo tên file dựa trên thời gian để không bị trùng
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        // 2. Mở luồng ghi vào file trong bộ nhớ riêng của App
        try (java.io.FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE)) {
            // 3. Nén và ghi dữ liệu
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            // Lấy đường dẫn để kiểm tra
            String path = getFilesDir().getAbsolutePath() + "/" + fileName;
            Toast.makeText(this, "Đã lưu tại: " + path, Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


}