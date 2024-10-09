import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Util;

public class MychatI implements Demo.ChatGroup {
    // Mapa para almacenar grupos de chat y los usuarios en cada grupo
    private Map<String, Set<String>> groups = new HashMap<>();
    // Mapa para almacenar usuarios registrados
    private Set<String> registeredUsers = new HashSet<>();
    private Map<String, String> userConnections = new HashMap<>(); // Mapa para almacenar conexiones de usuarios

    @Override
    public void createGroup(String groupName, String username, Current current) {
        if (!groups.containsKey(groupName)) {
            groups.put(groupName, new HashSet<>());
            groups.get(groupName).add(username);  // Agregar al creador al grupo
            System.out.println("Grupo creado: " + groupName + " por " + username);
        } else {
            System.out.println("El grupo " + groupName + " ya existe.");
        }
    }

    @Override
    public void addUserToGroup(String groupName, String userName, Current current) {
        if (!registeredUsers.contains(userName)) {
            System.out.println("El usuario " + userName + " no está registrado.");
            return;
        }
        Set<String> users = groups.get(groupName);
        if (users != null) {
            users.add(userName);
            System.out.println("Usuario " + userName + " añadido al grupo " + groupName);
        } else {
            System.out.println("El grupo " + groupName + " no existe.");
        }
    }

    /* 
    @Override
    public void sendMessageToUser(String userName, String message, Current current) {
        // Verificar si el usuario está conectado y su proxy está disponible
        if (userConnections.containsKey(userName)) {
            // Obtener el proxy del usuario
            String userProxyString = userConnections.get(userName);
            com.zeroc.Ice.ObjectPrx userProxy = com.zeroc.Ice.Util.stringToProxy(userProxyString);
            Demo.UserPrx user = Demo.UserPrx.checkedCast(userProxy);
            
            if (user != null) {
                // Enviar el mensaje al usuario
                try {
                    user.receiveMessage(message); // Método para que el usuario reciba el mensaje
                    System.out.println("Mensaje enviado a " + userName + ": " + message);
                } catch (Exception e) {
                    System.out.println("Error al enviar el mensaje a " + userName + ": " + e.getMessage());
                }
            } else {
                System.out.println("El proxy del usuario " + userName + " no es válido.");
            }
        } else {
            System.out.println("El usuario " + userName + " no está conectado.");
        }
    }
    
    */
    @Override
    public void sendMessageToGroup(String groupName, String message, String sender, Current current) {
        Set<String> users = groups.get(groupName);
        if (users != null) {
            if (users.contains(sender)) {
                System.out.println("Mensaje de " + sender + " en " + groupName + ": " + message);
                // Aquí puedes agregar lógica adicional para enviar el mensaje a los usuarios del grupo
            } else {
                System.out.println("El usuario " + sender + " no está en el grupo " + groupName);
            }
        } else {
            System.out.println("El grupo " + groupName + " no existe.");
        }
    }

    @Override
    public void listGroups(Current current) {
        // Imprimir los nombres de los grupos
        System.out.println("Grupos actuales: ");
        for (String group : groups.keySet()) {
            System.out.println(group);
        }
    }

    @Override
    public void registerUser(String username,Current current) {
        if (!registeredUsers.contains(username)) {
            registeredUsers.add(username);
            System.out.println("Usuario registrado: " + username);
        } else {
            System.out.println("El usuario " + username + " ya está registrado.");
        }
    }

    @Override
    public void sendMessageToUser(String userName, String message, Current current) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMessageToUser'");
    }
}
