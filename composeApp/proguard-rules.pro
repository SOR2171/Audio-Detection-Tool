# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Code\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep rules here:

-dontwarn io.github.oshai.kotlinlogging.**
-dontwarn org.apache.commons.csv.CSVPrinter
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings
-dontwarn space.kodio.core.**
-dontwarn java.lang.foreign.**
-dontwarn ch.qos.logback.**
-dontwarn kotlinx.coroutines.slf4j.**
-dontwarn org.slf4j.impl.**

-dontpreverify
-dontoptimize
