package ch.so.agi.ilivalidator.ext;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.InterlisFunction;
import ch.interlis.iox_j.validator.ObjectPool;
import ch.interlis.iox_j.validator.Value;

public class HasUniqueAttributePairsIoxPlugin implements InterlisFunction {
    private LogEventFactory logger = null;

    @Override
    public Value evaluate(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {

        System.out.println("-------------");
        System.out.println(validationKind);
        System.out.println(usageScope);
        System.out.println(mainObj.getobjectoid());
        System.out.println(actualArguments[0].getRefTypeName());
        System.out.println(actualArguments[0].getValues());
        System.out.println(actualArguments[0].getComplexObjects());
        System.out.println(actualArguments[0].getType());
        System.out.println(actualArguments[0].getViewable());
        System.out.println(actualArguments[1].getValue());
        System.out.println("****");
        System.out.println(mainObj.getattrcount());
        System.out.println(mainObj.getattrname(0));

        System.out.println("--------------");
        
        
        
        return new Value(true);
    }

    @Override
    public String getQualifiedIliName() {
        return "SO_FunctionsExt.hasUniqueAttributePairs";
    }

    @Override
    public void init(TransferDescription td, Settings settings, 
            IoxValidationConfig validationConfig, ObjectPool objectPool, 
            LogEventFactory logEventFactory) {
        
        logger = logEventFactory;
    }

}
