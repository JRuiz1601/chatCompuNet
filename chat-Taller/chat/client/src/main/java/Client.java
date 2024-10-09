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

            // Solicitar username al cliente
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            chatGroup.registerUser(username);

            // Interfaz para el cliente
            String input;
            System.out.println("Chat Client Started");
            System.out.println("Commands: \n1. createGroup <groupName>\n2. addUser <groupName> <userName>\n3. sendMessage <groupName> <message> \\n4. sendToUser <userName> <message> \n5. listGroups\nType 'exit' to quit");

            while (true) {
                System.out.print("> ");
                input = scanner.nextLine();

                String[] parts = input.split(" ");
                String command = parts[0];

                switch (command) {
                    case "createGroup":
                    if (parts.length == 2) {
                        chatGroup.createGroup(parts[1], username);  // Pasar el nombre de usuario al crear el grupo
                    } else {
                        System.out.println("Usage: createGroup <groupName>");
                    }
                    break;
                    case "addUser":
                        if (parts.length == 3) {
                            chatGroup.addUserToGroup(parts[1], parts[2]);
                        } else {
                            System.out.println("Usage: addUser <groupName> <userName>");
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
                            System.out.println("Usage: sendMessage <groupName> <message>");
                        }
                        break;
                    case "sendToUser": // Nueva opción para enviar mensaje a un usuario específico
                    if (parts.length >= 3) {
                        String userName = parts[1];
                        StringBuilder message = new StringBuilder();
                        for (int i = 2; i < parts.length; i++) {
                            message.append(parts[i]).append(" ");
                        }
                        chatGroup.sendMessageToUser(userName, message.toString().trim());
                    } else {
                        System.out.println("Usage: sendToUser <userName> <message>");
                    }
                    break;
                    case "listGroups":
                        chatGroup.listGroups();
                        break;

                    case "exit":
                        System.out.println("Exiting chat client...");
                        return;

                    default:
                        System.out.println("Unknown command. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
