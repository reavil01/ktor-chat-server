import io.ktor.http.cio.websocket.*
import java.util.*

class Connection (
    val session: DefaultWebSocketSession,
//    val name: String,
) {
    val name = UUID.randomUUID()
}
