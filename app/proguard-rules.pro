-keep public class com.github.mikephil.charting.animation.ChartAnimator {
    public protected *;
}

-keep public class androidx.recyclerview.widget.AsyncListDiffer {
     private *;
}

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Change here com.yourcompany.yourpackage
-keep,includedescriptorclasses class com.maxpoliakov.skillapp.data.**$$serializer { *; } # <-- change package name to your app's
-keepclassmembers class com.maxpoliakov.skillapp.data.** { # <-- change package name to your app's
    *** Companion;
}
-keepclasseswithmembers class com.maxpoliakov.skillapp.data.** { # <-- change package name to your app's
    kotlinx.serialization.KSerializer serializer(...);
}

# ProGuard Configuration file
#
# See http://proguard.sourceforge.net/index.html#manual/usage.html

# Needed to keep generic types and @Key annotations accessed via reflection

-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}

# Needed by google-http-client-android when linking against an older platform version

-dontwarn com.google.api.client.extensions.android.**

# Needed by google-api-client-android when linking against an older platform version

-dontwarn com.google.api.client.googleapis.extensions.android.**

# Needed by google-play-services when linking against an older platform version

-dontwarn com.google.android.gms.**
