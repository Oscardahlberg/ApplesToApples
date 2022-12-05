package Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public record PlayerSocketInfo(Socket socket, ObjectInputStream objectInputStream,
                               ObjectOutputStream objectOutputStream) {

}
