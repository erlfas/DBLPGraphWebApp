package no.dblp.rest.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class JSONGraph {

    private List<JSONEdge> edges;
    private List<JSONNode> nodes;
    
    public JSONGraph() {
        
    }
    
    public JSONGraph(List<Node> nodes, List<Relationship> edges) {
        this.nodes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); ++i) {
            Node node = nodes.get(i);
            String id = String.valueOf(node.getId());
            String name = (String) node.getProperty("name");
            this.nodes.add(new JSONNode(id,name));
        }
        this.edges = new ArrayList<>();
        for (int i = 0; i < edges.size(); ++i) {
            Relationship r = edges.get(i);
            String id = String.valueOf(r.getId());
            Node start = r.getStartNode();
            String startName = (String) start.getProperty("name");
            Node end = r.getEndNode();
            String endName = (String) end.getProperty("name");
            String label = "IS_AUTHOR_OF";
            this.edges.add(new JSONEdge(id, startName, endName, label));
        }
    }

    public List<JSONEdge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<JSONNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

}
