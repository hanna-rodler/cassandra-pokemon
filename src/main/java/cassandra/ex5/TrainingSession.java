package cassandra.ex5;

import cassandra.ex5_old.TrainingHistory;
import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class TrainingSession {
    @PartitionKey
    private UUID sessionId;
    @ClusteringColumn
    private int pokemonId;
    private LocalDateTime timestamp;
    private String notes; // optional

    private List<TrainingHistory> trainingHistory;

    public TrainingSession() {}

    public TrainingSession(UUID sessionId, int pokemonId, LocalDateTime timestamp, String notes) {
        this.sessionId = sessionId;
        this.pokemonId = pokemonId;
        this.timestamp = timestamp;
        this.notes = notes;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<TrainingHistory> getTrainingHistory() {
        return trainingHistory;
    }

    public void setTrainingHistory(List<TrainingHistory> trainingHistory) {
        this.trainingHistory = trainingHistory;
    }
}

