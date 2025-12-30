# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the Android SDK installation directory.

# Keep the model editor classes
-keep class com.modeleditor.** { *; }

# Keep Janino compiler classes
-keep class org.codehaus.janino.** { *; }
-keep class org.codehaus.commons.compiler.** { *; }

# Keep LibGDX classes
-keep class com.badlogic.gdx.** { *; }
-dontwarn com.badlogic.gdx.**

# Keep generated model classes at runtime
-keepclassmembers class * {
    public <init>(...);
}

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
