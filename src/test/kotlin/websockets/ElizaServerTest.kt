@file:Suppress("NoWildcardImports")

package websockets

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.websocket.ClientEndpoint
import jakarta.websocket.ContainerProvider
import jakarta.websocket.OnMessage
import jakarta.websocket.Session
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.URI
import java.util.concurrent.CountDownLatch

private val logger = KotlinLogging.logger {}

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ElizaServerTest {
    @LocalServerPort
    private var port: Int = 0

    @Test
    fun onOpen() {
        logger.info { "This is the test worker" }
        val latch = CountDownLatch(3)
        val list = mutableListOf<String>()

        val client = SimpleClient(list, latch)
        client.connect("ws://localhost:$port/eliza")
        latch.await()
        assertEquals(3, list.size)
        assertEquals("The doctor is in.", list[0])
    }

    @Test
    fun onChat() {
        logger.info { "Test thread" }
        val latch = CountDownLatch(4)
        val list = mutableListOf<String>()

        val client = ComplexClient(list, latch)
        client.connect("ws://localhost:$port/eliza")
        latch.await()
        val size = list.size
        // 1. EXPLAIN WHY size = list.size IS NECESSARY

        /*  Guardamos el tamñao de la lista en la variable size porque el método await() puede
         *  provocar que el hilo se bloquee hasta que el contador llegue a cero. Durante este tiempo,
         *  el cliente puede recibir mensajes adicionales que se añaden a la lista, lo que haría que
         *  el tamaño de la lista cambie después de la espera. Al almacenar el tamaño en una variable
         *  separada, nos aseguramos de que estamos trabajando con un valor consistente y no afectado
         *  por posibles cambios posteriores en la lista.
         */

        // 2. REPLACE BY assertXXX expression that checks an interval; assertEquals must not be used;
        assertTrue(size in 2..4)

        // 3. EXPLAIN WHY assertEquals CANNOT BE USED AND WHY WE SHOULD CHECK THE INTERVAL

        /*  No se puede usar assertEquals porque el número exacto de mensajes recibidos puede variar
         *  debido a que websockets es asíncrono y depende de la latencia de la red. Por eso, al verificar
         *  un intervalo, nos aseguramos de que el test sea más robusto.
         */

        // 4. COMPLETE assertEquals(XXX, list[XXX])
        assertEquals("The doctor is in.", list[0])
        assertTrue(list.any { it.contains("you") || it.contains("feel") || it.contains("?") })
    }
}

@ClientEndpoint
class SimpleClient(
    private val list: MutableList<String>,
    private val latch: CountDownLatch,
) {
    @OnMessage
    fun onMessage(message: String) {
        logger.info { "Client received: $message" }
        list.add(message)
        latch.countDown()
    }
}

@ClientEndpoint
class ComplexClient(
    private val list: MutableList<String>,
    private val latch: CountDownLatch,
) {
    @OnMessage
    @Suppress("UNUSED_PARAMETER") // Remove this line when you implement onMessage
    fun onMessage(
        message: String,
        session: Session,
    ) {
        logger.info { "Client received: $message" }
        list.add(message)
        latch.countDown()
        // 5. COMPLETE if (expression) {
        // 6. COMPLETE   sentence
        // }
        if (list.size == 3) {                                   // Si es el primer mensaje (bienvenida)
            session.basicRemote.sendText("I am feeling sad")    // Enviamos mensaje de prueba
        }
    }
}

fun Any.connect(uri: String) {
    ContainerProvider.getWebSocketContainer().connectToServer(this, URI(uri))
}
