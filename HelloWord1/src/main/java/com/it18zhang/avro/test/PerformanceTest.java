package com.it18zhang.avro.test;

import com.it18zhang.avro.test.model.MyUser;
import com.it18zhang.avro.test.model.User;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.*;

/**
 * Created by hongbing.li on 2017/8/20.
 * 性能评测,看时间和空间
 */
public class PerformanceTest {
    private static int max = 1000000;

    public static void main(String[] args) throws Exception {
        javaSerial();
        writableSerial();
        avroSerial();

    }

    /**
     * java串行化，
     *
     * @throws Exception
     */
    private static void javaSerial() throws Exception {
        long start = System.currentTimeMillis();
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();//往内存串行化
        FileOutputStream baos = new FileOutputStream("d:/avro/users.java");//往文件串行化
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        for (int i = 0; i < max; i++) {
            MyUser user = new MyUser();
            user.setName("tom" + i);
            user.setFavarite_number(i);
            user.setFavarite_color("red"+i);
            oos.writeObject(user);
        }
        oos.close();
        System.out.println("java serial : " + (System.currentTimeMillis() - start) + " : " /*+ baos.toByteArray().length*/);
    }

    /**
     * Writable串行化
     *
     * @throws Exception
     */
    private static void writableSerial() throws Exception {
        long start = System.currentTimeMillis();
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();//往内存串行化
        FileOutputStream baos = new FileOutputStream("d:/avro/users.writable");//往文件串行化
        DataOutputStream dos = new DataOutputStream(baos);
        MyUser user = null;
        for (int i = 0; i < max; i++) {
            user = new MyUser();
            user.setName("tom" + i);
            user.setFavarite_number(i);
            user.setFavarite_color("red" + i);
            user.write(dos);
        }
        dos.close();
        System.out.println("writable serial : " + (System.currentTimeMillis() - start) + " : " /*+ baos.toByteArray().length*/);
    }

    /**
     * 使用avro穿行化
     */
    private static void avroSerial() throws Exception {
        long start = System.currentTimeMillis();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
        User user = new User();
//        dataFileWriter.create(user.getSchema(),baos);//往内存串行化
        dataFileWriter.create(user.getSchema(),new FileOutputStream("d:/avro/users.avro"));//往文件串行化
        for (int i = 0; i < max; i++) {
            User u0 = new User();
            u0.setName("tom" + i);
            u0.setFavoriteNumber(i);
            u0.setFavoriteColor("red" + i);
            dataFileWriter.append(u0);
        }
        dataFileWriter.close();
        System.out.println("avro serial : " + (System.currentTimeMillis() - start) + " : "/* + baos.toByteArray().length*/);

    }
}
