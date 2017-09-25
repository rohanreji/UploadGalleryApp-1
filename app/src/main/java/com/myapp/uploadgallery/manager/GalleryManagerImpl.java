package com.myapp.uploadgallery.manager;

import com.myapp.uploadgallery.api.GalleryEndpoint;
import com.myapp.uploadgallery.api.ImageResponse;
import com.myapp.uploadgallery.ui.Viewable;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class GalleryManagerImpl implements GalleryManager {
    private final UserId userId;
    private final GalleryEndpoint endpoint;
    private Viewable view;
    private Scheduler backgroundScheduler;
    private Scheduler mainScheduler;
    private CompositeDisposable subscriptions;

    public GalleryManagerImpl(final UserId userId, final GalleryEndpoint endpoint,
                              Scheduler background, Scheduler main) {
        this.userId = userId;
        this.endpoint = endpoint;
        this.backgroundScheduler = background;
        this.mainScheduler = main;
        subscriptions = new CompositeDisposable();
    }

    @Override
    public void setView(Viewable view) {
        this.view = view;
    }

//    @Override
//    public Single updateImages() {
//        return endpoint.getImagesForUser(userId.get())
//                .subscribeOn(backgroundScheduler)
//                .observeOn(mainScheduler)
//                .doOnSubscribe((Disposable disposable) -> view.showProgress(true))
//                .doOnSuccess((ImageResponse imageResponse)
//                        -> images.addAll(imageResponse.getImages()))
//                .doOnError((Throwable throwable) -> view.showNetworkAlert(throwable))
//                .doOnEvent((ImageResponse ir, Throwable t) -> imagesUpdated());
//    }

//    private void imagesUpdated() {
//        if (images.size() == 0) {
//            view.showProgress(false);
//            view.showStubText();
//        } else {
//            view.showGallery(images);
//        }
//    }

//    private Single<File> saveBitmap(final File file, final Bitmap bitmap) {
//        return Single.defer(() -> {
//            OutputStream out = null;
//            try {
//                out = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (out != null) {
//                    try {
//                        out.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return Single.just(file);
//        });
//    }

//    private Single<GalleryImage> uploadCachedPicture(final File pictureFile) {
//        RequestBody body =
//                RequestBody.create(MediaType.parse("image/*"), pictureFile);
//        MultipartBody.Part part =
//                MultipartBody.Part.createFormData("image", pictureFile.getName(),
//                        body);
//
//        return endpoint.postImageForUser(userId.get(), part)
//                .subscribeOn(backgroundScheduler)
//                .observeOn(mainScheduler)
//                .doOnError((Throwable t) -> view.showUploadAlert(t))
//                .doOnSuccess((GalleryImage image) -> {
//                    images.add(image);
//                    imagesUpdated();
//                })
//                .doOnEvent((GalleryImage ir, Throwable t) -> imagesUpdated());
//    }

//    @Override
//    public GalleryViewable.GalleryListener getGalleryListener() {
//        return this;
//    }
//
//    @Override
//    public void onViewCreated() {
//        imagesUpdated();
//        view.showProgress(false);
//    }
//
//    @Override
//    public ManipulatorViewable.ManipulatorListener getManipulatorListener() {
//        return this;
//    }
//
//    @Override
//    public void onManipulatorCropped(final File pictureFile, final Bitmap bitmap) {
//        saveBitmap(pictureFile, bitmap)
//                .flatMap((File file) -> uploadCachedPicture(file))
//                .subscribeOn(backgroundScheduler)
//                .observeOn(mainScheduler)
//                .doOnError((Throwable t) -> view.showUploadAlert(t))
//                .doFinally(() -> view.showProgress(false))
//                .subscribe((GalleryImage galleryImage) -> view.showProgress(false),
//                        (Throwable throwable) -> view.showUploadAlert(throwable));
//    }

    @Override
    public void loadImages() {
        view.onFetchImagesStarted();
        subscriptions.clear();

        Disposable disposable = endpoint.getImagesForUser(userId.get())
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .subscribe((ImageResponse imageResponse) -> {
                    view.onFetchImagesCompleted(imageResponse.getImages());
                }, (Throwable throwable) -> {
                    view.onFetchImagesError(throwable);
                });
        subscriptions.add(disposable);
    }

    @Override
    public void uploadImage() {

    }

    @Override
    public void subscribe() {
        loadImages();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }
    @Override
    public void onDestroy() {
        this.view = null;
        // TODO: 9/25/17 destroy other dependencies
    }
}
