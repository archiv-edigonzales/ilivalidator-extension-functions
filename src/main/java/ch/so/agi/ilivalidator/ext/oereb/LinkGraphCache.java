package ch.so.agi.ilivalidator.ext.oereb;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import ch.interlis.iom.IomObject;

public class LinkGraphCache {
    private static LinkGraphCache instance = null;

//    private Map<String, String> linkGraph;
    
    
    // TODO: ausprobieren mit IomObject anstatt String.
    // Oder Anfang und Ende und OID der Edge "o1o2"?
    private Graph<String, DefaultEdge> linkGraph;

    private HashMap<String, String> duplicateEdges;

    private LinkGraphCache() {}

    public static LinkGraphCache getInstance(Collection<IomObject> collection) {
        if (instance == null) {
            instance = new LinkGraphCache();
            instance.linkGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
            instance.duplicateEdges = new HashMap<String, String>();
            
            for (IomObject iomObj : collection) {
                String startOid = iomObj.getattrobj("Ursprung", 0).getobjectrefoid();
                String endOid = iomObj.getattrobj("Hinweis", 0).getobjectrefoid();
                
//                System.out.println(startOid);
//                System.out.println(endOid);
                
                DefaultEdge e = null;
                e = Graphs.addEdgeWithVertices(instance.linkGraph, startOid, endOid);
                if (e == null) {
                    instance.duplicateEdges.put(startOid, endOid);
                }
            }
        }
        return instance;
    }
    
    public Graph<String, DefaultEdge> getGraph() {
        return linkGraph;
    }

    public HashMap<String, String> getDuplicateEdges() {
        return duplicateEdges;
    }
    
}
