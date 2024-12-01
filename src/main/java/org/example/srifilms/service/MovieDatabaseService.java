package org.example.srifilms.service;

import org.example.srifilms.entities.Movie;
import org.example.srifilms.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieDatabaseService {

    @Autowired
    private MovieRepository movieRepository;


    public Optional<Movie> getMovieByFileName(String fileName) {
        return movieRepository.findById(fileName);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}
