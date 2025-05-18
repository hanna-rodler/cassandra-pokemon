package cassandra.part1;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.session.Session;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.insert.InsertInto;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableWithOptions;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.shaded.guava.common.collect.ImmutableMap;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
                            new Pokemon(Integer.parseInt(split[0]), split[1], split[2], split[3], Integer.parseInt(split[11]), Boolean.parseBoolean(split[12])));
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
            Row row = rs.one();
            System.out.println(row.getString("release_version")); // oben release_version, deswegen kriegt hier auch release_version als Antwort column zurück
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
                    .withClusteringColumn("type", DataTypes.TEXT)
                    .withClusteringColumn("number", DataTypes.INT)
                    .withColumn("type_2", DataTypes.TEXT)
                    .withColumn("name", DataTypes.TEXT)
                    .withColumn("legendary", DataTypes.BOOLEAN)
                    .withCompaction(leveledCompactionStrategy())
                    .withSnappyCompression()
                    .withClusteringOrder("type", ClusteringOrder.DESC)
                    .withClusteringOrder("number", ClusteringOrder.DESC);
            session.execute(create.build());

            System.out.println(create.asCql()); // nur für uns zum anschauen


            // Insert via prepared stetement
            Insert insert = insertInto("big_data_pokemon", "pokemon")
                    .value("number", bindMarker())
                    .value("type", bindMarker())
                    .value("type_2", bindMarker())
                    .value("name", bindMarker())
                    .value("generation", bindMarker())
                    .value("legendary", bindMarker());

            for(Pokemon p: pokemon) {
                session.execute(insert.asCql(),
                        p.getId(), p.getType(), p.getType_2(),
                        p.getName(), p.getGeneration(), p.isLegendary());
            }

            // Insert TTL - non prepared
            Insert insertTTL = insertInto("big_data_pokemon", "pokemon")
                    .value("number", literal(-1))
                    .value("type", literal("Human"))
                    .value("type_2", literal(null))
                    .value("name", literal("Oliver"))
                    .value("generation", literal(-1))
                    .value("legendary", literal(true));
            session.execute(insertTTL.build());

            // SELECT ALL
            /*Select selectAll = selectFrom("big_data_pokemon", "pokemon")
                    .all();
            System.out.println(selectAll.asCql());
            session.execute(selectAll.build()).forEach(x ->
                    System.out.println(x.getFormattedContents()));*/


            // SELECT with FILTER
            Select select = selectFrom("big_data_pokemon", "pokemon")
                    .all()
                    .where(Relation.column("generation").isEqualTo(literal(1)))
                    .orderBy("type", ClusteringOrder.ASC)
                    .orderBy("number", ClusteringOrder.ASC);
            System.out.println(select.asCql());
            session.execute(select.build()).forEach(x ->
                    System.out.println(x.getFormattedContents()));

            System.out.println("--------------------------");
            // wenn nur .all() sehe ich nicht welche generation das ist.
            select = selectFrom("big_data_pokemon", "pokemon")
                    .column("generation")
                    .groupBy("generation").countAll();
            System.out.println(select.asCql());
            session.execute(select.build()).forEach(x ->
                    System.out.println(x.getFormattedContents()));

            System.out.println("--------------------------");
            // wenn nur .all() sehe ich nicht welche generation das ist.
            select = selectFrom("big_data_pokemon", "pokemon")
                    .columns("generation", "type").groupByColumns("generation", "type").countAll();
            System.out.println(select.asCql());
            session.execute(select.build()).forEach(x ->
                    System.out.println(x.getFormattedContents()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
