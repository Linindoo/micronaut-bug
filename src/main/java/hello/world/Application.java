package hello.world;

import com.mongodb.internal.connection.tlschannel.BufferAllocator;
import com.mongodb.internal.connection.tlschannel.DirectBufferAllocator;
import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        BufferAllocator defaultEncryptedBufferAllocator = new DirectBufferAllocator();
        Micronaut.run(Application.class, args);
    }
}
