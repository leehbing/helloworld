package com.it18zhang.avro.test;

import com.it18zhang.avro.test.model.User;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.File;

/**
 * Created by hongbing.li on 2017/8/18.
 */
public class Deserialize {
    public static void main(String args[]) throws Exception {
        File file = new File("d:/avro/users.avro");
        // Deserialize Users from disk
        DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader<User>(file, userDatumReader);
        User user = null;
        while (dataFileReader.hasNext()) {
        // Reuse user object by passing it to next(). This saves us from
        // allocating and garbage collecting many objects for files with many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}
