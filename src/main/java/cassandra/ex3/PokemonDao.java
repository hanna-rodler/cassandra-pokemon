package cassandra.ex3;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface PokemonDao {

    // Insert & update in Cassandra synonym
    @Insert
    void save(Pokemon pokemon);

    @Delete
    void delete(Pokemon pokemon);

    @Select(customWhereClause = "generation = :generation AND legendary = false")
    PagingIterable<Pokemon> findNonLegendaryByGeneration(int generation);
}
