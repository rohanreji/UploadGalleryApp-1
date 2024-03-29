package com.myapp.uploadgallery.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myapp.uploadgallery.BuildConfig;
import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.api.GalleryImage;
import com.myapp.uploadgallery.manager.GalleryManager;
import com.myapp.uploadgallery.manager.SharedPreferencesHelper;
import com.myapp.uploadgallery.test.GalleryIdlingResource;
import com.myapp.uploadgallery.ui.gallery.GalleryAdapter;
import com.myapp.uploadgallery.ui.gallery.GalleryFragment;
import com.myapp.uploadgallery.ui.manipulator.ManipulatorFragment;
import com.myapp.uploadgallery.ui.util.DialogPool;
import com.myapp.uploadgallery.util.FileUtils;

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
        HasSupportFragmentInjector {
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

    @Inject
    GalleryAdapter galleryAdapter;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Inject
    DialogPool dialogPool;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    // The Idling Resource which will be null in production.
    @Nullable
    private GalleryIdlingResource mIdlingResource;

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
        galleryManager.subscribe(mIdlingResource);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        galleryManager.setView(null);
        galleryManager.unsubscribe();
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
                    Snackbar.make(fab, R.string.no_camera_permission, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.action_settings, settingsListener);
                }
                break;
            }
            case PERMISSION_REQUEST_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGallery();
                } else {
                    Snackbar.make(fab, R.string.no_gallery_permission, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.action_settings, settingsListener);
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
                sharedPreferencesHelper.setImageUri(
                        Uri.fromFile(FileUtils.getPictureFile(getApplicationContext())));
                showManipulator();
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                final Uri imageUri = data.getData();
                sharedPreferencesHelper.setImageUri(imageUri);
                showManipulator();
            }
        }
    }

    @OnClick(R.id.fab)
    public void onClick() {
        showProgress(false);
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

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void startCamera() {
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
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Snackbar.make(fab, R.string.no_camera_resolved, Snackbar.LENGTH_INDEFINITE);
            }
        }
    }

    private void startGallery() {
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

    private void showManipulator() {
        showProgress(false);
        showFab(false);

        ManipulatorFragment newFragment = new ManipulatorFragment();
        android.support.v4.app.FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.flCropFragment, newFragment, MANIPULATOR);
        transaction.addToBackStack(MANIPULATOR);
        transaction.commit();

        newFragment.setManipulatorListener(mIdlingResource, galleryManager);
    }

    private void showStubText() {
        final Fragment galleryFragment = findGalleryFragment();
        if (galleryFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(galleryFragment)
                    .commit();
        }
        emptyText.setVisibility(View.VISIBLE);
    }

    private void showNetworkAlert(final Throwable throwable) {
        dialogPool.showDialog(NETWORK, this, R.string.dialog_network_title,
                R.string.dialog_network_message, android.R.string.ok, null, 0, null);
    }

    private void showUploadAlert(final Throwable throwable) {
        dialogPool.showDialog(MANIPULATOR, this, R.string.dialog_manipulation_title,
                R.string.dialog_manipulation_message, android.R.string.ok, null, 0, null);
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
            showGalleryFragment();
            galleryAdapter.setImages(imageList);
        }
    }

    @Override
    public void onFetchImagesError(final Throwable throwable) {
        showProgress(false);
        showStubText();
        showNetworkAlert(throwable);
    }

    @Override
    public void onUploadImageError(final Throwable throwable) {
        onManipulatorClosed();
        showUploadAlert(throwable);
    }

    @Override
    public void onUploadImageCompleted(final GalleryImage image) {
        onManipulatorClosed();
        galleryAdapter.addImage(image);
        showGalleryFragment();
    }

    private GalleryFragment showGalleryFragment() {
        final Fragment fragmentByTag = findGalleryFragment();
        GalleryFragment fragment;
        if (fragmentByTag == null) {
            fragment = new GalleryFragment();
            android.support.v4.app.FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.flFragment, fragment, GALLERY);
            transaction.commit();
            fragment.setAdapter(galleryAdapter);
        } else {
            fragment = (GalleryFragment) fragmentByTag;
        }
        return fragment;
    }

    private Fragment findGalleryFragment() {
        return getSupportFragmentManager().findFragmentByTag(GALLERY);
    }

    @Override
    public void onManipulatorClosed() {
        getSupportFragmentManager().popBackStack();
        showProgress(false);
        showFab(true);
    }
    @Override
    public void onUploadImageStarted() {
        showProgress(true);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    /**
     * Only called from test, creates and returns a new {@link GalleryIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new GalleryIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showFab(true);
    }
}
