package team15.potato.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team15.potato.api.MovieListApi;

@Service
@RequiredArgsConstructor
public class MovieListApiService {
    private final MovieListApi movieListApi;

    public void insertMovie() {
        movieListApi.movieList();
    }
}
