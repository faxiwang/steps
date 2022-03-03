package com.steps.client;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author fx
 * @date 2021-09-26 23:29
 */
public class SocketClient {
    public void startClient(String name) throws IOException {

        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 5000));

        socketChannel.configureBlocking(false);

        socketChannel.register(selector, SelectionKey.OP_READ);

        new Thread(new ClientThread(selector)).start();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            socketChannel.write(Charset.forName("UTF-8").encode(name+": "+msg));
        }
    }

}
