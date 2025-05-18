package cassandra.ex3;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
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

            CreateKeyspace newKeySpace = createKeyspace("big_data_pokemon")
                    .ifNotExists().withNetworkTopologyStrategy(ImmutableMap.of("DC1", 3));
            session.execute(newKeySpace.build());

            session.execute(dropTable("big_data_pokemon", "pokemon").ifExists().build());

            // create table Pokemon
            CreateTableWithOptions create = createTable("big_data_pokemon", "pokemon")
                    .withPartitionKey("generation", DataTypes.INT)
                    .withPartitionKey("legendary", DataTypes.BOOLEAN)
                    .withClusteringColumn("type", DataTypes.TEXT)
                    .withClusteringColumn("type_2", DataTypes.TEXT)
                    .withClusteringColumn("id", DataTypes.INT)
                    .withColumn("type", DataTypes.TEXT)
                    .withColumn("name", DataTypes.TEXT)
                    .withColumn("total", DataTypes.INT)
                    .withColumn("hp", DataTypes.INT)
                    .withColumn("attack", DataTypes.INT)
                    .withColumn("defense", DataTypes.INT)
                    .withColumn("speed_attack", DataTypes.INT)
                    .withColumn("speed_defence", DataTypes.INT)
                    .withColumn("speed", DataTypes.INT)
                    .withCompaction(leveledCompactionStrategy())
                    .withSnappyCompression()
                    .withClusteringOrder("type", ClusteringOrder.DESC)
                    .withClusteringOrder("type_2", ClusteringOrder.DESC)
                    .withClusteringOrder("id", ClusteringOrder.DESC);
            session.execute(create.build());

            System.out.println(create.asCql()); // nur für uns zum anschauen


            // use DAO
            // zuerst muss Mapper geschrieben werden
            PokemonMapper mapper = new PokemonMapperBuilder(session).build();
            PokemonDao dao = mapper.pokemonDao(CqlIdentifier.fromCql("big_data_pokemon"));
            int maxGeneration = 0;

            for (Pokemon p: pokemon) {
                dao.save(p);
                int gen = p.getGeneration();
                if (gen > maxGeneration) {
                    maxGeneration = gen;
                }
            }


            printAverageStatsByGeneration(dao, maxGeneration);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void printAverageStatsByGeneration(PokemonDao dao, int maxGeneration) {
        Map<Integer, PokemonCassandra.StatsAccumulator> generationStats = new TreeMap<>(); // TreeMap to keep sorted order by generation

        for (int gen = 1; gen <= maxGeneration; gen++) {
            PagingIterable<Pokemon> pokemons = dao.findNonLegendaryByGeneration(gen);

            // skip if generation is empty
            if (!pokemons.iterator().hasNext()) {
                continue;
            }

            for (Pokemon p : pokemons) {
                PokemonCassandra.StatsAccumulator acc = generationStats.computeIfAbsent(gen, k -> new PokemonCassandra.StatsAccumulator());
                acc.add(p);
            }
        }

        System.out.println("--- Average Stats per Generation (Non-Legendary) ---");
        generationStats.forEach((gen, acc) -> {
            System.out.printf("Generation %d: HP=%.2f; Attack=%.2f; Defense=%.2f; Speed=%.2f%n",
                    gen,
                    acc.avgHp(),
                    acc.avgAttack(),
                    acc.avgDefense(),
                    acc.avgSpeed());
        });
    }

    static class StatsAccumulator {
        long count = 0;
        long totalHp = 0;
        long totalAttack = 0;
        long totalDefense = 0;
        long totalSpeed = 0;

        void add(Pokemon p) {
            count++;
            totalHp += p.getHp();
            totalAttack += p.getAttack();
            totalDefense += p.getDefense();
            totalSpeed += p.getSpeed();
        }

        double avgHp() { return count == 0 ? 0 : (double) totalHp / count; }
        double avgAttack() { return count == 0 ? 0 : (double) totalAttack / count; }
        double avgDefense() { return count == 0 ? 0 : (double) totalDefense / count; }
        double avgSpeed() { return count == 0 ? 0 : (double) totalSpeed / count; }
    }

}
