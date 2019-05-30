package ch.so.agi.ilivalidator.ext.oereb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import ch.interlis.iom.IomObject;

public class LinkGraphCache {
    private static LinkGraphCache instance = null;

    private int hashCode;
    
    private Graph<String, DefaultEdge> linkGraph;

    private List<String> duplicateEdges;
    
    private Set<String> cycles;
  
    private LinkGraphCache() {}

    public static synchronized LinkGraphCache getInstance(Collection<IomObject> collection, int hashCode) {
        if (instance == null) {
            System.out.println("BAR");

            instance = new LinkGraphCache();
            instance.hashCode = hashCode;
            instance.linkGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
            instance.duplicateEdges = new ArrayList<String>();
            
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
                    // Self loops throw an IllegalArgumentException.
                    // Self loops will be handled in the InterlisFunction by 
                    // comparing the OIDs.
                }
            }
        }
        return instance;
    }
    
    public int getHashCode() {
        return hashCode;
    }
    
    public Graph<String, DefaultEdge> getGraph() {
        return linkGraph;
    }

    public List<String> getDuplicateEdges() {
        return duplicateEdges;
    }
}
