package com.my.kb.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface KnowledgeBaseRepository /*extends MongoRepository<KnowledgeBase, String>*/ {
    /*@Override
    List<KnowledgeBase> findAll();


    @Query("{$or: [ { title: { $regex: ?0 } }, { description: { $regex: ?0 } }, { tag: { $regex: ?0 } } ] })")
    List<KnowledgeBase> findBySearchString(String search);

    //void update(KnowledgeBase base);

    @Query(value = "{ most_searched : { $gte : 5 }, $limit: 3 }")
    List<KnowledgeBase> findByMostSearched();*/
}
