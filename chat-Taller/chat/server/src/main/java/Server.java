public class Server {
    public static void main(String[] args) {
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            // Crear un adaptador con el nombre 'ChatAdapter' y el puerto 10000
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("ChatAdapter", "default -p 10000");
            
            // Crear la instancia del objeto MychatI, que maneja la lógica del chat
            com.zeroc.Ice.Object object = new MychatI();
            
            // Asignar el objeto a una identidad única 'ChatGroup'
            adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("ChatGroup"));
            
            // Activar el adaptador para comenzar a escuchar las solicitudes
            adapter.activate();
            
            System.out.println("Chat server is running...");
            
            // Mantener el servidor en funcionamiento esperando solicitudes
            communicator.waitForShutdown();
        }
    }
}
