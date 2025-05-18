package cassandra.ex5_old;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

@Entity
public class Pokemon {

    @ClusteringColumn(1)
    private int id;

    private String name;

    @ClusteringColumn(0)
    private String type;

    @CqlName("type_2")
    private String type2;

    private int total;

    private int hp;

    private int attack;

    private int defense;

    @CqlName("speed_attack")
    private int speedAttack;

    @CqlName("speed_defence")
    private int speedDefence;

    private int speed;

    private int generation;

    @PartitionKey
    private boolean legendary;

    public Pokemon() {
    }

    public Pokemon(int id, String name, String type, String type_2,
                   int total, int hp, int attack, int defense, int speedAttack, int speedDefence, int speed, int generation, boolean legendary) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.type2 = type_2;
        this.generation = generation;
        this.legendary = legendary;
        this.total = total;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speedAttack = speedAttack;
        this.speedDefence = speedDefence;
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public boolean isLegendary() {
        return legendary;
    }

    public void setLegendary(boolean legendary) {
        this.legendary = legendary;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeedAttack() {
        return speedAttack;
    }

    public void setSpeedAttack(int speedAttack) {
        this.speedAttack = speedAttack;
    }

    public int getSpeedDefence() {
        return speedDefence;
    }

    public void setSpeedDefence(int speedDefence) {
        this.speedDefence = speedDefence;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
