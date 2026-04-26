package com.example.bt01_canvasanimation;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class MovingCanvasView extends View {

    private Paint paint;
    private float x = 100;
    private float y = 200;

    private float dx = 8; // tốc độ di chuyển

    private Handler handler = new Handler();

    public MovingCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);

        startAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // vẽ hình tròn
        canvas.drawCircle(x, y, 60, paint);
    }

    private void startAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // cập nhật vị trí
                x += dx;

                // đụng biên thì bật lại
                if (x > getWidth() - 60 || x < 60) {
                    dx = -dx;
                }

                // vẽ lại
                invalidate();

                // lặp lại
                handler.postDelayed(this, 16); // ~60 FPS
            }
        }, 16);
    }
}