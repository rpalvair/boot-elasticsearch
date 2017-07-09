package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.domain.User;
import com.palvair.elasticsearch.presentation.SearchResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class UserService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public UserService(final ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public SearchResult<User> searchExactly(final String value) {

        final BoolQueryBuilder query = boolQuery().must(
                boolQuery().should(matchQuery("nom", value))
                        .should(matchQuery("prenom", value))
        );

        final NativeSearchQuery builder = new NativeSearchQueryBuilder()
                .withIndices(IndexName.USER.getName())
                .withTypes(TypeName.USER.getName())
                .withQuery(query).build();

        return elasticsearchTemplate.query(builder, this::extractResults);
    }

    public SearchResult<User> searchApproximately(final String value) {

        final BoolQueryBuilder query = boolQuery().must(
                boolQuery().should(matchQuery("nom.autocomplete", value).analyzer("query_autocomplete"))
        );

        final NativeSearchQuery builder = new NativeSearchQueryBuilder()
                .withIndices(IndexName.USER.getName())
                .withTypes(TypeName.USER.getName())
                .withQuery(query).build();

        return elasticsearchTemplate.query(builder, this::extractResults);
    }

    public SearchResult<User> getAll() {
        final MatchAllQueryBuilder query = QueryBuilders.matchAllQuery();

        final NativeSearchQuery builder = new NativeSearchQueryBuilder()
                .withIndices(IndexName.USER.getName())
                .withTypes(TypeName.USER.getName())
                .withQuery(query).build();

        return elasticsearchTemplate.query(builder, this::extractResults);
    }

    private SearchResult<User> extractResults(final SearchResponse searchResponse) {
        List<User> users = Arrays.stream(searchResponse.getHits().getHits())
                .map(SearchHit::getSource)
                .map(this::getMapSearchResultFunction)
                .collect(Collectors.toList());
        return new SearchResult<>(users);
    }

    private User getMapSearchResultFunction(final Map<String, Object> map) {
        return new User((String) map.get("nom"), (String) map.get("prenom"));
    }


}
