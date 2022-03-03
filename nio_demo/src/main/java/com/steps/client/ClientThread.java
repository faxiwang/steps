package com.steps.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author fx
 * @date 2021-09-26 23:46
 */
public class ClientThread implements Runnable {
    private Selector selector;

    public ClientThread(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (true) {
                //获取通道数量
                int selectSize = selector.select();
                if (selectSize == 0) continue;

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    //是否可读
                    if (key.isReadable()) {
                        readOperator(selector, key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }
    }
}
