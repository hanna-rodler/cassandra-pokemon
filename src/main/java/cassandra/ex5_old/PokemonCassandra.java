package cassandra.ex5_old;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.session.Session;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableWithOptions;
import com.datastax.oss.driver.shaded.guava.common.collect.ImmutableMap;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URL;
import java.time.Duration;
import java.util.*;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.*;

public class PokemonCassandra {

    // DB Session to cassandra
    private static Session session;
    public static void main(String[] args) {
        List<Pokemon> pokemon = new ArrayList<Pokemon>();
        URL resource = Pokemon.class.getClassLoader().getResource("Pokemon.csv");
        try (Scanner scanner = new Scanner(new File(resource.toURI()))) {
            // catch
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(",");
                if(!split[0].equals("#")) {
                    pokemon.add(
                            new Pokemon(Integer.parseInt(split[0]), split[1], split[2], split[3], Integer.parseInt(split[4]),
                                    Integer.parseInt(split[5]), Integer.parseInt(split[6]), Integer.parseInt(split[7]),
                                    Integer.parseInt(split[8]), Integer.parseInt(split[9]), Integer.parseInt(split[10]),
                                    Integer.parseInt(split[11]), Boolean.parseBoolean(split[12])));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // connect to server
        try(CqlSession session = CqlSession.builder()
                .withConfigLoader(DriverConfigLoader.programmaticBuilder()
                    .withDuration(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofMillis(60000))
                    .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofMillis(60000))
                    .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(15000)).build())
                .addContactPoint(new InetSocketAddress("localhost", 9042))
                        .withLocalDatacenter("DC1").build()) {
            ResultSet rs = session.execute("SELECT release_version FROM system.local");
            /*Row row = rs.one();*/
            /*System.out.println(row.getString("release_version")); // oben release_version, deswegen kriegt hier auch release_version als Antwort column zurück*/
            ResultSet execute = session.execute("DESCRIBE keyspaces;");
            execute.forEach(x -> {
                System.out.println(x.getFormattedContents());
            });

            CreateKeyspace newKeySpace = createKeyspace("big_data_pokemon_trainer")
                    .ifNotExists().withNetworkTopologyStrategy(ImmutableMap.of("DC1", 4));
            session.execute(newKeySpace.build());

            session.execute(dropTable("big_data_pokemon", "pokemon").ifExists().build());


            // track pokemon process table <- level, XP points and training sessions completed
            CreateTableWithOptions createTrainerPokemon = createTable("big_data_pokemon", "pokemon")
                    .withPartitionKey("trainer_id", DataTypes.UUID)
                    .withClusteringColumn("pokemon_id", DataTypes.INT)
                    .withColumn("pokemon_name", DataTypes.TEXT)
                    .withColumn("level", DataTypes.INT)
                    .withColumn("type_2", DataTypes.TEXT)
                    .withColumn("xp", DataTypes.INT)
                    .withColumn("completed_training_sessions", DataTypes.INT)
                    .withCompaction(leveledCompactionStrategy())
                    .withSnappyCompression()
                    .withClusteringOrder("pokemon_id", ClusteringOrder.DESC);
            session.execute(createTrainerPokemon.build());

            System.out.println(createTrainerPokemon.asCql()); // nur für uns zum anschauen


            // have multiple Pokemons of same species with each their own training history



            // see which Pokemon types I have trained the most + avg improvement stats

            // create table Pokemon
            // use DAO
            // zuerst muss Mapper geschrieben werden
            TrainerPokemonMapper mapper = new TrainerPokemonMapperBuilder(session).build();
            TrainerPokemonDao trainerDao = mapper.trainerPokemonDao(CqlIdentifier.fromCql("big_data_pokemon"));

            List<TrainerPokemon> trainerPokemonList = new ArrayList<>();
            trainerPokemonList.add(new TrainerPokemon(
                    UUID.randomUUID(),
                    174,
                    "Igglybuff",
                    5,
                    1234,
                    10
            ));
            trainerPokemonList.add(new TrainerPokemon(
                    UUID.randomUUID(),
                    181,
                    "Ampharos",
                    2,
                    200,
                    1

            ));
            trainerPokemonList.add(new TrainerPokemon(
                    UUID.randomUUID(),
                    9,
                    "Blastoise",
                    6,
                    2100,
                    12
            ));

            UUID trainerId = UUID.randomUUID();
            int pokemonId = 9;
            String pokemonName = "Blastoise";
            int newLevel = 2;
            int newXp = 2100;
            int completedTrainingSessions = 13;


            for (TrainerPokemon p: trainerPokemonList) {
                trainerDao.save(p);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
