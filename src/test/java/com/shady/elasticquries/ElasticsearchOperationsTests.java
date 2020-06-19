package com.shady.elasticquries;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shady.elasticquries.model.Memo;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.pivovarit.function.ThrowingFunction.unchecked;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ElasticsearchOperationsTests {

	@Autowired
	ElasticsearchOperations elasticsearchOperations;

	@Test
	void test_elasticsearchOperations() {
		Memo memo = new Memo("3","desc","message");

		IndexQuery indexQuery = new IndexQueryBuilder()
				.withId(memo.getId())
				.withObject(memo)
				.build();
		String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of("memo"));


		assertEquals("3",documentId);
		assertEquals(3,elasticsearchOperations.count(Query.findAll(),Memo.class));


		memo = elasticsearchOperations.get("3",Memo.class,IndexCoordinates.of("memo"));
		assertEquals("3",memo.getId());


		SearchHits<Memo> searchHits = elasticsearchOperations
				.search(Query.findAll(),Memo.class,IndexCoordinates.of("memo"));

		assertEquals(3,searchHits.getTotalHits());
		assertEquals(3,searchHits.getSearchHits().size());
		assertTrue(searchHits.getSearchHit(0).getContent() instanceof  Memo);

	}



	@Autowired
	RestHighLevelClient restHighLevelClient;

	@Test
	void test_restHighLevelClient() throws IOException {

		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(QueryBuilders.matchAllQuery());
		builder.sort(new FieldSortBuilder("id").order(SortOrder.DESC));

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("memo");
		searchRequest.source(builder);


		SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);

		assertEquals(200,response.status().getStatus());

		ObjectMapper objectMapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //to ignore


		List<Memo> memos = Arrays.stream(response.getHits().getHits())
				.map(unchecked(hit->objectMapper.readValue(hit.getSourceAsString(),Memo.class))) //lampda checked exception
				.collect(Collectors.toList());

		assertTrue(Integer.valueOf(memos.get(0).getId())  > Integer.valueOf(memos.get(1).getId()) );







	}

	@Test
	public void test_elasticsearchOperations_queries(){
		Query query =new NativeSearchQueryBuilder()
				.withSort(SortBuilders.fieldSort("id")
						.order(SortOrder.DESC))
				.withQuery(QueryBuilders.matchAllQuery()).build();

		SearchHits<Memo> searchHits = elasticsearchOperations.search(query,Memo.class,IndexCoordinates.of("memo"));

		assertEquals("3",searchHits.getSearchHits().get(0).getContent().getId());

	}


}
