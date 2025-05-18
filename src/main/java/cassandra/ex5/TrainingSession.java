package cassandra.ex5;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.time.Instant;
import java.util.UUID;

@Entity
public class TrainingSession {
    @PartitionKey
    private UUID sessionId;

    @ClusteringColumn(0)
    private int pokemonId;

    @ClusteringColumn(3)
    private String species;

    @ClusteringColumn(1)
    private UUID trainerId;

    @ClusteringColumn(2)
    private Instant timestamp;

    private int totalUpdate;

    private int hpUpdate;

    private int attackUpdate;

    private int defenseUpdate;

    @CqlName("speed_attack_update")
    private int speedAttackUpdate;

    @CqlName("speed_defence_update")
    private int speedDefenceUpdate;

    private int speedUpdate;

    private int generationUpdate;

    private boolean legendaryUpdate;

    public TrainingSession() {}

    public TrainingSession(UUID sessionId, int pokemonId, UUID trainerId, Instant timestamp, int totalUpdate, int hpUpdate, int attackUpdate, int defenseUpdate, int speedAttackUpdate, int speedDefenceUpdate, int speedUpdate, int generationUpdate, boolean legendaryUpdate, String species) {
        this.sessionId = sessionId;
        this.pokemonId = pokemonId;
        this.trainerId = trainerId;
        this.timestamp = timestamp;
        this.totalUpdate = totalUpdate;
        this.hpUpdate = hpUpdate;
        this.attackUpdate = attackUpdate;
        this.defenseUpdate = defenseUpdate;
        this.speedAttackUpdate = speedAttackUpdate;
        this.speedDefenceUpdate = speedDefenceUpdate;
        this.speedUpdate = speedUpdate;
        this.generationUpdate = generationUpdate;
        this.legendaryUpdate = legendaryUpdate;
        this.species = species;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getTotalUpdate() {
        return totalUpdate;
    }

    public void setTotalUpdate(int totalUpdate) {
        this.totalUpdate = totalUpdate;
    }

    public int getHpUpdate() {
        return hpUpdate;
    }

    public void setHpUpdate(int hpUpdate) {
        this.hpUpdate = hpUpdate;
    }

    public int getAttackUpdate() {
        return attackUpdate;
    }

    public void setAttackUpdate(int attackUpdate) {
        this.attackUpdate = attackUpdate;
    }

    public int getDefenseUpdate() {
        return defenseUpdate;
    }

    public void setDefenseUpdate(int defenseUpdate) {
        this.defenseUpdate = defenseUpdate;
    }

    public int getSpeedAttackUpdate() {
        return speedAttackUpdate;
    }

    public void setSpeedAttackUpdate(int speedAttackUpdate) {
        this.speedAttackUpdate = speedAttackUpdate;
    }

    public int getSpeedDefenceUpdate() {
        return speedDefenceUpdate;
    }

    public void setSpeedDefenceUpdate(int speedDefenceUpdate) {
        this.speedDefenceUpdate = speedDefenceUpdate;
    }

    public int getSpeedUpdate() {
        return speedUpdate;
    }

    public void setSpeedUpdate(int speedUpdate) {
        this.speedUpdate = speedUpdate;
    }

    public int getGenerationUpdate() {
        return generationUpdate;
    }

    public void setGenerationUpdate(int generationUpdate) {
        this.generationUpdate = generationUpdate;
    }

    public boolean isLegendaryUpdate() {
        return legendaryUpdate;
    }

    public void setLegendaryUpdate(boolean legendaryUpdate) {
        this.legendaryUpdate = legendaryUpdate;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return "TrainingSession{" +
                "sessionId=" + sessionId +
                ", pokemonId=" + pokemonId +
                ", species='" + species + '\'' +
                ", trainerId=" + trainerId +
                ", timestamp=" + timestamp +
                ", totalUpdate=" + totalUpdate +
                ", hpUpdate=" + hpUpdate +
                ", attackUpdate=" + attackUpdate +
                ", defenseUpdate=" + defenseUpdate +
                ", speedAttackUpdate=" + speedAttackUpdate +
                ", speedDefenceUpdate=" + speedDefenceUpdate +
                ", speedUpdate=" + speedUpdate +
                ", generationUpdate=" + generationUpdate +
                ", legendaryUpdate=" + legendaryUpdate +
                '}';
    }
}

