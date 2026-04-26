package com.example.bai2; // Hãy đảm bảo tên này trùng với tên package khi bạn tạo project

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {
    private MapView map;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker userMarker;
    private static final int REQUEST_CODE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- BẮT BUỘC: Cấu hình cho OSM ---
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        // Dòng này cực kỳ quan trọng: Định danh ứng dụng để máy chủ OSM cho phép tải bản đồ
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_main);

        // Khởi tạo MapView
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getController().setZoom(15.0); // Độ zoom mặc định

        // Khởi tạo dịch vụ vị trí của Google (để lấy tọa độ)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Lắng nghe sự thay đổi vị trí
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        updateLocationOnMap(location);
                    }
                }
            }
        };

        checkPermissionAndStartTracking();
    }

    private void checkPermissionAndStartTracking() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        // Yêu cầu cập nhật mỗi 5 giây (5000ms)
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
        }
    }

    private void updateLocationOnMap(Location location) {
        GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());

        if (userMarker == null) {
            // Lần đầu tiên lấy được vị trí: tạo mới Marker
            userMarker = new Marker(map);
            userMarker.setTitle("Tôi đang ở đây");
            userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(userMarker);

            // Di chuyển camera đến vị trí đầu tiên
            map.getController().setCenter(point);
        }

        // Cập nhật vị trí Marker và di chuyển bản đồ theo
        userMarker.setPosition(point);
        map.getController().animateTo(point);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Bạn cần cấp quyền vị trí để ứng dụng hoạt động!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        // Dừng cập nhật vị trí khi thoát app để đỡ tốn pin
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}