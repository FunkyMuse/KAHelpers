

# KAHelpers

### Android extensions and helper classes for easier Kotlin development

[![](https://jitpack.io/v/FunkyMuse/KAHelpers.svg)](https://jitpack.io/#FunkyMuse/KAHelpers)
![API](https://img.shields.io/badge/Min%20API-23-green)
![API](https://img.shields.io/badge/Compiled%20API-33-green)

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

2. Add the dependency in the application build.gradle using

### Groovy
```gradle
dependencies {

    def utilsVersion = "2.2.10" // or check the latest from jitpack
    
    //biometrics
    implementation "com.github.FunkyMuse.KAHelpers:biometrics:$utilsVersion"
    
    //coroutines
    implementation "com.github.FunkyMuse.KAHelpers:coroutines:$utilsVersion"
    
    //customviews
    implementation "com.github.FunkyMuse.KAHelpers:customviews:$utilsVersion"
    
    //data structures and algorithms
    implementation "com.github.FunkyMuse.KAHelpers:dataStructuresAndAlgorithms:$utilsVersion"
    
    //glide
    implementation "com.github.FunkyMuse.KAHelpers:glide:$utilsVersion"
    
    //gson
    implementation "com.github.FunkyMuse.KAHelpers:gson:$utilsVersion"
    
    //Joda Date Time
    implementation "com.github.FunkyMuse.KAHelpers:jodaDateTime:$utilsVersion"
    
    //the most basic extensions that rely on the basic Android APIs such as context, content providers etc...
    implementation "com.github.FunkyMuse.KAHelpers:kotlinextensions:$utilsVersion"
    
    //moshi
    implementation "com.github.FunkyMuse.KAHelpers:moshi:$utilsVersion"
    
    //recyclerview
    implementation "com.github.FunkyMuse.KAHelpers:recyclerview:$utilsVersion"
    
    //reflection
    implementation "com.github.FunkyMuse.KAHelpers:reflection:$utilsVersion"
    
    //retrofit
    implementation "com.github.FunkyMuse.KAHelpers:retrofit:$utilsVersion"
    
    //RxJava3
    implementation "com.github.FunkyMuse.KAHelpers:rx:$utilsVersion"
    
    //security
    implementation "com.github.FunkyMuse.KAHelpers:security:$utilsVersion"
    
    //viewbinding
    implementation "com.github.FunkyMuse.KAHelpers:viewbinding:$utilsVersion"
    
  }
```
### Version catalog
```toml
[versions]
kaHelpers = <version>

[libraries]
kahelpers-toaster = { module = "com.github.FunkyMuse.KAHelpers:toaster", version.ref = "kaHelpers" }
kahelpers-internetDetector = { module = "com.github.FunkyMuse.KAHelpers:internetDetector", version.ref = "kaHelpers" }
kahelpers-context = { module = "com.github.FunkyMuse.KAHelpers:context", version.ref = "kaHelpers" }
kahelpers-kotlinextensions = { module = "com.github.FunkyMuse.KAHelpers:kotlinextensions", version.ref = "kaHelpers" }
kahelpers-retrofit = { module = "com.github.FunkyMuse.KAHelpers:retrofit", version.ref = "kaHelpers" }
...

[bundles]
kahelpers = [
    "kahelpers-toaster",
    "kahelpers-internetDetector",
    "kahelpers-context",
    "kahelpers-kotlinextensions",
    "kahelpers-retrofit",
    ...
]
```

3. To not run into any issues in your application build.gradle add

```gradle
   compileOptions {
        sourceCompatibility = 17
        targetCompatibility = 17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    
```


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)
