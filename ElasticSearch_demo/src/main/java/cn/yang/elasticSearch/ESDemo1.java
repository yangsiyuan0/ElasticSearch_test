package cn.yang.elasticSearch;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

public class ESDemo1 {
	/**
	 * 直接在ES中建立文档：自动创建索引 + 索引映射
	 * 
	 * @throws Exception
	 */
	@Test
	public void demo1() throws Exception {
		// 创建连接搜索服务器对象
		Client client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		// 描述JSON数据
		/*XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("id", 1).field("title", "我是你爸爸")
				.field("content", "我也很绝望啊，杨兵非要叫我爸爸，那我能怎么办，当然是原谅他并且接收咯").endObject();*/
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("id", 2).field("title", "ElasticSearch是一个基于Lucene的搜索服务器")
				.field("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。"
						+ "Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。"
						+ "设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便")
				.endObject();
		// 建立文档对象
		client.prepareIndex("blog1", "article", "2").setSource(builder).get();
		// 关闭连接
		client.close();
	}

	/**
	 * 搜索文档数据
	 * 
	 * @throws IOException
	 */
	@Test
	public void demo2() throws IOException {
		Client client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		// 搜索数据
		SearchResponse searchResponse = client.prepareSearch("blog1").setTypes("article")
				.setQuery(QueryBuilders.matchAllQuery()).get();
		//处理查询结果:获取命中次数
		SearchHits hits = searchResponse.getHits();
		System.out.println("共获得查询结果:" + hits.getTotalHits() + "条");
		// 遍历所有结果
		Iterator<SearchHit> iterator = hits.iterator();
		while(iterator.hasNext()){
			SearchHit searchHit = iterator.next();
			System.out.println(searchHit.getSourceAsString());
			System.out.println("title:"+searchHit.getSource().get("title"));
		}
		client.close();
	}
}
