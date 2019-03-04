package com.elasticsearch.test;
import org.elasticsearch.action.bulk.BulkRequestBuilder;

import java.io.*;
import java.util.*;

public class EsTest {


    static String SPLITTER = "|";
    public static String[] COLUMNS =new String[600];

    /**
     * 批量上传数据到elasticsearch
     */
    public static void UploadDataToEs() {
            String FILE_PATH = "d:/1000000.txt";     //先生成需上传的数据，存放于txt文件中
            System.out.println("生成多少条数据？");
            int MAX_LINES =1000000;
            FileOutputStream o=null;
            try {
                o = new FileOutputStream(FILE_PATH);
//                String firstLine ="CUSTID|fuzzy1|fuzzy2|fuzzy3|fuzzy4|fuzzy5|fuzzy6|fuzzy7|fuzzy8|fuzzy9|fuzzy10|fuzzy11|fuzzy12|fuzzy13|fuzzy14|fuzzy15|fuzzy16|fuzzy17|fuzzy18|fuzzy19|fuzzy20|fuzzy21|fuzzy22|fuzzy23|fuzzy24|fuzzy25|fuzzy26|fuzzy27|fuzzy28|fuzzy29|fuzzy30|fuzzy31|fuzzy32|fuzzy33|fuzzy34|fuzzy35|fuzzy36|fuzzy37|fuzzy38|fuzzy39|fuzzy40|fuzzy41|fuzzy42|fuzzy43|fuzzy44|fuzzy45|fuzzy46|fuzzy47|fuzzy48|fuzzy49|fuzzy50|fuzzy51|fuzzy52|fuzzy53|fuzzy54|fuzzy55|fuzzy56|fuzzy57|fuzzy58|fuzzy59|fuzzy60|fuzzy61|fuzzy62|fuzzy63|fuzzy64|fuzzy65|fuzzy66|fuzzy67|fuzzy68|fuzzy69|fuzzy70|fuzzy71|fuzzy72|fuzzy73|fuzzy74|fuzzy75|fuzzy76|fuzzy77|fuzzy78|fuzzy79|fuzzy80|fuzzy81|fuzzy82|fuzzy83|fuzzy84|fuzzy85|fuzzy86|fuzzy87|fuzzy88|fuzzy89|fuzzy90|fuzzy91|fuzzy92|fuzzy93|fuzzy94|fuzzy95|fuzzy96|fuzzy97|fuzzy98|fuzzy99|fuzzy100|range1|range2|range3|range4|range5|range6|range7|range8|range9|range10|range11|range12|range13|range14|range15|range16|range17|range18|range19|range20|range21|range22|range23|range24|range25|range26|range27|range28|range29|range30|range31|range32|range33|range34|range35|range36|range37|range38|range39|range40|range41|range42|range43|range44|range45|range46|range47|range48|range49|range50|range51|range52|range53|range54|range55|range56|range57|range58|range59|range60|range61|range62|range63|range64|range65|range66|range67|range68|range69|range70|range71|range72|range73|range74|range75|range76|range77|range78|range79|range80|range81|range82|range83|range84|range85|range86|range87|range88|range89|range90|range91|range92|range93|range94|range95|range96|range97|range98|range99|range100|flag1|flag2|flag3|flag4|flag5|flag6|flag7|flag8|flag9|flag10|flag11|flag12|flag13|flag14|flag15|flag16|flag17|flag18|flag19|flag20|flag21|flag22|flag23|flag24|flag25|flag26|flag27|flag28|flag29|flag30|flag31|flag32|flag33|flag34|flag35|flag36|flag37|flag38|flag39|flag40|flag41|flag42|flag43|flag44|flag45|flag46|flag47|flag48|flag49|flag50|flag51|flag52|flag53|flag54|flag55|flag56|flag57|flag58|flag59|flag60|flag61|flag62|flag63|flag64|flag65|flag66|flag67|flag68|flag69|flag70|flag71|flag72|flag73|flag74|flag75|flag76|flag77|flag78|flag79|flag80|flag81|flag82|flag83|flag84|flag85|flag86|flag87|flag88|flag89|flag90|flag91|flag92|flag93|flag94|flag95|flag96|flag97|flag98|flag99|flag100|flag101|flag102|flag103|flag104|flag105|flag106|flag107|flag108|flag109|flag110|flag111|flag112|flag113|flag114|flag115|flag116|flag117|flag118|flag119|flag120|flag121|flag122|flag123|flag124|flag125|flag126|flag127|flag128|flag129|flag130|flag131|flag132|flag133|flag134|flag135|flag136|flag137|flag138|flag139|flag140|flag141|flag142|flag143|flag144|flag145|flag146|flag147|flag148|flag149|flag150|flag151|flag152|flag153|flag154|flag155|flag156|flag157|flag158|flag159|flag160|flag161|flag162|flag163|flag164|flag165|flag166|flag167|flag168|flag169|flag170|flag171|flag172|flag173|flag174|flag175|flag176|flag177|flag178|flag179|flag180|flag181|flag182|flag183|flag184|flag185|flag186|flag187|flag188|flag189|flag190|flag191|flag192|flag193|flag194|flag195|flag196|flag197|flag198|flag199|flag200|flag201|flag202|flag203|flag204|flag205|flag206|flag207|flag208|flag209|flag210|flag211|flag212|flag213|flag214|flag215|flag216|flag217|flag218|flag219|flag220|flag221|flag222|flag223|flag224|flag225|flag226|flag227|flag228|flag229|flag230|flag231|flag232|flag233|flag234|flag235|flag236|flag237|flag238|flag239|flag240|flag241|flag242|flag243|flag244|flag245|flag246|flag247|flag248|flag249|flag250|flag251|flag252|flag253|flag254|flag255|flag256|flag257|flag258|flag259|flag260|flag261|flag262|flag263|flag264|flag265|flag266|flag267|flag268|flag269|flag270|flag271|flag272|flag273|flag274|flag275|flag276|flag277|flag278|flag279|flag280|flag281|flag282|flag283|flag284|flag285|flag286|flag287|flag288|flag289|flag290|flag291|flag292|flag293|flag294|flag295|flag296|flag297|flag298|flag299|flag300\n";
                String firstLine="CUSTID";
                int fuzzy=1;
                int range=1;
                int lable=1;
                for(int i= 0;i<600;i++){
                    if(i<150){
                        firstLine+="|fuzzy"+fuzzy;
                        fuzzy++;
                    }else if(i>149&&i<300){
                        firstLine+="|range"+range;
                        range++;
                    }else {
                        firstLine+="|lable"+lable;
                        lable++;
                    }
                }
                o.write(firstLine.getBytes("UTF-8"));
                for(int i=1;i<=MAX_LINES;i++) {
                    String content = generateLines(i);
                    o.write(content.getBytes("UTF-8"));
                }
                o.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        String file = ("D:\\1000000.txt");
        String index = "test_fox";  //索引名称
        String type = "data";

        long startTime = System.currentTimeMillis();    //获取开始时间
        Map<String, Object> ret = new HashMap<String, Object>();
        ArrayList<String> arrayListString = new ArrayList<String>(2000);
        BulkRequestBuilder bulkRequest = Es_Client.getTransportClient().prepareBulk();
        StringBuffer sb = new StringBuffer();
        try {
            RandomAccessFile raf = new RandomAccessFile(file,"r");
            String s ;
            long nowLine = 0;
            try{
                while((s=raf.readLine())!=null){
                    String[] firstLine = s.split("\\|");
                    if(501!=firstLine.length){
                        throw new RuntimeException("格式不对");
                    }
                    for(int i=1;i<firstLine.length;i++){
                        COLUMNS[i-1]=firstLine[i];
                    }
                    break;
                }
                while((s=raf.readLine())!=null){
                    arrayListString.add(s);
                    nowLine++;
                    if(nowLine%2000==0){
                        System.out.println(System.currentTimeMillis()-startTime);
                        for(String data:arrayListString){
                            //提交到es
                            ret=dataToMap(data);
                            bulkRequest.add(Es_Client.getTransportClient().prepareIndex(index, type).setId(data.split("\\|")[0]).setSource(ret));
                        }
//                        bulkRequest.setRefresh(true);
                        bulkRequest.execute().actionGet();
                        bulkRequest.request().requests().clear();     //这一步要清理，不然插入的条数不对，貌似bulkRequest里面的数据是一直累加的，所以每执行一次要清理一次
                        arrayListString= new ArrayList<String>(2000);
                        long endTime = System.currentTimeMillis();    //获取结束时间
                    }
                }
            } finally{
                raf.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

    }

    public static Map<String,Object> dataToMap(String data){
        String[] dataList= data.split("\\|");
        Map<String, Object> ret = new HashMap<String, Object>();
        if(dataList.length!=501){
            if(dataList.length==0){
                return null;
            }
            System.out.println(dataList.length);
            throw new RuntimeException("单行格式不符合要求");
        }
        for(int f=0;f<COLUMNS.length;f++){
            ret.put(COLUMNS[f],dataList[f+1]);
        }
        return ret;
    }

    public static String generateLines(int i) {
        StringBuffer sb = new StringBuffer();
        sb.append(i+SPLITTER);
        //添加数据

        final char[] CHARS = { '`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', ']', '{', '}', '\\', ';', ':', '\'', '"', ',', '<', '.', '>', '/', '?','0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
        for(int fuzzy = 0;fuzzy<100;fuzzy++) {
            char c1=CHARS[new Random().nextInt(96)+1];
            char c2=CHARS[new Random().nextInt(96)+1];
            char c3=CHARS[new Random().nextInt(96)+1];
            char c4=CHARS[new Random().nextInt(96)+1];
            sb.append(c1);
            sb.append(c2);
            sb.append(c3);
            sb.append(c4);
            sb.append(SPLITTER);
        }

        for(int range = 0;range<100;range++) {
            int c1 = (int)(Math.random()*9000+1000);
            sb.append(c1);
            sb.append(SPLITTER);

        }
        for(int flag = 0;flag<299;flag++) {
            int c1 = (int)(Math.random()+0.5);
            sb.append(c1);
            sb.append(SPLITTER);
        }
        sb.append((int)(Math.random()+0.5));
        sb.append("\n");
        return sb.toString();
    }


}
