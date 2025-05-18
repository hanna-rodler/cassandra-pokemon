package cassandra.ex5;

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
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.InsertInto;
import com.datastax.oss.driver.api.core.ConsistencyLevel;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
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

            /* ---------
            POKEMON TABLE
            ---------- */
            session.execute(dropTable("big_data_pokemon", "pokemon").ifExists().build());

            // create table Pokemon
            CreateTableWithOptions create = createTable("big_data_pokemon", "pokemon")
                    .withPartitionKey("id", DataTypes.INT)
                    .withClusteringColumn("type", DataTypes.TEXT)
                    .withColumn("generation", DataTypes.INT)
                    .withColumn("legendary", DataTypes.BOOLEAN)
                    .withColumn("type_2", DataTypes.TEXT)
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
                    .withClusteringOrder("type", ClusteringOrder.DESC);
            session.execute(create.build());

            System.out.println(create.asCql());


            /* ------
            POKEMON TRAINER TABLE
            ------- */
            session.execute(dropTable("big_data_pokemon", "trainer_pokemon").ifExists().build());

            CreateTableWithOptions pokemonTrainingTable = createTable("big_data_pokemon", "trainer_pokemon")
                    .withPartitionKey("trainer_id", DataTypes.UUID)
                    .withClusteringColumn("pokemon_id", DataTypes.INT)
                    .withColumn("species", DataTypes.TEXT)
                    .withColumn("level", DataTypes.INT)
                    .withColumn("xp", DataTypes.INT)
                    .withColumn("completed_training_sessions", DataTypes.INT)
                    .withCompaction(leveledCompactionStrategy())
                    .withSnappyCompression()
                    .withClusteringOrder("pokemon_id", ClusteringOrder.DESC);

            session.execute(pokemonTrainingTable.build());

            System.out.println(pokemonTrainingTable.asCql()); // nur für uns zum anschauen


            /* ------
            TRAINING SESSION TABLE
            ------- */
            session.execute(dropTable("big_data_pokemon", "training_session").ifExists().build());

            CreateTableWithOptions trainingSessionTable = createTable("big_data_pokemon", "training_session")
                    .withPartitionKey("session_id", DataTypes.UUID)
                    .withClusteringColumn("pokemon_id", DataTypes.INT)
                    .withClusteringColumn("trainer_id", DataTypes.UUID)
                    .withClusteringColumn("timestamp", DataTypes.TIMESTAMP)
                    .withColumn("total_update", DataTypes.INT)
                    .withColumn("hp_update", DataTypes.INT)
                    .withColumn("attack_update", DataTypes.INT)
                    .withColumn("defense_update", DataTypes.INT)
                    .withColumn("speed_attack_update", DataTypes.INT)
                    .withColumn("speed_defence_update", DataTypes.INT)
                    .withColumn("speed_update", DataTypes.INT)
                    .withColumn("generation_update", DataTypes.INT)
                    .withColumn("legendary_update", DataTypes.BOOLEAN)
                    .withCompaction(leveledCompactionStrategy())
                    .withSnappyCompression()
                    .withClusteringOrder("pokemon_id", ClusteringOrder.DESC);

            session.execute(trainingSessionTable.build());

            System.out.println(trainingSessionTable.asCql()); // nur für uns zum anschauen

            PokemonMapper mapper = new PokemonMapperBuilder(session).build();
            PokemonDao dao = mapper.pokemonDao(CqlIdentifier.fromCql("big_data_pokemon"));
            TrainerPokemonDao trainerDao = mapper.trainerPokemonDao(CqlIdentifier.fromCql("big_data_pokemon"));
            TrainingSessionDao trainingSessionDao = mapper.trainingSessionDao(CqlIdentifier.fromCql("big_data_pokemon"));

            for (Pokemon p: pokemon) {
                dao.save(p);
            }

            System.out.println("Pokemon Delcatty is: " + dao.findById(301));
            Pokemon delcatty = dao.findById(301);
            Pokemon slowbro = dao.findById(80);
            System.out.println("Slowbro is: " + slowbro.getName() + slowbro.getId() + slowbro.getType());


            // Create a test TrainerTypeStats object
            Trainer trainer1 = new Trainer(UUID.randomUUID(), "Hanna");
            Trainer trainer2 = new Trainer(UUID.randomUUID(), "Charlotte");
            TrainerPokemon trainer1Delcatty = new TrainerPokemon(delcatty.getId(), trainer1.getId(), delcatty.getType(), 2, 250, 4);
            trainerDao.save(trainer1Delcatty);

            TrainerPokemon trainerPokemon2 = new TrainerPokemon(slowbro.getId(), trainer1.getId(), slowbro.getType(), 3, 447, 8);
            trainerDao.save(trainerPokemon2);


            TrainerPokemon trainer2Delcatty =  new TrainerPokemon(delcatty.getId(), trainer2.getId(), delcatty.getType(), 2, 250, 4);
            trainerDao.save(trainer1Delcatty);

            // Retrieve it
            TrainerPokemon retrieved = trainerDao.getPokemonById(trainer1Delcatty.getTrainerId(), delcatty.getId());
            System.out.println("Total trainings for Delcatty for trainer1: " + retrieved.getCompletedTrainingSessions() + " Level: "+retrieved.getLevel());

            /* ------
             EX6: Record new training session for a Pokemon
             */
            TrainingSession ts1Delcatty = new TrainingSession(
                    UUID.randomUUID(),
                    delcatty.getId(),
                    trainer1.getId(),
                    Instant.now(),
                    10, 2, 3, 1, 2, 1, 1, 0, false
                    );

            TrainingSession ts1Pokemon2 = new TrainingSession(
                    UUID.randomUUID(),
                    slowbro.getId(),
                    trainer1.getId(),
                    Instant.now(),
                    2, 5, -1, 1, 2, 0, 1, 0, false
            );

            TrainingSession ts2Delcatty = new TrainingSession(
                    UUID.randomUUID(),
                    delcatty.getId(),
                    trainer1.getId(),
                    Instant.now(),
                    15, 4, 2, 2, 2, 3, 1, 0, false
            );

            TrainingSession ts1Trainer2Delcatty = new TrainingSession(
                    UUID.randomUUID(),
                    delcatty.getId(),
                    trainer1.getId(),
                    Instant.now(),
                    2, 1, 0, 1, 0, 1, 1, 0, false
            );

            // train from trainer 1
            recordTrainingSession(session, ts1Delcatty);
            recordTrainingSession(session, ts1Pokemon2);

            // train from trainer 2
            recordTrainingSession(session, ts1Trainer2Delcatty);

            System.out.println("\nTraining Sessions: ");
            for (TrainingSession ts : trainingSessionDao.getAllSessions()) {
                System.out.println(ts.toString());
            }
            System.out.println("\n");

            // EX 6 -  UPDATE POKEMON'S XP POINTS AND LEVEL
            // update Delcatty according to Training Session - Trainer 1
            TrainingSession retrievedDelcattyTS = trainingSessionDao.getByPrimaryKey(ts1Delcatty.getSessionId(), ts1Delcatty.getPokemonId(), ts1Delcatty.getTrainerId(),  ts1Delcatty.getTimestamp());
            updateTrainerPokemonByTrainingSession(trainer1Delcatty, retrievedDelcattyTS, trainerDao, session);

            // update Slowbro - Trainer 1
            TrainingSession retrievedSlowbroTS = trainingSessionDao.getByPrimaryKey(ts1Delcatty.getSessionId(), ts1Delcatty.getPokemonId(), ts1Delcatty.getTrainerId(),  ts1Delcatty.getTimestamp());
            updateTrainerPokemonByTrainingSession(trainerPokemon2, retrievedSlowbroTS, trainerDao, session);

            // update Delcatty according to Training Session - Trainer 2
            TrainingSession retrievedDelcattyTSTrainer2 = trainingSessionDao.getByPrimaryKey(ts1Trainer2Delcatty.getSessionId(), ts1Trainer2Delcatty.getPokemonId(), ts1Trainer2Delcatty.getTrainerId(),  ts1Trainer2Delcatty.getTimestamp());
            updateTrainerPokemonByTrainingSession(trainer2Delcatty, retrievedDelcattyTSTrainer2, trainerDao, session);


            System.out.println("Train Delcatty by Trainer 1 again");
            recordTrainingSession(session, ts2Delcatty);
            TrainingSession retrievedDelcattyTS2 = trainingSessionDao.getByPrimaryKey(ts2Delcatty.getSessionId(), ts2Delcatty.getPokemonId(), ts2Delcatty.getTrainerId(),  ts2Delcatty.getTimestamp());
            TrainerPokemon delcattyTrainer1AfterTS1 = trainerDao.getPokemonById(ts2Delcatty.getTrainerId(), ts2Delcatty.getPokemonId());
            updateTrainerPokemonByTrainingSession(delcattyTrainer1AfterTS1, retrievedDelcattyTS2, trainerDao, session);


            // update trainer's statistics for that pokemon type.



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: As a trainer, I want to see which Pokemon types I have trained the most and their average
    //improvement in stats.
    // Updates the trainer’s statistics for that Pokemon type

    private static void updateTrainerPokemonByTrainingSession(TrainerPokemon tp, TrainingSession ts, TrainerPokemonDao trainerDao, CqlSession session) {
        System.out.println("\n------- UPDATING POKEMON AFTER TRAINING --------");
        System.out.println("Trainer pokemon before training: " + tp.toString());
        // only update level if totalUpdate is greater ten
        int newLevel = ts.getTotalUpdate() > 10 ? tp.getLevel() + 1 : tp.getLevel();
        int newTrainingSessions = tp.getCompletedTrainingSessions() +1;
        int newXp = tp.getXp() + ts.getTotalUpdate();

        updateTrainerPokemonStatsBatch(session, tp.getTrainerId(), tp.getPokemonId(), newXp, newLevel, newTrainingSessions);
        TrainerPokemon updatedDelcatyTrainerPokemon = trainerDao.getPokemonById(tp.getTrainerId(), tp.getPokemonId());
        System.out.println("After batch update: " + updatedDelcatyTrainerPokemon.toString());
        System.out.println("-------------------------\n");
    }

    public static void updateTrainerPokemonStatsBatch(
            CqlSession session,
            UUID trainerId,
            int pokemonId,
            int xp,
            int level,
            int completedTrainingSessions
    ) {
        // Prepare update statements for each field
        String updateXpCql = "UPDATE big_data_pokemon.trainer_pokemon SET xp = ? WHERE trainer_id = ? AND pokemon_id = ?";
        String updateLevelCql = "UPDATE big_data_pokemon.trainer_pokemon SET level = ? WHERE trainer_id = ? AND pokemon_id = ?";
        String updateCompletedSessionsCql = "UPDATE big_data_pokemon.trainer_pokemon SET completed_training_sessions = ? WHERE trainer_id = ? AND pokemon_id = ?";

        PreparedStatement updateXpStmt = session.prepare(updateXpCql);
        PreparedStatement updateLevelStmt = session.prepare(updateLevelCql);
        PreparedStatement updateCompletedStmt = session.prepare(updateCompletedSessionsCql);

        BoundStatement boundXp = updateXpStmt.bind(xp, trainerId, pokemonId);
        BoundStatement boundLevel = updateLevelStmt.bind(level, trainerId, pokemonId);
        BoundStatement boundCompleted = updateCompletedStmt.bind(completedTrainingSessions, trainerId, pokemonId);

        BatchStatement batch = BatchStatement.builder(DefaultBatchType.LOGGED)
                .addStatement(boundXp)
                .addStatement(boundLevel)
                .addStatement(boundCompleted)
                .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM) // or QUORUM
                .build();

        session.execute(batch);
    }



    private static void recordTrainingSession(CqlSession session, TrainingSession ts) {
        // Prepare insert for training_session
        PreparedStatement insertTraining = session.prepare(
                "INSERT INTO big_data_pokemon.training_session (" +
                        "session_id, pokemon_id, trainer_id, timestamp, total_update, hp_update, attack_update, defense_update, " +
                        "speed_attack_update, speed_defence_update, speed_update, generation_update, legendary_update) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
        );

        BoundStatement insertTrainingBound = insertTraining.bind(
                ts.getSessionId(), ts.getPokemonId(), ts.getTrainerId(), ts.getTimestamp(),
                ts.getTotalUpdate(), ts.getHpUpdate(), ts.getAttackUpdate(), ts.getDefenseUpdate(),
                ts.getSpeedAttackUpdate(), ts.getSpeedDefenceUpdate(), ts.getSpeedUpdate(),
                ts.getGenerationUpdate(), ts.isLegendaryUpdate()
        );

        BatchStatement batch = BatchStatement.builder(DefaultBatchType.LOGGED)
                .addStatement(insertTrainingBound)
                .build();

        session.execute(batch);

        System.out.println("Training session recorded.");
    }
}
