import org.example.Movie;
import org.example.Movies;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;


public class MoviesTest {
    @Test
    public void testMovies() {
        Movies mymovies = new Movies();
        List<Movie> movieTitles = mymovies.getMovies();

        Assert.assertEquals(3982, movieTitles.size());
    }
}
