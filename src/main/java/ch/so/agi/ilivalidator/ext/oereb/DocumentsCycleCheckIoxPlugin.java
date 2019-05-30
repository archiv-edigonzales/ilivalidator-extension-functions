package ch.so.agi.ilivalidator.ext.oereb;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jgrapht.Graph;
import org.jgrapht.GraphTests;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.InterlisFunction;
import ch.interlis.iox_j.validator.ObjectPool;
import ch.interlis.iox_j.validator.Value;

public class DocumentsCycleCheckIoxPlugin implements InterlisFunction {
    private LogEventFactory logger = null;
    
    private ObjectPool objectPool = null;
    
    @Override
    public Value evaluate(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {   
        System.out.println(mainObj.getobjectoid());
//        Graph<String, DefaultEdge> g = new SimpleDirectedGraph<>(DefaultEdge.class);
//        DefaultEdge e = null;
//        e = Graphs.addEdgeWithVertices(g, "o1", "o2");
//        System.out.println(e.toString());
//        e = Graphs.addEdgeWithVertices(g, "o2", "o3");
//        e = Graphs.addEdgeWithVertices(g, "o2", "o3"); // Returns null, wenn die Kante bereits vorhanden ist. -> Fehlermeldung resp. wegspeichern, um beim Objekt den Fehler zu melden. (Oder MANDATORY CONSTRAIN für diese Art von Fehler.)
//        System.out.println(e);
//        Graphs.addEdgeWithVertices(g, "o3", "o4");
//        Graphs.addEdgeWithVertices(g, "o3", "o2");
//        System.out.println(g.toString());
//        
//        CycleDetector<String, DefaultEdge> cycleDetector = new CycleDetector<>(g);
//        System.out.println(cycleDetector.detectCycles());
//        
//        if (cycleDetector.detectCycles()) {
//            String cycles = String.join(",", cycleDetector.findCycles());
//            System.out.println(cycles);
//        }
        
        // Reihenfolge des Hinzufügens von Kanten.
        // Ist das ein Problem beim Herausfinden von Fehlern?
//        Graph<String, DefaultEdge> g = new SimpleDirectedGraph<>(DefaultEdge.class);
//        DefaultEdge e = null;
//        Graphs.addEdgeWithVertices(g, "o2", "o3");
//        Graphs.addEdgeWithVertices(g, "o1", "o2");
//        Graphs.addEdgeWithVertices(g, "o3", "o4");
//
//        // bis hierhin ist der Graph connected.
//        
//        Graphs.addEdgeWithVertices(g, "o5", "o6");
//        Graphs.addEdgeWithVertices(g, "o6", "o4");
//        Graphs.addEdgeWithVertices(g, "o6", "o6"); // Wirft Exception! -> Wegkopieren. Btw: Was machen mit OID von Assoz?
//        // nicht mehr connected.
//
//        System.out.println(GraphTests.isConnected(g));
////        System.out.println(GraphTests.hasSelfLoops(g));
//        
//        CycleDetector<String, DefaultEdge> cycleDetector = new CycleDetector<>(g);
//        System.out.println(cycleDetector.detectCycles());
//
//        
//        // Cycles werden nicht mitkopiert. 
//        ConnectivityInspector connectivityInspector = new ConnectivityInspector<>(g);
//        List<Set<String>> sets = connectivityInspector.connectedSets();
//        System.out.println(sets);
        /*
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        
        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("/Users/stefan/tmp/graph.png");
        try {
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e1) {
        }
        */
        try {
            LinkGraphCache lc = LinkGraphCache.getInstance(actualArguments[0].getComplexObjects(), objectPool.hashCode());
            
            System.out.println("**");
            System.out.println(lc.getHashCode());
            System.out.println(objectPool.hashCode());
            
            if (objectPool.hashCode() != lc.getHashCode()) {
                System.out.println("neue instanz");
                Class<LinkGraphCache> clazz = LinkGraphCache.class;
                Constructor<LinkGraphCache> cons = clazz.getDeclaredConstructor();
                cons.setAccessible(true);
                lc = cons.newInstance();
                System.out.println("--");
                System.out.println(lc.getHashCode());
            }
            
            Graph<String, DefaultEdge> graph = lc.getGraph();
            List<String> duplicateEdges = lc.getDuplicateEdges();
            
            // Duplicate edge: two (or more) associations from document A to document B.
            if (duplicateEdges.contains(mainObj.getobjectoid())) {
                logger.addEvent(logger.logErrorMsg("duplicate link found: " + mainObj.getobjectoid(), mainObj.getobjectoid()));
                return new Value(false);
            }
            
            String startOid = mainObj.getattrobj("Ursprung", 0).getobjectrefoid();
            String endOid = mainObj.getattrobj("Hinweis", 0).getobjectrefoid();
            
//            logger.addEvent(logger.logInfoMsg("Ursprung: " + startOid, null));
//            logger.addEvent(logger.logInfoMsg("Hinweis: " + endOid, null));
            
            // Self loop: An association the points from document A to document A.
            if (startOid.equals(endOid)) {
                logger.addEvent(logger.logErrorMsg("self loop found: " + mainObj.getobjectoid(), mainObj.getobjectoid()));
                return new Value(false);
            }   
            
            // Cycle: Document A -> Document B -> Document C -> Document A.
            // There is not ONE wrong edge but always several wrong ones.
            // It is up to the user to decide.
            CycleDetector<String, DefaultEdge> cycleDetector = new CycleDetector<>(graph);
            Set<String> vertices = cycleDetector.findCycles();
            System.out.println(vertices);
            if (vertices.size() == 0) {
                return new Value(true);
            } else {
                if (vertices.contains(startOid) && vertices.contains(endOid)) {
                    String cycles = String.join(",", vertices);
                    logger.addEvent(logger.logErrorMsg("object "+mainObj.getobjectoid()+" ("+startOid +" <-> "+endOid+") is part of a cycle: "+cycles+".", mainObj.getobjectoid()));
                    return new Value(false);
                }
            }
        } catch (Exception e) {
            logger.addEvent(logger.logErrorMsg(e, "something went wrong", null));
            return new Value(false);
        }
        return new Value(true);
    }

    @Override
    public String getQualifiedIliName() {
        return "SO_OEREB_FunctionsExt.documentsCycleCheck";
    }

    @Override
    public void init(TransferDescription td, Settings settings, 
            IoxValidationConfig validationConfig, ObjectPool objectPool, 
            LogEventFactory logEventFactory) {
        
//        System.out.println("INIT");
////        System.out.println(objectPool.hashCode());
        
        logger = logEventFactory;
        logger.setValidationConfig(validationConfig);
        
        this.objectPool = objectPool;
    }
}
