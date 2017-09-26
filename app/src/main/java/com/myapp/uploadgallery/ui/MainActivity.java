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
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.uploadgallery.BuildConfig;
import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.manager.GalleryManager;
import com.myapp.uploadgallery.ui.util.DialogPool;
import com.myapp.uploadgallery.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements Viewable,
        ManipulatorViewable.ManipulatorUiListener, HasSupportFragmentInjector {
    public static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final int REQUEST_IMAGE_GALLERY = 102;
    public static final int PERMISSION_REQUEST_GALLERY = 103;
    public static final int PERMISSION_REQUEST_CAMERA = 104;
    public static final String GALLERY = "GALLERY";
    public static final String MANIPULATOR = "MANIPULATOR";
    public static final String NETWORK = "NETWORK";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.tvStub)
    TextView emptyText;

    @Inject
    GalleryManager galleryManager;

    private GalleryViewable galleryViewable;
    private ManipulatorViewable manipulatorViewable;

    @Inject
    DialogPool dialogPool;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

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

        galleryManager.setView(this);
        galleryManager.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        galleryManager.unsubscribe();
        galleryManager.setView(null);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        int ok = 0;
        DialogInterface.OnClickListener okListener = null;
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            ok = R.string.dialog_upload_camera;
            okListener = (DialogInterface dialog, int which) -> startCamera();
        }
        DialogInterface.OnClickListener cancelListener =
                (DialogInterface dialog, int which) -> startGallery();
        dialogPool.showDialog(GALLERY, this, R.string.dialog_upload_title,
                R.string.dialog_upload_message,
                ok, okListener, R.string.dialog_upload_gallery, cancelListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (manipulatorViewable == null) {
//            galleryManager.updateImages()
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer() {
//                        @Override
//                        public void accept(final Object o) throws Exception {
//
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(final Throwable throwable) throws Exception {
//                            showProgress(false);
//                            showStubText();
//                            showNetworkAlert(throwable);
//                        }
//                    });
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
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
            takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID,
                    FileUtils.getPictureFile(this));
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            takePictureIntent.setDataAndType(uri, "image/*");
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
            showProgress(true);
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                File file = FileUtils.getPictureFile(this);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                showManipulator(bitmap);
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

    private void showSnack(@StringRes int resId) {
        Snackbar.make(fab, resId, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_settings, settingsListener).show();
    }

    public void showStubText() {
        if (galleryViewable != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove((Fragment) galleryViewable)
                    .commit();
            galleryViewable = null;
        }
        emptyText.setVisibility(View.VISIBLE);
    }

    public void showGallery(final List<GalleryImage> images) {
        if (galleryViewable == null) {
            // Create new fragment and transaction
            GalleryFragment newFragment = new GalleryFragment();
            android.support.v4.app.FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.flFragment, newFragment, GALLERY);
            transaction.commit();
            galleryViewable = newFragment;
//            galleryViewable.setCallback(galleryManager.getGalleryListener());
        }
//        galleryViewable.setImages(images);
    }

    public void showManipulator(Bitmap bitmap) {
        showProgress(false);
        if (manipulatorViewable == null) {
            ManipulatorFragment newFragment = new ManipulatorFragment();
            android.support.v4.app.FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.flFragment, newFragment, GALLERY);
            transaction.addToBackStack(MANIPULATOR);
            transaction.commit();
            manipulatorViewable = newFragment;
//            manipulatorViewable.setManipulatorListeners(galleryManager.getManipulatorListener(),
//                    this);
        }
        manipulatorViewable.setBitmapToManipulate(bitmap);
        showFab(false);
    }

    public void showNetworkAlert(final Throwable throwable) {
        dialogPool.showDialog(NETWORK, this, R.string.dialog_network_title,
                R.string.dialog_network_message, android.R.string.ok, null, 0, null);
    }

    public void showUploadAlert(final Throwable throwable) {
        dialogPool.showDialog(MANIPULATOR, this, R.string.dialog_manipulation_title,
                R.string.dialog_manipulation_message, android.R.string.ok, null, 0, null);
    }

    @Override
    public void closeManipulator() {
        if (manipulatorViewable != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove((Fragment) manipulatorViewable)
                    .commit();
            manipulatorViewable = null;
        }
        showFab(true);
    }

    @Override
    public void onManipulationError() {
        showProgress(false);
        closeManipulator();
    }

    private void showFab(boolean show) {
        fab.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFetchImagesStarted() {
        showProgress(true);
    }

    @Override
    public void onFetchImagesCompleted(final List<GalleryImage> imageList) {
        showProgress(false);
        if (imageList.isEmpty()) {
            showStubText();
        } else {
            showGallery(imageList);
        }
    }

    @Override
    public void onFetchImagesError(final Throwable throwable) {
        showNetworkAlert(throwable);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }
}
