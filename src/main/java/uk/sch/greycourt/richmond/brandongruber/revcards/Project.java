package uk.sch.greycourt.richmond.brandongruber.revcards;

/**
 * Encapsulates a project.
 */
public class Project {

    private String id;
    private String name;
    private String description;


    /**
     * Constructor.
     * 
     * @param id The {@link String} id of the project.
     * @param name The {@link String} name of the project.
     * @param description The {@link String} description of the project.
     */
    public Project(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
