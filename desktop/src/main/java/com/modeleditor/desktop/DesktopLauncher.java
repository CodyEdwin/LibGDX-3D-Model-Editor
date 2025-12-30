package com.modeleditor.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.modeleditor.core.ModelEditorApplication;

/**
 * Desktop launcher for the LibGDX 3D Model Editor.
 * Supports Windows, macOS, and Linux.
 */
public class DesktopLauncher {

    public static void main(String[] args) {
        // Create application configuration
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        // Set window title
        config.setTitle("LibGDX 3D Model Editor");

        // Set window size (default to 1280x800)
        config.setWindowedMode(1400, 900);

        // Set window icon if available
        // config.setWindowIcon("icon.png");

        // Enable vsync for smoother rendering
        config.useVsync(true);

        // Set FPS limit (0 for unlimited, but we use vsync)
        config.setForegroundFPS(60);

        // Enable resizing
        config.setResizable(true);

        // Create and start the application
        new Lwjgl3Application(new ModelEditorApplication(), config);
    }
}
