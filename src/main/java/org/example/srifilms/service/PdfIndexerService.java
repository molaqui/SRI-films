package org.example.srifilms.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import org.example.srifilms.DataExtracteur.PdfContentExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class PdfIndexerService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private static final String INDEX_NAME = "pdf_files";

    public void indexPdfFiles(String folderPath) throws IOException {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path: " + folderPath);
        }

        for (File file : folder.listFiles((dir, name) -> name.endsWith(".pdf"))) {
            String content = PdfContentExtractor.extractTextFromPdf(file);
            indexPdfFile(file.getName(), content);
        }
    }

    private void indexPdfFile(String fileName, String content) throws IOException {
        elasticsearchClient.index(IndexRequest.of(i -> i
                .index(INDEX_NAME)
                .id(fileName) // Le nom du fichier comme identifiant unique
                .document(Map.of("content", content))
        ));
    }
}
