module Demo
{
    interface Printer
    {
        void printString(string s);
    }

    interface ChatGroup {
        void createGroup(string groupName, string username);
        void addUserToGroup(string groupName, string userName);
        void sendMessageToGroup(string groupName, string message, string sender);
        void sendMessageToUser(string userName, string message, string sender);
        void listGroups();
        void registerUserWithProxy(string username, string userProxy);
    }

    interface User {
        void receiveMessage(string message, string sender);
    }
}
