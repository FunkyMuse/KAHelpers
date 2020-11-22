# Database handlers

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

implementation "com.github.FunkyMuse.KAHelpers:database:$utilsVersion"

}
```

3. Simple usage

```kotlin
//inside your ViewModel
 private val favoritesData: MutableLiveData<DBResult<List<MyModel>>> = MutableLiveData()
 val favorites: LiveData<DBResult<List<MyModel>>> = favoritesData
 
 private fun getAll() {
        makeDBCallFlow(favoritesData) {
            favoritesRepo.getAll()
        }
    }
```

```kotlin
//your getAll method 
@Query("select * from myFavorites") fun getAllFavorites(): Flow<List<MyModel>>

```
