package cassandra.part2;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;

@Mapper
public interface PokemonMapper {
    @DaoFactory
    PokemonDao pokemonDao(@DaoKeyspace CqlIdentifier keyspace);
}
