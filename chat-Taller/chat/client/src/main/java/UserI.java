import com.zeroc.Ice.Current;

public class UserI implements Demo.User {
    @Override
    public void receiveMessage(String message, String sender, Current current) {
        // Mostrar el remitente y el mensaje
        System.out.println("Nuevo mensaje de " + sender + ": " + message);
    }
}
