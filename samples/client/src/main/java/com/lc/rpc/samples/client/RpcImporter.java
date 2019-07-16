package com.lc.rpc.samples.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcImporter<S> {

    public S importer(final Class<?> serviceClass, final InetSocketAddress addr) {
        return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass.getInterfaces()[0]},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        try (Socket socket = new Socket()) {
                            socket.connect(addr);

                            try (ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                                 ObjectInputStream is = new ObjectInputStream(socket.getInputStream())) {
                                os.writeUTF(serviceClass.getName());
                                os.writeUTF(method.getName());
                                os.writeObject(method.getParameterTypes());
                                os.writeObject(args);

                                return is.readObject();
                            }

                        }
                    }
                });
    }
}
