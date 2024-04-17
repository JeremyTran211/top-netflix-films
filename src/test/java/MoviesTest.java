import org.example.Movies;
import org.testng.Assert;
import org.testng.annotations.Test;


public class MoviesTest {
    @Test
    public void testMovies() {
        Movies mymovies = new Movies();
        String[] movieTitles = mymovies.getMovies();

        Assert.assertEquals(3974, movieTitles.length);
    }
}
