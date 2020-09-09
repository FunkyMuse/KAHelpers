# Retrofit

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

implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:retrofit:$utilsVersion"

}

3. Simple usage

```kotlin
private val retrofit by lazy {
   RetrofitClient.moshiInstanceCoroutines(application, TestApi.url, enableDebuggingInterceptor = BuildConfig.DEBUG).create<TestApi>()
}
```
