package com.elasticsearch.test;

import org.apache.lucene.queryparser.xml.FilterBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.index.query.QueryFilterBuilder;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

//import org.elasticsearch.common.settings.ImmutableSettings;

/**
 * Created by cc on 2016/4/4.
 */
public class Es_Client {
    public static final String CLUSTER_NAME ="razor_es"; //实例名称
    private static final String IP = "10.240.44.40";
    private static final int PORT = 9300;  //端口
    //1.设置集群名称：默认是elasticsearch，并设置client.transport.sniff为true，使客户端嗅探整个集群状态，把集群中的其他机器IP加入到客户端�?
    //对ES2.0有效
    private static Settings settings = Settings
            .settingsBuilder()
            .put("cluster.name", CLUSTER_NAME)
            .put("client.transport.sniff", true)
            .put("client.transport.ping_timeout", "120s")
            .build();

    //创建私有对象
    private static TransportClient client;


    static {
        try {
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP), PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //取得实例
    public static  TransportClient getTransportClient() {
        return client;
    }

    public static void main(String[] args)  throws Exception{
        queryData();

    }

    public static void queryData(){

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("s0","10"));
        queryBuilder.must(QueryBuilders.termQuery("s1","20"));

        SearchResponse response = Es_Client.getTransportClient().prepareSearch("razor_user_history")
                .setTypes("test")
                .setQuery(queryBuilder)
                .setSize(5).setFrom(0)
                .execute()
                .actionGet();

        System.out.println(response);

        SearchHits hits = response.getHits();
        for (int i = 0; i < hits.getHits().length; i++) {
            System.out.println(hits.getHits()[i].getSourceAsString());}
    }

    public static void filterData(){
        /*FilterBuilder filterBuilder = FilterBuilders.boolFilter()
                .must(FilterBuilders.termFilter("name", "葫芦1493娃"))
                .mustNot(FilterBuilders.rangeFilter("age").from(1000).to(3000))
                .should(FilterBuilders.termFilter("home", "山西省太原市7077街道"));
*/
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("s0","10"));
        queryBuilder.must(QueryBuilders.termQuery("s1","20"));

        SearchResponse response = Es_Client.getTransportClient().prepareSearch("bigdata")
                .setTypes("student")
                .setPostFilter(QueryBuilders.termQuery("s0","10"))
                .setPostFilter(QueryBuilders.termQuery("s1","20"))
                .setQuery(queryBuilder)                            //加还是不加呢？？？
                .setSize(5).setFrom(0)
                .execute()
                .actionGet();
        System.out.println(response);



    }


}
