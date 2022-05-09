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
Import Ouinet in your activity and create a private variable to hold the client:

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

## Adjust Build Configuration

Create a `local.properties` file in the root of this project and set the
following value before building the app:
```groovy
CACHE_PUB_KEY="YOUR OUINET CACHE PUB KEY"
```

Those values will be loaded by Gradle during the build process and can be referenced from Java via `BuildConfig`:

```java


public class MainActivity extends AppCompatActivity {
    ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...

        Config config = new Config.ConfigBuilder(this)
                .setCacheType("bep5-http")
                .setCacheHttpPubKey(BuildConfig.CACHE_PUB_KEY) // This value comes from Gradle local.properties
                .build();
        
        ...
    }
}
```

