# Retrofit

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

implementation "com.github.FunkyMuse.KAHelpers:retrofit:$utilsVersion"

}
```

3. Simple usage

```kotlin
private val retrofit by lazy {
   RetrofitClient.moshiInstanceCoroutines(application, TestApi.url, enableDebuggingInterceptor = BuildConfig.DEBUG).create<TestApi>()
}
```
4. Custom usage

Your retrofit
```kotlin
private val retrofit by lazy {
        RetrofitClient.customInstance(application, TestApi.API, false, builderCallback = {
            addCallAdapterFactory(ApiResultAdapterFactory())
            addConverterFactory(MoshiConverterFactory.create())
            this
        }).create<TestApi>()
    }
```

Your API
```kotlin
@GET("posts")
suspend fun getPostsAdapter(): ApiResult<List<TestModel>>
```

Your view model
```kotlin
private val postsData: MutableStateFlow<ApiResult<List<TestModel>>> = MutableStateFlow(ApiResult.EmptyData)
val posts: MutableStateFlow<ApiResult<List<TestModel>>> = postsData
```
```kotlin
 fun getposts() {
        postsData.value = ApiResult.Loading
        viewModelScope.launch(ioDispatcher + SupervisorJob()) {
            delay(3000) //simulating network delay
            postsData.value = retrofit.getPostsAdapter()
        }
    }
```