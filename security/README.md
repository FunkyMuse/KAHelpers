# Security

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

implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:security:$utilsVersion"

}
```

3. Simple usage
```kotlin
val testFile = File(filesDir, "testfile.txt")
encryptFileSafely(testFile, fileContent = "JETPACK SECURITY".toByteArray())
 val file = getEncryptedFile(testFile)
debug("TEXT DECRYPTED ${file.readText()}")
debug("TEXT ENCRYPTED ${testFile.readText()}")
```
