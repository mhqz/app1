# Ouinet's test app

## Prepare your app for importing Ouinet

Add Ouinet lib and Relinker to your dependencies list in **app/build.gradle**:

```groovy
dependencies {
    ...
    implementation 'ie.equalit.ouinet:ouinet:1.5.0'
    implementation 'com.getkeepsafe.relinker:relinker:1.4.4'
}
```
Import Ouinet in your Android activity and create a private variable to hold the client:

```java
import ie.equalit.ouinet.Ouinet;

public class MainActivity extends AppCompatActivity {
    private Ouinet ouinet;
    
    ...
}
```

Import config and setup the Ouinet client:

```java
import ie.equalit.ouinet.Ouinet;

public class MainActivity extends AppCompatActivity {
    private Ouinet ouinet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        
        Config config = new Config.ConfigBuilder(this)
                        .setCacheType("bep5-http")
                        .build();

        ouinet = new Ouinet(this, config);
        ouinet.start();
        
        ...
    }
}

```

## Pass config values to Ouinet during the build process

You can have Ouinet keys and passwords added to the
client during the building process by Gradle.

You just need to create a `local.properties` file in the root of this project
and set the values as follows before building the app:
```groovy
CACHE_PUB_KEY="YOUR OUINET CACHE PUB KEY"
```

Those values should be loaded by Gradle during the build process in **app/build.gradle**:
```groovy
...

Properties localProperties = new Properties()
localProperties.load(rootProject.file('local.properties').newDataInputStream())

android {
    compileSdk 32

    defaultConfig {
        ...
        buildConfigField "String", "CACHE_PUB_KEY", localProperties['CACHE_PUB_KEY']
    }
    ...
}
```

and can be referenced after that from Java via `BuildConfig`:

```java
public class MainActivity extends AppCompatActivity {
    ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...

        Config config = new Config.ConfigBuilder(this)
                .setCacheType("bep5-http")
                .setCacheHttpPubKey(BuildConfig.CACHE_PUB_KEY) //From local.properties
                .build();
        
        ...
    }
}
```

