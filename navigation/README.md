# Navigation component for multiple stack Fragment management

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

implementation "com.github.FunkyMuse.KAHelpers:navigation:$utilsVersion"


}
```

3. [Advanced sample](https://github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample) usage
```kotlin
private var currentNavController: LiveData<NavController>? = null
.
.
.

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

}
.
.
.

/**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar(selectedItemID: Int) {

        val navGraphIds = listOf(R.navigation.home, R.navigation.favorites, R.navigation.settings)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = binding.bottomNavigation.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.navHostContainer,
                intent = intent,
                enter = R.anim.nav_default_enter_anim,
                exit = R.anim.nav_default_exit_anim,
                popEnter = R.anim.nav_default_pop_enter_anim,
                popExit = R.anim.nav_default_pop_exit_anim
        )
       
        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this) { navController ->
            setupActionBarWithNavController(navController)
        }
        currentNavController = controller

    }
.
.
.
override fun onSupportNavigateUp() = currentNavController?.value?.navigateUp() ?: false
.
.
.
override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
}

```
