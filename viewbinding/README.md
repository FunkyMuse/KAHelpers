# View Binding

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

implementation "com.github.CraZyLegenD.Set-Of-Useful-Kotlin-Extensions-and-Helpers:viewbinding:$utilsVersion"

}
```

3. Activity usage
```kotlin
class MainAbstractActivity : AppCompatActivity() {
    private val activityMainBinding by viewBinding(ActivityMainBinding::inflate)
    
 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
}

}
```
4. Fragment usage
```kotlin
class DetailedFragment : Fragment(R.layout.fragment_details) {
    private val binding by viewBinding(FragmentDetailsBinding::bind)
   
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //do your things here
    }
    
}
```
