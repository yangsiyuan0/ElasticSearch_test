package cn.yang.elasticSearch;

import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

/**
 * 索引相关操作
 * @author yang
 *
 */
public class IndexDemo {

	/**
	 * 建立索引：【不会自动创建映像】
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception{
		// 创建连接搜索服务器对象
		Client client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		// 建立索引blog2 【此种方法建立索引，不会自动创建镜像】
		client.admin().indices().prepareCreate("blog2").get();
		// 关闭连接
		client.close();
	}
	
	/**
	 * 删除索引
	 * @throws Exception 
	 */
	@Test
	public void test2() throws Exception{
		// 创建连接搜索服务器对象
		Client client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		// 建立索引blog2 【此种方法建立索引，不会自动创建镜像】
		client.admin().indices().prepareDelete("blog2").get();
		// 关闭连接
		client.close();
	}
}
