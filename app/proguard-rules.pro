# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Prevent R8 from leaving Data object members always null
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
  }
  -keep class com.wang.avi.** { *; }
  -keep class com.wang.avi.indicators.** { *; }


-dontwarn com.pollfish.**
-keep class com.pollfish.** { *; }

-keepattributes Signature
    -keepattributes *Annotation*
    -keep class com.mintegral.** {*; }
    -keep interface com.mintegral.** {*; }
    -keep interface androidx.** { *; }
    -keep class androidx.** { *; }
    -keep public class * extends androidx.** { *; }
    -dontwarn com.mintegral.**
    -keep class **.R$* { public static final int mintegral*; }
    -keep class com.alphab.** {*; }
    -keep interface com.alphab.** {*; }
