package ch.so.agi.ilivalidator.ext.oereb;

import java.util.ArrayList;
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

    private List<String> duplicateEdges;

    private List<String> selfLoops;

    private LinkGraphCache() {}

    public static LinkGraphCache getInstance(Collection<IomObject> collection) {
        if (instance == null) {
            instance = new LinkGraphCache();
            instance.linkGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
            instance.duplicateEdges = new ArrayList<String>();
            instance.selfLoops = new ArrayList<String>();
            
            for (IomObject iomObj : collection) {
                String startOid = iomObj.getattrobj("Ursprung", 0).getobjectrefoid();
                String endOid = iomObj.getattrobj("Hinweis", 0).getobjectrefoid();
                
                try {
                    DefaultEdge e = null;
                    e = Graphs.addEdgeWithVertices(instance.linkGraph, startOid, endOid);
                    if (e == null) {
                        instance.duplicateEdges.add(iomObj.getobjectoid());
                    }
                } catch (IllegalArgumentException e) {
                    if (startOid.equalsIgnoreCase(endOid)) {
                        // self loop
                        instance.selfLoops.add(startOid);
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }
    
    public Graph<String, DefaultEdge> getGraph() {
        return linkGraph;
    }

    public List<String> getDuplicateEdges() {
        return duplicateEdges;
    }
    
    public List<String> getSelfLoops() {
        return selfLoops;
    }
}
