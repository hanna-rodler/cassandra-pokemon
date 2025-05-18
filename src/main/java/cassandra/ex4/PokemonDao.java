package cassandra.ex4;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

import java.util.List;

@Dao
public interface PokemonDao {

    // Insert & update in Cassandra synonym
    @Insert
    void save(Pokemon pokemon);

    @Delete
    void delete(Pokemon pokemon);

    @Select(customWhereClause = "legendary = true")
    PagingIterable<Pokemon> getLegendary();
}
