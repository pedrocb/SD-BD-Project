/**
 * Created by mariobalca on 25-10-2015.
 */
public class Extra {
    private int id;
    private double minValue;
    private String name;
    private String description;

    public Extra(int id, double min, String n, String d){
        this.id = id;
        this.minValue = min;
        this.name = n;
        this.description = d;
    }

    public int getId() {
        return id;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}