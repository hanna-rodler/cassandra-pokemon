package cassandra.ex5;

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

    @DaoFactory
    TrainingSessionDao trainingSessionDao(@DaoKeyspace CqlIdentifier keyspace);

    @DaoFactory
    TrainerStatsDao trainerStatsDao(@DaoKeyspace CqlIdentifier keyspace);

    @DaoFactory
    HighestTrainerStatsDao highestTratinerStatsDao(@DaoKeyspace CqlIdentifier keyspace);
}
