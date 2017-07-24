package cn.yang.elasticSearch;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.junit.Test;

import cn.yang.domain.Article;

/**
 * 查询结果高亮显示【利用<em标签>】
 * @author yang
 *
 */
public class HighlightQueryDemo {

	/**
	 * 查询结果高亮显示【利用<em>标签，装得好像真的浏览器一样】
	 * @throws Exception 
	 */
	@Test
	public void test() throws Exception{
		// 1. 创建连接搜索服务器对象
		TransportClient client = TransportClient.builder().build()
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		ObjectMapper objectMapper = new ObjectMapper();
		// 2. 搜索数据
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("blog2").setTypes("article").setQuery(QueryBuilders.termQuery("title", "日志"));
		// 3. 设置高亮
		searchRequestBuilder.addHighlightedField("title"); // 对title字段进行高亮
		searchRequestBuilder.setHighlighterPreTags("<em>"); //前置元素
		searchRequestBuilder.setHighlighterPostTags("<em>"); //后置元素
		// 4. 查询
		SearchResponse searchResponse = searchRequestBuilder.get();
		// 5. 处理结果数据
		SearchHits searchHits = searchResponse.getHits();
		System.out.println("共获得查询结果:" + searchHits.getTotalHits() + "条");
		Iterator<SearchHit> iterator = searchHits.iterator();
		while(iterator.hasNext()){ // 用高亮处理后的内容替换原内容
			SearchHit searchHit = iterator.next();
			Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
			HighlightField titleField = highlightFields.get("title");
			Text[] texts = titleField.fragments();
			String title = "";
			for (Text text : texts) { //拼接高亮片段
				title += text;
			}
			// 转换查询结果
			Article article = objectMapper.readValue(searchHit.getSourceAsString(),Article.class);
			article.setTitle(title); //用高亮内容替换原内容
			System.out.println(title);
		}
		// 关闭连接
		client.close();
	}
}
