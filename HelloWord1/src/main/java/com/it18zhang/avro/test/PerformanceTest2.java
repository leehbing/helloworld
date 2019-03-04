package com.it18zhang.avro.test;

import com.it18zhang.avro.test.model.MyUser;
import com.it18zhang.avro.test.model.User;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.*;

/**
 * Created by hongbing.li on 2017/8/20.
 * 性能评测,看时间和空间,反串行化
 */
public class PerformanceTest2 {
    private static int max = 1000000;

    public static void main(String[] args) throws Exception {
        javaDeSerial();
        writableDeSerial();
        avroDeSerial();

    }

    /**
     * java反串行化，
     *
     * @throws Exception
     */
    private static void javaDeSerial() throws Exception {
        long start = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream("d:/avro/users.java");//从文件反串行化
        ObjectInputStream oos = new ObjectInputStream(fis);
        MyUser user =null;
        for(int i=0;i<max;i++){
            user = (MyUser)oos.readObject();
        }
        oos.close();
        System.out.println("java deserial : " + (System.currentTimeMillis() - start) + " : " );
    }

    /**
     * Writable反串行化
     *
     * @throws Exception
     */
    private static void writableDeSerial() throws Exception {
        long start = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream("d:/avro/users.writable");//从文件反串行化

        DataInputStream dos = new DataInputStream(fis);
        MyUser user = new MyUser();
        for(int i=0;i<max;i++){
            user.readFields(dos);
        }
        dos.close();
        System.out.println("writable deserial : " + (System.currentTimeMillis() - start) + " : " );
    }

    /**
     * 使用avro反串行化
     */
    private static void avroDeSerial() throws Exception {
        long start = System.currentTimeMillis();
        File file = new File("d:/avro/users.avro");//从文反件串行化

        DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader<User>(file,userDatumReader);
        User user = new User();
        while(dataFileReader.hasNext()) {
            dataFileReader.next();
        }
        dataFileReader.close();
        System.out.println("avro deserial : " + (System.currentTimeMillis() - start) + " : ");

    }
}
