# Glide

[![](https://jitpack.io/v/CraZyLegenD/Set-Of-Useful-Kotlin-Extensions-and-Helpers.svg)](https://jitpack.io/#CraZyLegenD/Set-Of-Useful-Kotlin-Extensions-and-Helpers)


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

implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:glide:$utilsVersion"
kapt "com.github.bumptech.glide:compiler:$glide"

}
```

3. Simple usage

```kotlin
binding.headerImage.loadImgNoCache(myModel.imageURL) {
            RequestOptions().apply {
                centerCrop()
            }
        }
```
