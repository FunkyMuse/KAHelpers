# Set Of Useful Kotlin Extensions and Helpers

### Kotlin extensions and Helper classes

[![](https://jitpack.io/v/CraZyLegenD/Set-Of-Useful-Kotlin-Extensions-and-Helpers.svg)](https://jitpack.io/#CraZyLegenD/Set-Of-Useful-Kotlin-Extensions-and-Helpers)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.3.20-blue.svg)](https://kotlinlang.org) [![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/guide/) [![sad](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)]( https://twitter.com/intent/tweet?url=https%3A%2F%2Ftwitter.com%2Fintent%2Ftweet%3Fhttps%3A%2F%2Fgithub.com%2FCraZyLegenD%2FSet-Of-Useful-Kotlin-Extensions-and-Helpers&text=Kotlin%20Extensions%20and%20Class%20Helpers)

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
    implementation 'com.github.CraZyLegenD:Set-Of-Useful-Kotlin-Extensions-and-Helpers:version'
  }
```

3. To not run into any issues in your application build.gradle add

```gradle
  compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
        exclude 'META-INF/LICENSE'
    }

    androidExtensions {
        experimental = true
    }
```


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
