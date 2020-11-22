# Security

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

implementation "com.github.FunkyMuse.KAHelpers:security:$utilsVersion"

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
