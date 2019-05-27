package ch.so.agi.ilivalidator.ext.oereb;

import java.util.HashMap;
import java.util.Iterator;

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

public class DocumentsSelfLoopCheckIoxPlugin implements InterlisFunction {
    private LogEventFactory logger = null;
    
//    private ObjectPool objectPool = null;

    @Override
    public Value evaluate(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        
        System.out.println("-------------");
        LinkCache lc = LinkCache.getInstance(actualArguments[0].getComplexObjects());
        
        System.out.println(lc.getCatalog().toString());
        
        System.out.println(usageScope);
        System.out.println(mainObj.getobjectoid());
        System.out.println(actualArguments[0].getComplexObjects());
        System.out.println("mainObj: " + mainObj);
        System.out.println("getattrcount: " + mainObj.getattrcount());
        System.out.println("getattrname: " + mainObj.getattrname(0));
        System.out.println("getattrobj.getobjectrefoid: " + mainObj.getattrobj("Ursprung", 0).getobjectrefoid());
//        System.out.println(actualArguments[0].getType());
//        System.out.println(actualArguments[0].getViewable());
//        System.out.println(actualArguments[0]);
//        System.out.println(actualArguments[1].getValue());
//        System.out.println(actualArguments[2].getValue());
//        System.out.println(mainObj.getattrcount());
//        System.out.println(mainObj.getattrname(0));

        System.out.println("--------------");

        // mainObj vergleichen mit actualArguments[0].getComplexObjects()
        
        // https://github.com/AgenciaImplementacion/iliValidator_custom_plugins/blob/master/src/main/java/co/interlis/topology/TopologyCache.java
        // falls Cache benötigt wird.
        
        
        
        return new Value(true);
    }

    @Override
    public String getQualifiedIliName() {
        return "SO_OEREB_FunctionsExt.documentsSelfLoopCheck";
    }

    @Override
    public void init(TransferDescription td, Settings settings, 
            IoxValidationConfig validationConfig, ObjectPool objectPool, 
            LogEventFactory logEventFactory) {
        
        logger = logEventFactory;
//        this.objectPool = objectPool;
    }

}
