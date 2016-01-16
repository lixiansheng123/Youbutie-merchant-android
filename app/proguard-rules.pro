# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#bmob-------------------------------------------------------
-ignorewarnings
#cn.bmob.android:bmob-sdk:3.4.5
# 这里根据具体的SDK版本修改
#-libraryjars libs/bmob_v3.4.5beta.jar
-keepattributes Signature
-keep class cn.bmob.v3.** {*;}
# 保证继承自BmobObject、BmobUser类的JavaBean不被混淆
-keep class com.yuedong.youbutie_merchant_android.mouble.bmob.bean.**{*;}

# 如果你使用了okhttp、okio的包，请添加以下混淆代码
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep interface com.squareup.okhttp.** { *; }
-dontwarn okio.**

#umeng反馈----------------------------------------
-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}
-keep public class com.yuedong.youbutie_merchant_android.R$*{
    public static final int *;
}

-keep public class com.umeng.fb.** { *;}
-keep public class com.umeng.fb.ui.ThreadView { }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#umeng自动更新
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
#如果您使用了双向反馈功能
-keep public class com.umeng.fb.ui.ThreadView {
}
# 添加第三方jar包
#-libraryjars libs/umeng-sdk.jar
# 以下类过滤不混淆
-keep public class * extends com.umeng.**
# 以下包不进行过滤
-keep class com.umeng.** { *; }

#百度推送
#-libraryjars libs/pushservice-4.6.0.66.jar
-dontwarn com.baidu.**
-keep class com.baidu.**{*; }


