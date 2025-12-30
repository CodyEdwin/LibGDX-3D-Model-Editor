package com.modeleditor.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.modeleditor.core.ModelEditorApplication;

/**
 * Android launcher for the LibGDX 3D Model Editor.
 * Supports Android devices with OpenGL ES 2.0 or higher.
 */
public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Android application configuration
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        // Use accelerometer for camera control (optional)
        config.useAccelerometer = false;
        config.useCompass = false;

        // Enable OpenGL ES 2.0 or higher
        config.useGL30 = true;

        // Enable continuous rendering for smooth preview
        config.r = 8;
        config.g = 8;
        config.b = 8;
        config.a = 8;

        // Initialize and start the application
        initialize(new ModelEditorApplication(), config);
    }
}
