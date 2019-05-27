package ch.so.agi.ilivalidator.ext.oereb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.interlis.iom.IomObject;

public class LinkCache {
    private static LinkCache instance = null;

    private Map<String, String> linkCatalog;

    private LinkCache() {}

    public static LinkCache getInstance(Collection<IomObject> collection) {
        if (instance == null) {
            instance = new LinkCache();
            instance.linkCatalog = new HashMap<String, String>();
            
            for (IomObject iomObj : collection) {
                String startVertex = iomObj.getattrobj("Ursprung", 0).getobjectrefoid();
                String endVertex = iomObj.getattrobj("Hinweis", 0).getobjectrefoid();
                instance.linkCatalog.put(startVertex, endVertex);
            }
        }
        return instance;
    }
    
    public Map<String, String> getCatalog() {
        return linkCatalog;
    }

}
