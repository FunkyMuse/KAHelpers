

# Set Of Useful Kotlin Extensions and Helpers

### Android extensions and helper classes for easier Kotlin development

[![](https://jitpack.io/v/CraZyLegenD/Set-Of-Useful-Kotlin-Extensions-and-Helpers.svg)](https://jitpack.io/#CraZyLegenD/Set-Of-Useful-Kotlin-Extensions-and-Helpers)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.0-blue.svg)](https://kotlinlang.org) [![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/guide/) [![sad](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)]( https://twitter.com/intent/tweet?url=https%3A%2F%2Ftwitter.com%2Fintent%2Ftweet%3Fhttps%3A%2F%2Fgithub.com%2FCraZyLegenD%2FSet-Of-Useful-Kotlin-Extensions-and-Helpers&text=Kotlin%20Extensions%20and%20Class%20Helpers)
![API](https://img.shields.io/badge/Min%20API-21-green)
![API](https://img.shields.io/badge/Compiled%20API-30-green)

## Usage
1. Add JitPack to your project build.gradle

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
   }
}
```

2. Add the dependency in the application build.gradle

```gradle
dependencies {

    def utilsVersion = "2.0.8" // or check the latest from jitpack
    
    //biometrics
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:biometrics:$utilsVersion"
    
    //coroutines
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:coroutines:$utilsVersion"
    
    //customviews
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:customviews:$utilsVersion"
    
    //data structures and algorithms
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:dataStructuresAndAlgorithms:$utilsVersion"
    
    //database handlers
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:database:$utilsVersion"
    
    //glide
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:glide:$utilsVersion"
    
    //gson
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:gson:$utilsVersion"
    
    //Joda Date Time
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:jodaDateTime:$utilsVersion"
    
    //the most basic extensions that rely on the basic Android APIs such as context, content providers etc...
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:kotlinextensions:$utilsVersion"
    
    //moshi
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:moshi:$utilsVersion"
    
    //navigation component
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:navigation:$utilsVersion"
    
    //recyclerview
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:recyclerview:$utilsVersion"
    
    //reflection
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:reflection:$utilsVersion"
    
    //retrofit
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:retrofit:$utilsVersion"
    
    //RxJava3
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:rx:$utilsVersion"
    
    //RxBindings 3
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:rxbindings:$utilsVersion"
    
    //security
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:security:$utilsVersion"
    
    //viewbinding
    implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:viewbinding:$utilsVersion"
    
  }
```

3. To not run into any issues in your application build.gradle add

```gradle
   compileOptions {
        sourceCompatibility = 1.8
        targetCompatibility = 1.8
	
	//if you're using the desugaring library
	coreLibraryDesugaringEnabled true

    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
        exclude 'META-INF/LICENSE'
    }
    
    buildFeatures {
        viewBinding = true
    }

    androidExtensions {
        experimental = true
    }
```
4. Additionally you can include
```gradle
    kapt {	
	correctErrorTypes = true
        useBuildCache = true
    }
    
    buildTypes {
        debug {
        // useful if you're using crashlytics
	/**In your manifest file
	 <meta-data
            android:name="firebase_crashlytics_collection_enabled"
            android:value="${crashlytics}" />
	*/
          firebaseCrashlytics {
                mappingFileUploadEnabled false
          }
	  
	  FirebasePerformance {
          instrumentationEnabled false
	  }
	  
          manifestPlaceholders = [crashlytics: "false"]
        // end of crashlytics region
            
          crunchPngs false
        }
      }
    
     defaultConfig {
     	 vectorDrawables.useSupportLibrary = true
    }
```  
5. Inside gradle.properties

```gradle
org.gradle.parallel=true
org.gradle.caching=true
room.incremental = true // if you're using room
kapt.use.worker.api = true
org.gradle.unsafe.watch-fs=true
org.gradle.configureondemand=true
```
Informational/optinal
1. Proguard configs 
```gradle
-keepattributes SourceFile,LineNumberTable  
-keep public class * extends java.lang.Exception  
-keep class com.google.firebase.crashlytics.** { *; }  
-dontwarn com.google.firebase.crashlytics.**
-dontwarn org.jetbrains.annotations.**
#these are if you're using crashlytics and don't want to receive obfuscated crashes

```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)
