package com.hbase.test;

import com.hbase.test.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author jianghe.cao
 */
public class AccUserStat extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccUserStat.class);

    private static String dateDay="20180501";
    private static String workID = "xx";
    private boolean isSucc = true;
    static Map<String, Long> accValsHashMap = new HashMap<String, Long>();
    private static Configuration conf = null;

    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.zookeeper.quorum", "tw-master,tw-slave01,tw-slave02");
        conf.set("hbase.master", "tw-master");
    }
    /**
     * Count accumulate user number on dateDay.
     *
     * @param dateDay
     * @param workID
     */
    public AccUserStat(String dateDay, String workID) {
        this.dateDay = dateDay;
        this.workID = workID;
    }

    public boolean isSucc() {
        return isSucc;
    }

     public static void main(String []args) {
        String JOB_NAME =  "Job-AccUserStat";
        String jobID = JOB_NAME+ workID;
//        SQLLogger.insJobStart(workID, jobID, JOB_NAME);
        String msg = "";
        try {
            // Start ...
            LOGGER.info("AccUserStat starting ...");
            calcAccUser();
        } catch (Exception e) {
            LOGGER.error("An error was caught", e);
        } finally {
            LOGGER.info("AccUserStat completed.");
        }
    }

    /**
     * Calculate accumulated users.
     *
     * @throws IOException
     */
    private static void calcAccUser() throws IOException {
        // <"productid<splitor>Qualifier", count>
        // Add column filters of new users and sessions.
        FilterList colFilterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        colFilterList.addFilter(
                new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("N"))));
        colFilterList.addFilter(new ColumnPrefixFilter(Bytes.toBytes("NV")));
        colFilterList.addFilter(new ColumnPrefixFilter(Bytes.toBytes("NC")));
        colFilterList.addFilter(new ColumnPrefixFilter(Bytes.toBytes("UV")));
        colFilterList.addFilter(new QualifierFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes("Session"))));
        colFilterList.addFilter(new ColumnPrefixFilter(Bytes.toBytes("SV")));
        colFilterList.addFilter(new ColumnPrefixFilter(Bytes.toBytes("SC")));
        // Begin calculation for current day.
//        HTableInterface sumStatTable = RazorConf.getHBaseConn().getTable(TableName.valueOf(MetaTables.SumStatistic.TABLE_NAME));
        HTableInterface sumStatTable = new HTable(conf, "gd:Sum_Statistic");
        // Begin scanning.
        Scan scan = new Scan();
        scan.setFilter(colFilterList);
        scan.setCaching(2000);
        ResultScanner sumStatScanner = sumStatTable.getScanner(scan);
        // Scan all rows.
        for (Result rowRes : sumStatScanner) {
            String rowKey = new String(rowRes.getRow());
            String[] rkSlices = rowKey.split("_");
            String productId = rkSlices[0];
            String dateD = rkSlices[1];
            if (dateD.compareTo(dateDay) > 0) {
                continue;
            }
            // Iterate cells in current row.
            for (Cell cell : rowRes.rawCells()) {
                String qualifier = new String(CellUtil.cloneQualifier(cell));
                System.out.println("rowkey:"+rowKey);
                sumStatPro( rowRes, productId, cell, qualifier);
            }
        }
        // Close Scanner.
        sumStatScanner.close();
        // Put result into HTable "Sum_Statistic".
        Set<Entry<String, Long>> accValsSet = accValsHashMap.entrySet();
        for (Entry<String, Long> accValEntry : accValsSet) {
            String[] keySlices = accValEntry.getKey().split(Constants.SPLIT_CHARACTERS_1);
            String productID = keySlices[0];
            byte[] rowKey = Bytes.toBytes(productID + "_" + dateDay);
            byte[] qualifier = Bytes.toBytes(keySlices[1]);
            byte[] value = Bytes.toBytes(String.valueOf(accValEntry.getValue()));
            Put put = new Put(rowKey);
            put.add("f".getBytes(), qualifier, value);
            sumStatTable.put(put);
        }
        sumStatTable.close();
    }
    //  the method like *Pro was add on 2017-10-17 10:51:52
    private static void sumStatPro( Result rowRes, String productId, Cell cell, String qualifier) {
        Long val;
        if (qualifier.startsWith("S")) {
            val =
                    Bytes.toLong(rowRes.getValue("f".getBytes(), CellUtil.cloneQualifier(cell)));
        } else {
            val = Long.valueOf(
                    new String(rowRes.getValue("f".getBytes(), CellUtil.cloneQualifier(cell))));
        }
        if ("N".equals(qualifier))
            accnpPro( productId, val);
        else if (qualifier.startsWith("NV"))
            accnvPro( productId, qualifier.split("_")[1], val);
        else if (qualifier.startsWith("UV")) { // UV add to ACCNV
            accnvPro( productId, qualifier.split("_")[1], val);
        } else if (qualifier.startsWith("NC"))
            accncPro( productId, qualifier.split("_")[1], val);
        else if ("Session".equals(qualifier))
            accspPro( productId, val);
        else if (qualifier.startsWith("SV")){

            accsvPro( productId, qualifier.split("_")[1], val);
        }
        else if (qualifier.startsWith("SC"))
            accscPro( productId, qualifier.split("_")[1], val);
    }


    private static void accscPro( String productId, String s, Long val) {
        String channelId = s;
        String key = productId + Constants.SPLIT_CHARACTERS_1 + "ACCSC_" + channelId;
        Long val1 = val;
        if (accValsHashMap.containsKey(key)) {
            val1 = val + accValsHashMap.get(key);
        }
        accValsHashMap.put(key, val1);
    }

    private static void accsvPro( String productId, String s, Long val) {
        String version = s;
        String key = productId + Constants.SPLIT_CHARACTERS_1 + "ACCSV_" + version;
        Long val1 = val;
        if (accValsHashMap.containsKey(key)) {
            val1 = val + accValsHashMap.get(key);
        }
        accValsHashMap.put(key, val1);
    }

    private static void accspPro(String productId, Long val) {
        String key = productId + Constants.SPLIT_CHARACTERS_1 + "ACCSP";
        Long val1 = val;
        if (accValsHashMap.containsKey(key)) {
            val1 = val + accValsHashMap.get(key);
        }
        accValsHashMap.put(key, val1);
    }

    private static void accncPro(String productId, String s, Long val) {
        String channelId = s;
        String key = productId + Constants.SPLIT_CHARACTERS_1 + "ACCNC_" + channelId;
        Long val1 = val;
        if (accValsHashMap.containsKey(key)) {
            val1 = val + accValsHashMap.get(key);
        }
        accValsHashMap.put(key, val1);
    }

    private static void accnvPro( String productId, String s, Long val) {
        String version = s;
        String key = productId + Constants.SPLIT_CHARACTERS_1 + "ACCNV_" + version;
        Long val1 = val;
        if (accValsHashMap.containsKey(key)) {
            val1 = val + accValsHashMap.get(key);
        }
        accValsHashMap.put(key, val1);
    }


    private static void accnpPro(String productId, Long val) {
        String key = productId + Constants.SPLIT_CHARACTERS_1 + "ACCNP";
        Long val1 = val;
        if (accValsHashMap.containsKey(key)) {
            val1 = val + accValsHashMap.get(key);
        }
        accValsHashMap.put(key, val1);
    }
}
