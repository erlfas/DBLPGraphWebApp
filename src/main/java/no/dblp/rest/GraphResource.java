package no.dblp.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import no.dblp.dbutils.DbFactory;
import no.dblp.rest.model.JSONGraph;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

@Path("Graph")
public class GraphResource {
    
    private static final GraphDatabaseService DB = DbFactory.getInstance();
    
    public GraphResource() {
    }

    @GET
    @Path("{authorName}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONGraph getGraph(@PathParam("authorName") String authorName) {
        
        String query = String.format("MATCH (a:Author)-[r:IS_AUTHOR_OF]->(p:Publication)<-[s:IS_AUTHOR_OF]-(b:Author) WHERE a.name = '%s' RETURN a,r,b,s,p", authorName);
        
        System.out.println("Inni GraphResource");
        
        Node mainAuthor = null;
        List<Node> authors = new ArrayList<>();
        List<Relationship> relationships = new ArrayList<>();
        
        try (Transaction tx = DB.beginTx()) {
            mainAuthor = DB.findNode(DynamicLabel.label("Author"), "name", authorName);
            authors.add(mainAuthor);
            Iterable<Relationship> isAuthorOf = mainAuthor.getRelationships(Direction.OUTGOING);
            for (Relationship r : isAuthorOf) {
                relationships.add(r);
                Node publication = r.getEndNode();
                Iterable<Relationship> isAuthoredBy = publication.getRelationships(Direction.INCOMING);
                for (Relationship s : isAuthoredBy) {
                    relationships.add(s);
                    Node coauthor = s.getEndNode();
                    if (!coauthor.equals(mainAuthor)) {
                        authors.add(coauthor);
                    }
                }
            }
            
            tx.success();
        }
        
        return new JSONGraph(authors, relationships);
    }
    
}
