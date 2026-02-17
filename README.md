# Pat

Pat is a streamlined, high-performance Java library built on top of [Lettuce](https://lettuce.io/), designed to provide an effortless way to integrate Redis Pub/Sub functionality into your projects. With Pat, you can incorporate Redis listeners and efficiently send messages without the hassle of complex setup and implementation.

## Features

- **Fluent API**: Use `PatBuilder` for clean and readable configuration
- **Easy Pub/Sub**: Simple methods for synchronous and asynchronous message publishing
- **Annotation-based Subscriptions**: Register listeners using the `@PatSubscribe` annotation
- **Functional Subscriptions**: Dynamically subscribe to channels using `Consumer<PatEvent>`
- **Built-in Parsing**: Integrated support for **Gson** and **Protobuf** to easily deserialize messages
- **Compression Support**: Built-in support for GZIP and DEFLATE compression
- **Client Reuse**: Support for both managed and external Redis clients
- **Lightweight**: Minimal overhead over Lettuce

## Installation

### Gradle (Kotlin DSL)
```kotlin
dependencies {
    implementation("com.fabiodm:pat:1.1.3")
}
```

### Maven
```xml
<dependency>
    <groupId>com.fabiodm</groupId>
    <artifactId>pat</artifactId>
    <version>1.1.3</version>
</dependency>
```

## Getting Started

### 1. Create and Connect the Client

#### Option A: Pat Manages the Redis Client

Use `PatBuilder` to let Pat create and manage the Redis connection:
```java
import com.fabiodm.pat.PatBuilder;
import com.fabiodm.pat.api.PatClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.CompressionCodec;

PatClient pat = PatBuilder.create(RedisURI.create("redis://localhost:6379"))
        .withCompression(CompressionCodec.CompressionType.GZIP) // Optional
        .build();

// Connect to the Redis server
pat.connect();
```

#### Option B: Reuse Existing Redis Client

If you already have a `RedisClient` instance, you can pass it to Pat:
```java
import io.lettuce.core.RedisClient;

RedisClient existingClient = RedisClient.create("redis://localhost:6379");

PatClient pat = PatBuilder.fromClient(existingClient)
        .withCompression(CompressionCodec.CompressionType.GZIP) // Optional
        .build();

pat.connect();

// Note: When using an external client, shutdown() will NOT close the RedisClient
```

### 2. Publishing Messages

Pat supports both synchronous and asynchronous message publishing:
```java
// Synchronous publishing
pat.send("my-channel", "Hello, Redis!");
pat.send("my-channel", new byte[]{0x01, 0x02, 0x03});

// Asynchronous publishing
pat.sendAsync("my-channel", "Async message");
```

### 3. Subscribing to Channels

There are two ways to receive messages:

#### A. Using Annotations (Recommended)

Annotate methods with `@PatSubscribe` and register the class instance:
```java
public class MyListener {

    @PatSubscribe("my-channel")
    public void onMessage(PatEvent event) {
        System.out.println("Received from " + event.channel() + ": " + event.messageAsString());
    }
    
    @PatSubscribe("other-channel")
    public void onOtherMessage(PatEvent event) {
        System.out.println("Another channel: " + event.messageAsString());
    }
}

// Register the listener
MyListener listener = new MyListener();
pat.register(listener);
```

#### B. Functional Subscription

Subscribe dynamically using a `Consumer`:
```java
pat.subscribeToChannel(this, "dynamic-channel", event -> {
    System.out.println("Dynamic message: " + event.messageAsString());
});
```

### 4. Message Parsing

Pat includes built-in parsers for common formats:

#### JSON Parsing (via Gson)
```java
@PatSubscribe("json-channel")
public void onJsonEvent(PatEvent event) {
    event.asGsonParser().asObject().ifPresent(jsonElement -> {
        System.out.println("JSON: " + jsonElement);
    });
}
```

#### Protobuf Parsing
```java
@PatSubscribe("proto-channel")
public void onProtoEvent(PatEvent event) {
    event.asProtobufParser(MyProtoMessage.parser()).asObject().ifPresent(proto -> {
        System.out.println("Proto name: " + proto.getName());
    });
}
```

#### Raw Message Access
```java
@PatSubscribe("raw-channel")
public void onRawEvent(PatEvent event) {
    String message = event.messageAsString();
    byte[] rawBytes = event.message();
    String channel = event.channel();
}
```

## Advanced Configuration

### Custom Client Options

Customize the underlying Lettuce `ClientOptions`:
```java
import io.lettuce.core.ClientOptions;
import io.lettuce.core.TimeoutOptions;
import java.time.Duration;

ClientOptions options = ClientOptions.builder()
        .autoReconnect(true)
        .pingBeforeActivateConnection(true)
        .timeoutOptions(TimeoutOptions.builder()
                .fixedTimeout(Duration.ofSeconds(5))
                .build())
        .build();

PatClient pat = PatBuilder.create(RedisURI.create("redis://localhost:6379"))
        .withClientOptions(options)
        .build();
```

**Note**: `withClientOptions()` cannot be used when passing an external `RedisClient` via `fromClient()`.

### Compression

Pat supports GZIP and DEFLATE compression for message payloads:
```java
PatClient pat = PatBuilder.create(RedisURI.create("redis://localhost:6379"))
        .withCompression(CompressionCodec.CompressionType.GZIP)
        .build();
```

## Lifecycle Management

### Disconnect

Close the Pub/Sub connection while keeping the Redis client alive:
```java
pat.disconnect();
```

### Shutdown

Fully shut down Pat and release all resources:
```java
pat.shutdown();
```

**Important**:
- If Pat created the Redis client (via `PatBuilder.create()`), `shutdown()` will close the Redis client
- If you passed an external Redis client (via `PatBuilder.fromClient()`), `shutdown()` will only close Pat's connection, leaving your Redis client open for you to manage

## Complete Example
```java
import com.fabiodm.pat.PatBuilder;
import com.fabiodm.pat.api.PatClient;
import com.fabiodm.pat.annotation.PatSubscribe;
import com.fabiodm.pat.event.PatEvent;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.CompressionCodec;

public class PatExample {

    static void main(String[] args) {
        // Create and connect
        PatClient pat = PatBuilder.create(RedisURI.create("redis://localhost:6379"))
                .withCompression(CompressionCodec.CompressionType.GZIP)
                .build();
        
        pat.connect();
        
        // Register listener
        pat.register(new MessageListener());
        
        // Send messages
        pat.send("notifications", "Hello from Pat!");
        pat.sendAsync("events", "Async event");
        
        // Cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            pat.disconnect();
            pat.shutdown();
        }));
    }
    
    static class MessageListener {
        @PatSubscribe("notifications")
        public void onNotification(PatEvent event) {
            System.out.println("Notification: " + event.messageAsString());
        }
        
        @PatSubscribe("events")
        public void onEvent(PatEvent event) {
            event.asGsonParser().asObject().ifPresent(json -> {
                System.out.println("Event data: " + json);
            });
        }
    }
}
```

## Requirements

- **Java**: 17 or higher
- **Redis (Lettuce)**: 5.0 or higher

## License

This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! If you'd like to contribute:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

All contributions are appreciated!

## Contributors

Thanks to these awesome contributors:
- [@StarlessDev](https://github.com/StarlessDev) – Library design, performance improvements and bug fixes

## Support

If you encounter any issues or have questions, please open an issue on the [GitHub repository](https://github.com/fabiodimauro/Pat/issues)