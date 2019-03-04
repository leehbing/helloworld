package hamner.db;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Author lihongbing on 6/24/2018.
 */

public class DBTest {
    public static void main(String [] args) throws Exception{
//        RecordsFile recordsFile = new RecordsFile("testDatabase.jdb", 64);
//        RecordWriter rw = new RecordWriter("foo.lastAccessTime");
//        rw.writeObject(new Date());
//        recordsFile.insertRecord(rw);
        RecordsFile recordsFile = new RecordsFile("testDatabase.jdb", "r");
        RecordReader rr = recordsFile.readRecord("foo.lastAccessTime");
        Date d = (Date)rr.readObject();
        System.out.println("last access was at: " + d.toString());
    }
}
