package no.dblp.factories.model;

/**
 *
 * @author erlend321
 */
public class Author {

    private final Long id;
    private final String name;
    
    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
}
