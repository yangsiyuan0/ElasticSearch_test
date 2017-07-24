package cn.yang.elasticSearch;

import java.net.InetAddress;
import java.util.Iterator;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

/**
 * 各种查询对象 Query  的使用
 * 1. TermQuery 词条查询
 * 2. WildcardQuery 模糊查询
 * 3. FuzzyQuery 相似度查询
 * 4. BooleanQuery 布尔查询
 * @author yang
 *
 */
public class QueryDemo {

	/**
	 * 所有字段进行分词查询【queryStringQuery】
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception{
		Client client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		// 搜索数据
		SearchResponse searchResponse = client.prepareSearch("blog1").setTypes("article")
				.setQuery(QueryBuilders.queryStringQuery("爸爸")).get(); 
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
	
	/**
	 * 对content进行模糊查询【wildcardQuery】
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception{
		Client client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		// 搜索数据
		SearchResponse searchResponse = client.prepareSearch("blog1").setTypes("article")
				.setQuery(QueryBuilders.wildcardQuery("content", "*爸爸*")).get(); 
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
	
	/**
	 * 词条搜索【TermQuery】：ElasticSearch的默认分词技术，将词条分成了一个一个的字，因此单个字可以查询到结果，两字词语却无法获取结果
	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception{
		Client client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		// 搜索数据
		SearchResponse searchResponse = client.prepareSearch("blog1").setTypes("article")
				.setQuery(QueryBuilders.termQuery("content", "搜索")).get();  // 使用单个字"搜"、"索"可以查询到结果
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
