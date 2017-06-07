package swp_impl_acr.quizapy.Database.Entity;

/**
 * key / value (options) entity
 */
public class Option {

    private int id;
    private String name;
    private String value;

    /**
     * constructor
     *
     * @param id
     * @param name
     * @param value
     */
    public Option(int id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    /**
     * returns the id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * sets the id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns the key
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * sets the key
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns the value
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * sets the value
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
