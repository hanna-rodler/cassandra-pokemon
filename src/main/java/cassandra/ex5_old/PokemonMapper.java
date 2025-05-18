package cassandra.ex5_old;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface PokemonMapper {
    @DaoFactory
    PokemonDao pokemonDao(@DaoKeyspace CqlIdentifier keyspace);

    @DaoFactory
    TrainerPokemonDao trainerPokemonDao(@DaoKeyspace CqlIdentifier keyspace);
}
