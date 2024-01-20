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

-obfuscationdictionary dictionary.txt
-classobfuscationdictionary dictionary.txt
-packageobfuscationdictionary dictionary.txt

# https://issuetracker.google.com/issues/150189783
# kotlin bean r8.fullmode
-if class *

-keepclasseswithmembers class <1> {

<init>(...);

@com.google.gson.annotations.SerializedName <fields>;

}

# okhttp/retrofit/okio
-dontwarn org.codehaus.mojo.animal_sniffer.*
# 泛型反射
-keepattributes Signature
# Kotlin 反射
-keep class kotlin.Metadata{ *; }

# gson
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, java.lang.Boolean);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn org.bouncycastle.jsse.*
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.javax.net.ssl.*
-dontwarn org.openjsse.net.ssl.*
-dontwarn org.bouncycastle.jsse.provider.*

# retroift bug
# https://github.com/square/retrofit/pull/3596
-keep,allowobfuscation,allowshrinking class io.reactivex.rxjava3.core.Observable

# https://bugly.qq.com/docs/user-guide/instruction-manual-android/?v=1.0.0
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}