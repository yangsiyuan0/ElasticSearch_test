package cn.yang.elasticSearch;

import java.net.InetAddress;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

/**
 * 映射的相关操作
 * @author yang
 *
 */
public class MappingDemo {

	/**
	 * 添加映射
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception{
		// 创建连接搜索服务器对象
		Client client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		// 添加映射
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
				.startObject("article")
					.startObject("properties")
						.startObject("id").field("store","yes").field("type","integer").endObject()
						.startObject("title").field("store","yes").field("type","string").field("analyzer","ik").endObject()
						.startObject("content").field("store","yes").field("type","string").field("analyzer","ik").endObject()
						.endObject()
					.endObject()
				.endObject();
		PutMappingRequest mapping = Requests.putMappingRequest("blog2").type("article").source(builder);
		client.admin().indices().putMapping(mapping).get();
		// 关闭连接
		client.close();
	}
}
