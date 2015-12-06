package com.example.evindj.popularmovies;
import com.example.evindj.popularmovies.movie.Movie;
import com.example.evindj.popularmovies.movie.MovieService;
import com.example.evindj.popularmovies.movie.SortOrder;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.lang.System;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by evindj on 12/5/15.
 */
public class MovieServiceTest {
    private MovieService movieService;
    private Object parameters;
    @Test
    public void serviceSendsTheRequest() throws Exception {
       movieService = MovieService.getInstance();
       Method method = movieService.getClass().getDeclaredMethod("sendGet");
        method.setAccessible(true);
      Object res ="";
       // res = method.invoke(movieService);

        ArrayList<Movie> movies =(ArrayList<Movie>) movieService.getMovies(SortOrder.HIGHESTRATED);
        Assert.assertEquals(movies.size(),23);
    }

}
