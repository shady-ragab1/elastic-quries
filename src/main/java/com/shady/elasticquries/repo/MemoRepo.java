package com.shady.elasticquries.repo;

import com.shady.elasticquries.model.Memo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MemoRepo extends ElasticsearchRepository<Memo,String> {
}
