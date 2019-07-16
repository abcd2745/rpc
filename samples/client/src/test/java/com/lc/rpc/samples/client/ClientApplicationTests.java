package com.lc.rpc.samples.client;

import com.lc.rpc.samples.api.EchoService;
import com.lc.rpc.samples.exporter.impl.EchoServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetSocketAddress;

public class ClientApplicationTests {

    @Test
    public void contextLoads() {
        RpcImporter<EchoService> importer = new RpcImporter<>();
        EchoService echo = importer.importer(EchoServiceImpl.class,new InetSocketAddress("localhost",8080));

        System.out.println(echo.echo("Are you Ok ?"));

    }

}
