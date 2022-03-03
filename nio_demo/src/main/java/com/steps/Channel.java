package com.steps;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.RandomAccess;

/**
 * @date 2021-09-20 17:37
 */
public class Channel {
    public static void main(String[] args) throws Exception {
        RandomAccessFile rad = new RandomAccessFile("F:\\test.txt","rw");
        FileChannel channel = rad.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = channel.read(buffer);
        while (read!=-1){
            buffer.flip();
            while (buffer.hasRemaining()){
                System.out.println((char) buffer.get());
            }
            buffer.clear();
            read = channel.read(buffer);
        }
        channel.close();
        rad.close();

    }
}
