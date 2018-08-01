# Blur4All

[![](https://jitpack.io/v/vinisauter/Blur4All.svg)](https://jitpack.io/#vinisauter/Blur4All)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)


# Integrating into your project
This library is available in [JitPack.io](https://jitpack.io/) repository.
To use it, make sure to add the below inside root build.gradle file

```
allprojects {
    repositories {
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}
```

and add the repository's url to the app's build.gradle file.

```
dependencies {
   implementation 'com.github.vinisauter:Blur4All:1.0'
    // Other dependencies your app might use
}
```

IMPLEMENTATION
----
#### BlurredImageView.
This library has different methods which you can use to maintain your image blur.
 - blurRadius - "blureness" [0 ... 25]
 - downScale - "downScale" [0 ... 1]
 
xml:
```xml
<vas.com.blur4all.BlurredImageView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:scaleType="centerCrop"
    android:src="@drawable/wallhaven_680095"
    app:blurRadius="20"
    app:downScale="0.9" />
```
java:
```
BlurredImageView blurredImageView = findViewById(R.id.blurredImageView);
blurredImageView.setBlurRadius(20);
blurredImageView.setScale(0.9);
```
#### Blur.
```
Bitmap blured = Blur.apply(getContext(), bitmap, blurRadius);
```
#### BluredActivity. 
TODO: 
#### BlurActionBarDrawerToggle. 
TODO:
#### BlurDialog. 
TODO: