package client.utils;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class GenericStompFrameHandler<T> implements StompFrameHandler {

    private final Class<T> payloadType;
    private final Consumer<T> listener;
    /**
     * Constructor
     *
     * @param payloadType The Class object representing the type (T) the payload should be
     * deserialized into by the STOMP message converter.
     * @param listener The consumer function to be executed when a message frame arrives,
     * accepting the deserialized payload (T).
     */
    public GenericStompFrameHandler(Class<T> payloadType, Consumer<T> listener) {
        this.payloadType = payloadType;
        this.listener = listener;
    }

    /**
     * Returns the expected type of the message payload for deserialization.
     * This method is called by the STOMP client to determine what Java type
     * the incoming JSON payload should be converted into.
     *
     * @param headers The headers of the incoming STOMP frame (ignored in this implementation).
     * @return The target {@link Type} for deserialization, which is the class T.
     */
    @Override
    public Type getPayloadType(StompHeaders headers) {
        // Tell STOMP what type to deserialize into
        return payloadType;
    }

    /**
     * Handles the incoming STOMP message frame after the payload has been successfully
     * deserialized.
     * This implementation casts the payload to the expected type T and passes it to
     * the provided listener function for processing.
     *
     * @param headers The headers of the incoming STOMP frame (ignored in this implementation).
     * @param payload The deserialized Java object payload (of type T).
     */
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {

        T cast = (T) payload;
        listener.accept(cast);
    }
}

