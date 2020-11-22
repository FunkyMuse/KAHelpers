# Reflection

[![](https://jitpack.io/v/FunkyMuse/KAHelpers.svg)](https://jitpack.io/#FunkyMuse/KAHelpers)


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

implementation "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
implementation "com.github.FunkyMuse.KAHelpers:reflection:$utilsVersion"

}
```
