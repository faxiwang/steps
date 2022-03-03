package com.steps.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @date 2021-09-26 22:45
 */
public class SocketServer {
    public static void main(String[] args) {
        try {
            new SocketServer().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() throws IOException {
        //创建选择器
        Selector selector = Selector.open();
        //打开 server 通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定服务端口
        serverSocketChannel.bind(new InetSocketAddress(5000));
        //非堵塞模式
        serverSocketChannel.configureBlocking(false);
        //将通道注册到选择器中，绑定为接收状态
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务端已启动...");
        while (true) {
            //获取通道数量
            int selectSize = selector.select();
            if (selectSize == 0) continue;

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                //是否接收状态
                if (key.isAcceptable()) {
                    acceptOperator(serverSocketChannel, selector);
                }
                //是否可读
                if (key.isReadable()) {
                    readOperator(selector, key);
                }
            }
        }
    }

    private void readOperator(Selector selector, SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = channel.read(buffer);
        StringBuilder msg = new StringBuilder();
        if (read > 0) {
            //切换为读模式
            buffer.flip();
            msg.append(Charset.forName("UTF-8").decode(buffer));

        }
        channel.register(selector, SelectionKey.OP_READ);
        //将数据广播给其它客户端
        if (msg.length() > 0) {
            System.out.println(msg);
            castOperator(msg, selector, key);
        }
    }

    private void castOperator(StringBuilder msg, Selector selector, SelectionKey socketChannel) throws IOException {
        //从选择器中获取所有通道
        Set<SelectionKey> keys = selector.keys();
        //将消息发送给选择器中的其它通道，不包括自身的通道
        for (SelectionKey selectionKey : keys) {
            Channel channel = selectionKey.channel();
            if (channel instanceof SocketChannel && channel != socketChannel) {
                ((SocketChannel) channel).write(Charset.forName("UTF-8").encode(msg.toString()));
            }
        }
    }

    private void acceptOperator(ServerSocketChannel socketChannel, Selector selector) throws IOException {
        //接受连接
        SocketChannel channel = socketChannel.accept();
        //非堵塞
        channel.configureBlocking(false);
        //将 channel 注册到 selector 监听读状态
        channel.register(selector, SelectionKey.OP_READ);
        //给客户端一个响应
        channel.write(Charset.forName("UTF-8").encode("欢迎进入聊天室"));

    }
}
