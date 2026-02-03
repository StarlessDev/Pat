# Pat

Pat is a streamlined, high-performance Java library built on top of [Lettuce](https://lettuce.io/), designed to provide an effortless way to integrate Redis Pub/Sub functionality into your projects. With Pat, you can incorporate Redis listeners and efficiently send messages without the hassle of complex setup and implementation.

## Features

- **Fluent API**: Use `PatBuilder` for a clean and readable configuration.
- **Easy Pub/Sub**: Simple methods for synchronous and asynchronous message publishing.
- **Annotation-based Subscriptions**: Register listeners using the `@PatSubscribe` annotation.
- **Functional Subscriptions**: Dynamically subscribe to channels using `Consumer<PatEvent>`.
- **Built-in Parsing**: Integrated support for **Gson** and **Protobuf** to easily deserialize messages.
- **Compression Support**: Built-in support for compression (e.g., DEFLATE).
- **Lightweight**: Minimal overhead over Lettuce.

## Installation

### Gradle (Kotlin DSL)
```kotlin
dependencies {
    implementation("com.fabiodm.pat:pat:1.1.3")
}
```

### Maven
```xml
<dependency>
    <groupId>com.fabiodm.pat</groupId>
    <artifactId>pat</artifactId>
    <version>1.1.3</version>
</dependency>
```

## Getting Started

### 1. Create and Connect the Client

Use the `PatBuilder` to configure your Redis connection.

```java
import com.fabiodm.pat.PatBuilder;
import com.fabiodm.pat.api.PatClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.CompressionCodec;

PatClient pat = PatBuilder.create(RedisURI.create("redis://localhost:6379"))
        .withCompression(CompressionCodec.CompressionType.DEFLATE) // Optional
        .build();

// Connect to the Redis server
pat.connect();
```

### 2. Publishing Messages

Pat supports both synchronous and asynchronous message publishing.

```java
// Synchronous publishing
pat.send("my-channel", "Hello, Redis!");
pat.send("my-channel", new byte[]{0x01, 0x02, 0x03});

// Asynchronous publishing
pat.sendAsync("my-channel", "I'm fast!");
```

### 3. Subscribing to Channels

There are two ways to receive messages:

#### A. Using Annotations (Recommended)
Annotate your methods with `@PatSubscribe` and register the class instance.

```java
public class MyListener {

    @PatSubscribe("my-channel")
    public void onMessage(PatEvent event) {
        System.out.println("Received from " + event.channel() + ": " + event.messageAsString());
    }
}

// Registration
MyListener listener = new MyListener();
pat.register(listener);
```

#### B. Functional Subscription
You can also subscribe dynamically using a `Consumer`.

```java
pat.subscribeToChannel(this, "other-channel", event -> {
    System.out.println("Dynamic message: " + event.messageAsString());
});
```

### 4. Message Parsing

Pat includes built-in parsers for common formats like JSON (via Gson) and Protobuf.

```java
@PatSubscribe("data-channel")
public void onEvent(PatEvent event) {
    // Parse as JSON using Gson
    event.asGsonParser().asObject().ifPresent(jsonElement -> System.out.println("JSON: " + jsonElement));

    // Parse as Protobuf
    event.asProtobufParser(MyProtoMessage.parser()).asObject().ifPresent(proto -> System.out.println("Proto: " + proto.getName()));
}
```

## Advanced Configuration

You can customize the underlying Lettuce `ClientOptions`:

```java
ClientOptions options = ClientOptions.builder()
        .autoReconnect(true)
        .build();

PatClient pat = PatBuilder.create(RedisURI.create("redis://localhost:6379"))
        .withClientOptions(options)
        .build();
```

## Lifecycle Management

To gracefully shut down the client and release resources:

```java
pat.disconnect();
pat.shutdown();
```

## Requirements
- Java 25 or higher.
- Redis server.
