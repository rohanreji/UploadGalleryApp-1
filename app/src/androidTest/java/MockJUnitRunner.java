import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import com.myapp.uploadgallery.MockGalleryApp;

public class MockJUnitRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        return newApplication(MockGalleryApp.class, context);
    }
}