package org.example.srifilms.controller;

import org.example.srifilms.entities.Movie;
import org.example.srifilms.service.MovieDatabaseService;
import org.example.srifilms.service.PdfIndexerService;
import org.example.srifilms.service.PdfSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MovieController {

    @Autowired
    private PdfSearchService pdfSearchService;

    @Autowired
    private MovieDatabaseService movieDatabaseService;
    @Autowired
    private PdfIndexerService pdfIndexerService;


    //indexation
    @GetMapping("/index")
    public String indexPdfFiles() {
        try {

            String folderPath = "C:\\Users\\User\\Documents\\test\\Projet-SRI\\Projet\\SRI-Films\\src\\main\\resources\\movies\\pdf";
            pdfIndexerService.indexPdfFiles(folderPath);
            return "Indexation des fichiers PDF terminée avec succès.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de l'indexation des fichiers PDF : " + e.getMessage();
        }
    }

    @GetMapping("/search")
    public List<Movie> searchMovies(@RequestParam String query) throws IOException {

        // Étape 1 : Rechercher les noms de fichiers correspondant à la requête

        List<String> fileNames = pdfSearchService.searchFilesByContent(query);

        //  all-falll-form.pdf

      for (String fileName : fileNames) {
          System.out.println(fileName);
      }

        // Étape 2 : Récupérer les détails des films à partir de la base de données
        return fileNames.stream()
                .map(fileName -> movieDatabaseService.getMovieByFileName(fileName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
    @GetMapping("/movie/{fileName}")
    public Movie getMovie(@PathVariable String fileName) {
        return movieDatabaseService.getMovieByFileName(fileName).orElse(null);
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return movieDatabaseService.getAllMovies();
    }
}

