package ru.goodibunakov.testforzennex2018.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

import java.io.IOException;

import ru.goodibunakov.testforzennex2018.R;
import ru.goodibunakov.testforzennex2018.utils.TouchImageView;

public class PhotoActivity extends AppCompatActivity {

    TouchImageView imageView;
    private Bitmap bmp;
    RelativeLayout relativeLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = findViewById(R.id.full_photo);
        relativeLayout = findViewById(R.id.rl);

        //получаем интент, достаем из него фотку и ставим в имиджвью
        Intent intent = getIntent();
        Uri uri = intent.getExtras().getParcelable("fotka");
        try {
            bmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bmp);
        imageView.setMaxZoom(4f);

        //инициализация зум контролс
        final ZoomControls simpleZoomControls = findViewById(R.id.zoom_controls);

        // увеличение
        simpleZoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // вычисляем текущие значения масштаба x и y ImageView
                float x = imageView.getScaleX();
                float y = imageView.getScaleY();
                // устанавливаем шаг увеличения картинки
                imageView.setScaleX((float) (x + 0.1));
                imageView.setScaleY((float) (y + 0.1));
            }
        });

        // уменьшение
        simpleZoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // вычисляем текущие значения масштаба x и y ImageView
                float x = imageView.getScaleX();
                float y = imageView.getScaleY();
                // устанавливаем шаг уменьшения картинки
                imageView.setScaleX((float) (x - 0.1));
                imageView.setScaleY((float) (y - 0.1));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final View decorView = getWindow().getDecorView();
        hideSystemUI(decorView);
    }

    // скрываем системные панели
    private void hideSystemUI(View decorView) {

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN); // hide status bar
        }
    }
}