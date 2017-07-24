package cn.yang.es.test;

import java.util.Iterator;
import java.util.List;

/**
 * 测试文档的增删该查
 */
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.yang.es.domain.Article;
import cn.yang.es.service.IElasticServcie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ESTest {

	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	IElasticServcie elasticsearchService;
	/**
	 * 进行index创建 + mapping添加
	 */
	@Test
	public void createIndexTest(){
		// 创建index
		elasticsearchTemplate.createIndex(Article.class);
		// 添加mapping
		elasticsearchTemplate.putMapping(Article.class);
	}
	
	/**
	 * 增加文档
	 */
	@Test
	public void saveTest(){
		Article article = new Article();
		article.setId(1001);
		article.setTitle("权力的游戏");
		article.setContent("是一部中世纪史诗奇幻题材的美国电视连续剧,"
				+ "美国作家乔治·R·R·马丁的奇幻文学《冰与火之歌》系列作为基础改编创作，"
				+ "由大卫·贝尼奥夫和D·B·魏斯（D. B. Weiss）编剧执笔");
		elasticsearchService.save(article);
	}
	
	/**
	 * 删除文档
	 */
	@Test
	public void deleteTest(){
		Article article = new Article();
		article.setId(1001);
		elasticsearchService.delete(article);
	}
	
	/**
	 * 查找文档：单个
	 */
	@Test
	public void findOneTest(){
		Article article = elasticsearchService.findOne(1001);
		System.out.println(article);
	}
	
	/**
	 * 查找文档：所有
	 */
	@Test
	public void findAllTest(){
		Iterable<Article> iterable = elasticsearchService.findAll();
		for (Article article : iterable) {
			System.out.println(article);
		}
	}
	
	/**
	 * 循环存储多个文档
	 */
	@Test
	public void SaveBatchTest(){
		for (int i = 1; i <= 100; i++) {
			Article article = new Article();
			article.setId(i);
			article.setTitle(i + "权力的游戏");
			article.setContent(i
					+ "目前HBO已播映六季，第六季已于2016年6月26日结束。第七季将在2017年7月16日播映");
			elasticsearchService.save(article);
		}
	}
	
	/**
	 * 查找文档：分页
	 */
	@Test
	public void sortAndPageQueryTest(){
		Pageable pageable = new PageRequest(0, 10);
		Page<Article> pageData = elasticsearchService.findAll(pageable);
		for (Article article : pageData) {
			System.out.println(article);
		}
	}
	
	/**
	 * 查找文档：条件查询
	 */
	@Test
	public void conditionQueryQueryTest(){
		Pageable pageable = new PageRequest(0, 10);
		Page<Article> pageData = elasticsearchService.findByTitle("之歌",pageable);
		for (Article article : pageData) {
			System.out.println(article);
		}
	}
	
}
