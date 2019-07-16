package com.lc.rpc.samples.exporter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RpcExporter {

    static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void exporter(String hostName, int port) throws Exception {
        ServerSocket server = new ServerSocket();

        server.bind(new InetSocketAddress(hostName, port));

        try {
            while (true) {
                executor.execute(new ExporterTask(server.accept()));
            }
        } finally {
            server.close();
        }
    }

    private static class ExporterTask implements Runnable {

        Socket client;

        public ExporterTask(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {

            try (ObjectInputStream is = new ObjectInputStream(client.getInputStream());
                 ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream())) {

                String interfaceName = is.readUTF();

                Class<?> service = Class.forName(interfaceName);

                String methodName = is.readUTF();

                Class<?>[] parameterTypes = (Class<?>[]) is.readObject();

                Object[] args = (Object[]) is.readObject();

                Method method = service.getMethod(methodName, parameterTypes);

                Object result = method.invoke(service.getDeclaredConstructor().newInstance(), args);

                os.writeObject(result);

                System.out.println("I am working. interfaceName:"+interfaceName);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
