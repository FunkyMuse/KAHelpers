# Gson

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

implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:gson:$utilsVersion"


}
```

3. Add an object to your prefs (discouraged)
```kotlin
requireContext().defaultPrefs.putObject("myObj", MyObj())
```

4. Convert JSON string to object
```kotlin
val myObj : MyObj = gson.fromJson<MyObj>(someExternalJson)
```

5. Convert object to JSON
```kotlin
val json :String = myObjectInstance.toJson()
```
