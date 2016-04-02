package no.dblp.factories.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Publication {

    private final String title;
    private final List<String> authors;
    
    public Publication(String title) {
        this.title = title;
        this.authors = new ArrayList<>();
    }
    
    public List<String> getAuthors() {
        return Collections.unmodifiableList(authors);
    }
    
    public void addAuthor(String author) {
        this.authors.add(author);
    }

    public String getTitle() {
        return title;
    }
    
}
