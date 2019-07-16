package com.lc.rpc.samples.exporter.impl;

import com.lc.rpc.samples.api.EchoService;

public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String ping) {
        return ping != null ? ping + " --> I am OK." : "I am OK.";
    }
}
