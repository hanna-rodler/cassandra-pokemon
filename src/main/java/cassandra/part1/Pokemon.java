package cassandra.part1;

public class Pokemon {

    private int id;

    private String name;

    private String type;

    private String type_2;

    private int generation;

    private boolean legendary;

    public Pokemon() {
    }

    public Pokemon(int id, String name, String type, String type_2, int generation, boolean legendary) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.type_2 = type_2;
        this.generation = generation;
        this.legendary = legendary;
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

    public String getType_2() {
        return type_2;
    }

    public void setType_2(String type_2) {
        this.type_2 = type_2;
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
}
