package cn.yang.elasticSearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

import cn.yang.domain.Article;

/**
 * 查询文档分页操作
 * @author yang
 *
 */
public class PageQueryDemo {

	/**
	 * 循环建立100个文档,进行分页查询
	 * @throws Exception 
	 */
	
	@Test
	public void test1() throws Exception{
		// 1. 创建连接搜索服务器对象
		TransportClient client = TransportClient.builder().build()
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		ObjectMapper objectMapper = new ObjectMapper();
		// 2. 循环建立文档
		for(int i=1;i <= 100;i++){
			Article article = new Article();
			article.setId(i);
			article.setTitle("第" + i + "个标题");
			article.setContent("第" + i + "个正文：假装自己时正文，多写一点");
			// 建立文档
			client.prepareIndex("blog2","article2",article.getId().toString()).setSource(objectMapper.writeValueAsString(article)).get();
		}
		// 3. 分页查询【searchRequestBuilder】
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("blog2").setTypes("article2").setQuery(QueryBuilders.matchAllQuery());
		// 设置分页数据：第二页，每页20条
		SearchResponse searchResponse = searchRequestBuilder.setFrom(2).setSize(20).get();
		// 4. 处理查询结果:获取命中次数
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
