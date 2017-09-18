package com.myapp.uploadgallery.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.model.UpImage;
import com.myapp.uploadgallery.presenter.GalleryManager;
import com.myapp.uploadgallery.util.UniqueList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements Viewable {
    public static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final int REQUEST_IMAGE_GALLERY = 102;
    public static final int PERMISSION_REQUEST_GALLERY = 103;
    public static final int PERMISSION_REQUEST_CAMERA = 104;
    public static final String GALLERY = "GALLERY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    GalleryManager galleryManager;

    private GalleryAdapter adapter;
    private GalleryViewable galleryViewable;

    private View.OnClickListener settingsListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        adapter = new GalleryAdapter(this);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_upload_title)
                .setMessage(R.string.dialog_upload_message)
                .setNegativeButton(R.string.dialog_upload_gallery,
                        (DialogInterface dialog, int which) -> startGallery());
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            builder.setPositiveButton(R.string.dialog_upload_camera,
                    (DialogInterface dialog, int which) -> startCamera());
        }
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        galleryManager.onResume();
    }

    public void startCamera() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_GALLERY);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void startGallery() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_GALLERY);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (galleryIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    showNoCameraSnack();
                }
                break;
            }
            case PERMISSION_REQUEST_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGallery();
                } else {
                    showNoGallerySnack();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                showManipulator(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    showManipulator(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    }
    protected void showNoCameraSnack() {
        showSnack(R.string.no_camera_permission);
    }

    protected void showNoGallerySnack() {
        showSnack(R.string.no_gallery_permission);
    }

    protected void showSnack(@StringRes int resId) {
        Snackbar.make(fab, resId, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_settings, settingsListener).show();
    }

    @Override
    public void showStubText() {
        if (galleryViewable != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove((Fragment) galleryViewable)
                    .commit();
        }
    }

    @Override
    public void showGallery(final UniqueList<UpImage> images) {
        if (galleryViewable == null) {
            // Create new fragment and transaction
            GalleryFragment newFragment = new GalleryFragment();
            android.support.v4.app.FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.add(R.id.flFragment, newFragment, GALLERY);

            // Commit the transaction
            transaction.commit();
            galleryViewable = newFragment;
        }
        galleryViewable.setImages(images);
    }

    public void showManipulator(Bitmap bitmap) {
        // TODO: 9/18/17 show manipulator fragment
        galleryManager.onPictureChosen(this, bitmap)
                .flatMap((File file) -> galleryManager.uploadCachedPicture(this, file))
                .subscribeOn(Schedulers.io())
                .doOnError((Throwable t) -> t.printStackTrace())
                .doOnTerminate(() -> galleryManager.onResume())
                .subscribe();
    }
}
