package ru.goodibunakov.testforzennex2018.fragments;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import ru.goodibunakov.testforzennex2018.BuildConfig;
import ru.goodibunakov.testforzennex2018.R;
import ru.goodibunakov.testforzennex2018.activities.PhotoActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PhotoFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE = 500;
    Button btnPhoto, btnGallery;
    private static final int PHOTO_INTENT_REQUEST_CODE = 100;
    private static final int GALLERY_INTENT_REQUEST_CODE = 1;
    public Uri uri;

    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_photo, container, false);
        btnGallery = v.findViewById(R.id.btn_gallery);
        btnPhoto = v.findViewById(R.id.btn_photo);
        btnGallery.setOnClickListener(PhotoFragment.this);
        btnPhoto.setOnClickListener(PhotoFragment.this);
        uri = generateFileUri();
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gallery:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        //Тип получаемых объектов - image:
                        photoPickerIntent.setType("image/*");
                        //Запускаем переход с ожиданием обратного результата в виде информации об изображении
                        startActivityForResult(photoPickerIntent, GALLERY_INTENT_REQUEST_CODE);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    }
                }
                break;
            case R.id.btn_photo:
                if (uri == null) {
                    Toast.makeText(getView().getContext(), getResources().getString(R.string.sd_not), Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intentCamera, PHOTO_INTENT_REQUEST_CODE);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturned) {
        switch (requestCode) {
            //если результат пришел от галереи
            case GALLERY_INTENT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage_gallery = null;
                    if (imageReturned != null) selectedImage_gallery = imageReturned.getData();
                    Uri preinsertedUri;
                    preinsertedUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                    if (selectedImage_gallery == null && preinsertedUri != null)
                        selectedImage_gallery = preinsertedUri;
                    Log.i("vv", "uri ok" + selectedImage_gallery.toString());
                    Intent last_intent_gallery = new Intent(getView().getContext(), PhotoActivity.class);
                    last_intent_gallery.putExtra("fotka", selectedImage_gallery);
                    startActivity(last_intent_gallery);
                }
                break;
            case PHOTO_INTENT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //берем сгенерированный uri
                    Uri selectedImage = uri;
                    //создаем интент для запуска новой активити
                    Intent last_intent_photo = new Intent(getView().getContext(), PhotoActivity.class);
                    //помещаем в интент этот uri
                    last_intent_photo.putExtra("fotka", selectedImage);
                    //стартуем новую активити
                    startActivity(last_intent_photo);

                } else if (resultCode == RESULT_CANCELED)
                    Toast.makeText(getView().getContext(), "Capture cancelled", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getView().getContext(), "Capture failed", Toast.LENGTH_LONG).show();
                break;
            default: super.onActivityResult(requestCode, resultCode, imageReturned);
        }
    }

    private Uri generateFileUri() {
        // Проверяем доступность SD карты
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;

        // Проверяем и создаем директорию
        File path = new File (Environment.getExternalStorageDirectory(), "CameraTest");
        if (! path.exists()){
            if (! path.mkdirs()){
                return null;
            }
        }

        // Создаем имя файла
        String timeStamp = String.valueOf(System.currentTimeMillis());
        File newFile = new File(path.getPath() + File.separator + timeStamp + ".jpg");
        return FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", newFile);
    }

    // сохраняеем состояние
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (uri != null) {
            outState.putString("uri", uri.toString());
        }
    }

    // восстанавливаем состояние
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("uri")) {
            uri = Uri.parse(savedInstanceState.getString("uri"));
        }
    }
}