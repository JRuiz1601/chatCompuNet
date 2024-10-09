module Demo
{
    interface Printer
    {
        void printString(string s);
    }

    interface ChatGroup {
        void createGroup(string groupName, string username);  // Añadido username como parámetro
        void addUserToGroup(string groupName, string userName);
        void sendMessageToGroup(string groupName, string message, string sender);
        void sendMessageToUser(string userName, string message); // Nuevo método para enviar mensaje a un usuario específico
        void listGroups();
        void registerUser(string username);
    }
}
