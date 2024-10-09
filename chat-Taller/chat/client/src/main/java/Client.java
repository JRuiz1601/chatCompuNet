import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            // Proxy para comunicarse con el servidor de chat
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("ChatGroup:default -p 10000");
            Demo.ChatGroupPrx chatGroup = Demo.ChatGroupPrx.checkedCast(base);
            if (chatGroup == null) {
                throw new Error("Invalid proxy");
            }

            // Crear el adaptador para el cliente (usuario)
            com.zeroc.Ice.ObjectAdapter userAdapter = communicator.createObjectAdapterWithEndpoints("UserAdapter", "default -p 0");

            // Crear una instancia de UserI para recibir mensajes
            Demo.User user = new UserI();

            // Registrar el objeto del usuario y obtener su proxy bidireccional
            com.zeroc.Ice.ObjectPrx userProxy = userAdapter.addWithUUID(user);  // Proxy bidireccional

            // Activar el adaptador del cliente
            userAdapter.activate();

            // Solicitar username al cliente
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            // Registrar al usuario en el servidor junto con su proxy
            chatGroup.registerUserWithProxy(username, communicator.proxyToString(userProxy));

            // Interfaz para el cliente
            String input;

            while (true) {
                System.out.println("\nSeleccione una opción:");
                System.out.println("1. Grupos");
                System.out.println("2. Mensajes Privados");
                System.out.println("3. Salir");

                System.out.print("> ");
                input = scanner.nextLine();

                switch (input) {
                    case "1": // Sección de Grupos
                        handleGroupsCommands(scanner, chatGroup, username);
                        break;
                    case "2": // Sección de Mensajes Privados
                        handlePrivateMessagesCommands(scanner, chatGroup, username);
                        break;
                    case "3": // Salir
                        System.out.println("Exiting chat client...");
                        return;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleGroupsCommands(Scanner scanner, Demo.ChatGroupPrx chatGroup, String username) {
        String input;
        System.out.println("\nComandos de Grupos:");
        System.out.println("1. createGroup <groupName>");
        System.out.println("2. addUser <groupName> <userName>");
        System.out.println("3. sendMessage <groupName> <message>");
        System.out.println("4. listGroups");
        System.out.println("Escribe 'back' para volver al menú principal");

        while (true) {
            System.out.print("> ");
            input = scanner.nextLine();

            if (input.equals("back")) {
                break; // Regresar al menú principal
            }

            String[] parts = input.split(" ");
            String command = parts[0];

            switch (command) {
                case "createGroup":
                    if (parts.length == 2) {
                        chatGroup.createGroup(parts[1], username);
                    } else {
                        System.out.println("Uso: createGroup <groupName>");
                    }
                    break;
                case "addUser":
                    if (parts.length == 3) {
                        chatGroup.addUserToGroup(parts[1], parts[2]);
                    } else {
                        System.out.println("Uso: addUser <groupName> <userName>");
                    }
                    break;
                case "sendMessage":
                    if (parts.length >= 3) {
                        String groupName = parts[1];
                        StringBuilder message = new StringBuilder();
                        for (int i = 2; i < parts.length; i++) {
                            message.append(parts[i]).append(" ");
                        }
                        chatGroup.sendMessageToGroup(groupName, message.toString().trim(), username);
                    } else {
                        System.out.println("Uso: sendMessage <groupName> <message>");
                    }
                    break;
                case "listGroups":
                    chatGroup.listGroups();
                    break;
                default:
                    System.out.println("Comando desconocido. Intenta de nuevo.");
            }
        }
    }

    private static void handlePrivateMessagesCommands(Scanner scanner, Demo.ChatGroupPrx chatGroup, String username) {
        String input;
        System.out.println("\nComandos de Mensajes Privados:");
        System.out.println("1. sendToUser <userName> <message>");
        System.out.println("Escribe 'back' para volver al menú principal");

        while (true) {
            System.out.print("> ");
            input = scanner.nextLine();

            if (input.equals("back")) {
                break; // Regresar al menú principal
            }

            String[] parts = input.split(" ");
            String command = parts[0];

            switch (command) {
                case "sendToUser": // Nueva opción para enviar mensaje a un usuario específico
                    if (parts.length >= 3) {
                        String userName = parts[1];
                        StringBuilder message = new StringBuilder();
                        for (int i = 2; i < parts.length; i++) {
                            message.append(parts[i]).append(" ");
                        }
                        chatGroup.sendMessageToUser(userName, message.toString().trim(), username);
                    } else {
                        System.out.println("Uso: sendToUser <userName> <message>");
                    }
                    break;
                default:
                    System.out.println("Comando desconocido. Intenta de nuevo.");
            }
        }
    }
}
