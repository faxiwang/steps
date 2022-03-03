package com.steps.client;

import java.io.IOException;

/**
 * @author fx
 * @date 2021-09-26 23:56
 */
public class AClient {
    public static void main(String[] args) throws IOException {
        new SocketClient().startClient("李四");
    }
}
