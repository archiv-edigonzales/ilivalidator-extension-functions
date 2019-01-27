package ch.so.agi.ilivalidator.ext;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.InterlisFunction;
import ch.interlis.iox_j.validator.ObjectPool;
import ch.interlis.iox_j.validator.Value;

public class CheckHttpRessourceIoxPlugin implements InterlisFunction {
    private LogEventFactory logger = null;
    
    @Override
    public Value evaluate(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        // TODO Auto-generated method stub
        
        System.err.println("evaluate");
        
        return new Value(false);
    }

    @Override
    public String getQualifiedIliName() {
        return "SO_FunctionsExt.checkHttpRessource";
    }

    @Override
    public void init(TransferDescription td, Settings settings, 
            IoxValidationConfig validationConfig, ObjectPool objectPool, 
            LogEventFactory logEventFactory) {
        
        System.err.println("init");

    }

}
