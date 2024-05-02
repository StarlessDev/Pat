# Pat

Pat is a streamlined Java library built on top of Lettuce, providing an effortless way to integrate Redis functionality
into your projects. With Pat, you can easily incorporate Redis listeners and efficiently send packets without the hassle
of complex setup and implementation.

### Getting Started

To begin using Pat in your projects, simply refer to the PatClient interface to familiarize yourself with the available
methods and their usage.

### Example

Usage of Pat for sending and receiving messages:

```java
public class PatExample {

    public static void main(String[] args) {
        // Create a PatClient instance
        final PatClient pat = PatBuilder.create("redis://localhost:6379") // Create a PatBuilder instance
            .withClientOptions(ClientOptions.builder().build()) // Set the client options (optional, default options are used if not set)
            .withCompression(CompressionCodec.CompressionType.DEFLATE) // Set the compression type (optional, no compression is used if not set)
            .build(); // Build the PatClient instance
        // Connect to the Redis server
        pat.connect();

        // Register the class to listen for events
        pat.register(this);

        // Send a message to the "test" channel
        pat.send("test", "Test message.");
    }

    @PatSubscribe("test") // Subscribe to the "test" channel
    public void onPat(final PatEvent event) { // Listen for PatEvents
        // Get the channel name
        final String channel = event.channel();

        // Get the message bytes
        final byte[] bytes = event.message();

        // Get the message as a string
        final String message = event.messageString();
    }
}
```
