package uk.sch.greycourt.richmond.brandongruber.revcards;

public class Project {

    private String id;
    private String name;
    private String description;
    

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
