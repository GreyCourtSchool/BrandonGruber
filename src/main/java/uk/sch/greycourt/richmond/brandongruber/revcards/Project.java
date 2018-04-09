package uk.sch.greycourt.richmond.brandongruber.revcards;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulates a project.
 */
public class Project implements Comparable<Project> {

    private String name;
    private String description;
    private List<RevCard> cardList =new ArrayList<>();

    /**
     * Constructor.
     *
     * @param name        The {@link String} name of the project.
     * @param description The {@link String} description of the project.
     */
    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(name, project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Project o) {
        return this.name.compareTo(o.name);
    }

    public void addCard(RevCard revCard) {
        cardList.add(revCard);
    }

    public List<RevCard> getCardList() {
        return cardList;
    }
}

