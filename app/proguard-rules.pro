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

# OPT (needed now that R8 is on): Gson 2.10 ships no R8 rules and maps JSON keys onto fields
# reflectively, so keep the API model's fields and their @SerializedName names.
-keepattributes Signature, RuntimeVisibleAnnotations
-keep class com.google.gson.annotations.SerializedName
-keep class com.example.speertechassessment.data.GitHubUser { <fields>; }
