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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.myapp.uploadgallery.R;
import com.myapp.uploadgallery.model.UpImage;
import com.myapp.uploadgallery.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainViewable {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private MainPresenter mainPresenter;
    private GalleryAdapter adapter;

    public static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final int REQUEST_IMAGE_GALLERY = 102;
    public static final int PERMISSION_REQUEST_GALLERY = 103;
    public static final int PERMISSION_REQUEST_CAMERA = 104;

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
//        AndroidInjection.inject(this);
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
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface,
                                                final int i) {
                                startGallery();
                            }
                        });
        if (mainPresenter.hasCamera(this)) {
            builder.setPositiveButton(R.string.dialog_upload_camera,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface,
                                            final int i) {
                            startCamera();
                        }
                    });
        }
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(final MainPresenter presenter) {
        this.mainPresenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mainPresenter.checkImages()
                .subscribeOn(Schedulers.io())
                .doOnTerminate(() -> {
                    Log.d("IMAGE", "TERMINATED!");
                    if (adapter.getItemCount() > 0) {
                        // TODO: 9/18/17  add fragment
                    }
                })
                .doOnNext((UpImage image) -> adapter.add(image))
                .subscribe(UpImage -> Log.d("Image", String.valueOf("image")));
    }

    @Override
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

    @Override
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
                return;
            }
            case PERMISSION_REQUEST_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGallery();
                } else {
                    showNoGallerySnack();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void showNoCameraSnack() {
        showSnack(R.string.no_camera_permission);
    }
    public void showNoGallerySnack() {
        showSnack(R.string.no_gallery_permission);
    }

    public void showSnack(int resId) {
        Snackbar.make(fab, R.string.no_gallery_permission, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_settings, settingsListener).show();
    }
}
