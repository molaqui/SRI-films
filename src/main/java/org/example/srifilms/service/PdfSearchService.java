package org.example.srifilms.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfSearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private static final String INDEX_NAME = "pdf_files";

    public List<String> searchFilesByContent(String query) throws IOException {
        // Diviser la requête en plusieurs termes
        String[] terms = query.split("\\s+");
        for (String term : terms) {
            System.out.println(term);
        }


        Query boolQuery = Query.of(q -> q
                .bool(b -> b
                        .should(Arrays.stream(terms).map(term ->
                                Query.of(qt -> qt
                                        .wildcard(w -> w
                                                .field("content")
                                                .value("*" + term + "*") // Correspondance partielle
                                                .caseInsensitive(true)  // Recherche insensible à la casse
                                        )
                                )
                        ).toList())
                )
        );

        // Exécuter la recherche
        SearchResponse<Object> response = elasticsearchClient.search(
                SearchRequest.of(s -> s
                        .index(INDEX_NAME)
                        .query(boolQuery)
                ),
                Object.class
        );

        // Retourne une liste unique de noms de fichiers
        return response.hits().hits().stream()
                .map(hit -> hit.id())
                .distinct()
                .collect(Collectors.toList());
    }
}
