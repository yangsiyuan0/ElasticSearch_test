package cn.yang.es.service;


import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.yang.es.dao.ArticleRepository;
import cn.yang.es.domain.Article;

@Service
public class ElasticServiceImpl implements IElasticServcie{

	@Autowired 
	ArticleRepository articleRepository;
	
	public void save(Article article) {
		articleRepository.save(article);
	}

	public void delete(Article article) {
		articleRepository.delete(article);
	}

	public Article findOne(int id) {
		return articleRepository.findOne(id);
	}

	public Iterable<Article> findAll() {
		return articleRepository.findAll();
	}

	public Page<Article> findAll(Pageable pageable) {
		return articleRepository.findAll(pageable);
	}

	public Page<Article> findByTitle(String term, Pageable pageable) {
		
		return articleRepository.search(QueryBuilders.termQuery("title", term), pageable);
	}


}
