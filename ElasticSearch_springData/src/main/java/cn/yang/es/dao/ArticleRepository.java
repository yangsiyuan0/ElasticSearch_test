package cn.yang.es.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import cn.yang.es.domain.Article;

public interface ArticleRepository extends ElasticsearchRepository<Article, Integer>{

}
