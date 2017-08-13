# Retrolambda
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

# Retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-dontwarn okio.**

# Dagger 2
-dontwarn com.google.errorprone.annotations.*

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Spots progress dialog
-keep class dmax.dialog.** {
    *;
}

# Butterknife
-keep public class * implements butterknife.Unbinder {
    public <init>(**, android.view.View);
}
-keep class butterknife.*

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

# Joda Time
-keep class org.joda.** { *; }

# Room
-dontwarn android.arch.util.paging.CountedDataSource
-dontwarn android.arch.persistence.room.paging.LimitOffsetDataSource