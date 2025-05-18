package cassandra.ex3_new;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.session.Session;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableWithOptions;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.shaded.guava.common.collect.ImmutableMap;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URL;
import java.time.Duration;
import java.util.*;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;
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
                            new Pokemon(
                                    Integer.parseInt(split[0]),
                                    Integer.parseInt(split[5]),
                                    Integer.parseInt(split[6]),
                                    Integer.parseInt(split[7]),
                                    Integer.parseInt(split[10]),
                                    Integer.parseInt(split[11]),
                                    Boolean.parseBoolean(split[12])
                            )
                    );

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
                    .withPartitionKey("legendary", DataTypes.BOOLEAN)
                    .withClusteringColumn("generation", DataTypes.INT)
                    .withClusteringColumn("id", DataTypes.INT)
                    .withColumn("hp", DataTypes.INT)
                    .withColumn("attack", DataTypes.INT)
                    .withColumn("defense", DataTypes.INT)
                    .withColumn("speed", DataTypes.INT)
                    .withCompaction(leveledCompactionStrategy())
                    .withSnappyCompression();
            session.execute(create.build());

            System.out.println(create.asCql()); // nur für uns zum anschauen

            Insert insert = insertInto("big_data_pokemon", "pokemon")
                    .value("legendary", bindMarker())
                    .value("generation", bindMarker())
                    .value("id", bindMarker())
                    .value("hp", bindMarker())
                    .value("attack", bindMarker())
                    .value("defense", bindMarker())
                    .value("speed", bindMarker());


            for(Pokemon p: pokemon) {
                session.execute(insert.asCql(),
                        p.isLegendary(),
                        p.getGeneration(),
                        p.getId(),
                        p.getHp(),
                        p.getAttack(),
                        p.getDefense(),
                        p.getSpeed());

            }

            Select select = selectFrom("big_data_pokemon", "pokemon")
                    .columns("hp", "attack", "defense", "speed", "generation")
                    .whereColumn("legendary").isEqualTo(literal(false))
                    .orderBy("generation", ClusteringOrder.ASC)
                    .orderBy("id", ClusteringOrder.ASC);

            ResultSet resSet = session.execute(select.build());

            Map<Integer, List<PokemonStats>> statsByGeneration = new HashMap<>();

            while (resSet.iterator().hasNext()) {
                Row row = resSet.one();
                int generation = row.getInt("generation");
                int hp = row.getInt("hp");
                int attack = row.getInt("attack");
                int defense = row.getInt("defense");
                int speed = row.getInt("speed");

                statsByGeneration.computeIfAbsent(generation, k -> new ArrayList<>())
                        .add(new PokemonStats(hp, attack, defense, speed));
            }

            // Compute averages
            for (Map.Entry<Integer, List<PokemonStats>> entry : statsByGeneration.entrySet()) {
                int generation = entry.getKey();
                List<PokemonStats> statsList = entry.getValue();

                double avgHp = statsList.stream().mapToInt(s -> s.hp).average().orElse(0);
                double avgAttack = statsList.stream().mapToInt(s -> s.attack).average().orElse(0);
                double avgDefense = statsList.stream().mapToInt(s -> s.defense).average().orElse(0);
                double avgSpeed = statsList.stream().mapToInt(s -> s.speed).average().orElse(0);

                System.out.printf("Generation %d: Avg HP=%.2f, Avg Attack=%.2f, Avg Defense=%.2f, Avg Speed=%.2f%n",
                        generation, avgHp, avgAttack, avgDefense, avgSpeed);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class PokemonStats {
        int hp, attack, defense, speed;
        PokemonStats(int hp, int attack, int defense, int speed) {
            this.hp = hp;
            this.attack = attack;
            this.defense = defense;
            this.speed = speed;
        }
    }

}
