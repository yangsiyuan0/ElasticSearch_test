package cn.yang.elasticSearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import cn.yang.domain.Article;

/**
 * 文档相关操作
 * @author yang
 *
 */
public class DocumentDemo {

	/**
	 * 直接构建JSON数据，建立文档
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception{
		// 创建连接搜索服务器对象
		TransportClient client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		// 直接在 XContentBuilder 中构建 json 数据，建立文档
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("id",1).field("title","ElasticSearch是一个基于Lucene的搜索服务器")
				.field("content","它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。"
						+ "Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。"
						+ "设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便")
				.endObject();
		client.prepareIndex("blog2","article","1").setSource(builder).get();
		// 关闭连接
		client.close();
	}
	
	/**
	 * 对已经存在的POJO对象，转化为JSON然后建立文档
	 * @throws Exception 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@Test
	public void test2() throws Exception{
		// 创建连接搜索服务器对象
		TransportClient client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		// 建立文档
		ObjectMapper objectMapper = new ObjectMapper();
		Article article = new Article();
		article.setId(1001);
		article.setTitle("集中式日志系统 ELK");
		article.setContent("ELK 其实并不是一款软件，而是一整套解决方案，是三个软件产品的首字母缩写，"
				+ "Elasticsearch，Logstash 和 Kibana。这三款软件都是开源软件，通常是配合使用，而且又先后归于 Elastic.co 公司名下，故被简称为 ELK 协议栈");
		client.prepareIndex("blog2","article",article.getId().toString()).setSource(objectMapper.writeValueAsString(article)).get();
		// 关闭连接
		client.close();
		
	}
	
	/**
	 * 修改文档
	 * @throws Exception 
	 */
	@Test
	public void test3() throws Exception{
		// 创建连接搜索服务器对象
		TransportClient client = TransportClient.builder().build()
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		ObjectMapper objectMapper = new ObjectMapper();
		Article article = new Article();
		article.setId(1001);
		article.setTitle("集中式日志系统 ELK");
		article.setContent("在我们日常生活中，我们经常需要回顾以前发生的一些事情；或者，当出现了一些问题的时候，"
				+ "可以从某些地方去查找原因，寻找发生问题的痕迹。无可避免需要用到文字的、图像的等等不同形式的记录。用计算机的术语表达，就是 LOG，或日志");
		// 修改文档方法一：【使用prepareUpdate】
		// client.prepareUpdate("blog2","article",article.getId().toString()).setDoc(objectMapper.writeValueAsString(article)).get();
		
		// 修改文档方法二：【直接使用update】
		client.update(new UpdateRequest("blog2","article",article.getId().toString()).doc(objectMapper.writeValueAsString(article))).get();
		// 关闭连接
		client.close();
	}
	
	/**
	 * 删除文档
	 * @throws Exception 
	 */
	@Test
	public void test4() throws Exception{
		// 创建连接搜索服务器对象
		TransportClient client = TransportClient.builder().build()
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		Article article = new Article();
		article.setId(1001);
		// 删除文档方法一：【使用prepareDelete】
		// client.prepareDelete("blog2","article",article.getId().toString()).get();
		// 删除文档方法二：【直接使用Delete】
		client.delete(new DeleteRequest("blog2","article",article.getId().toString())).get();
	}
}
