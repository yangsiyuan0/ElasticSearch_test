package cn.yang.es.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.yang.es.domain.Article;

public interface IElasticServcie {

	public void save(Article article);

	public void delete(Article article);

	public Article findOne(int id);

	public Iterable<Article> findAll();

	public Page<Article> findAll(Pageable pageable);

	public Page<Article> findByTitle(String string, Pageable pageable);

	
}
