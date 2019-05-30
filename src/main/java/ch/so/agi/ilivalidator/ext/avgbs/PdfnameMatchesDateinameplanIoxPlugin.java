package ch.so.agi.ilivalidator.ext.avgbs;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.InterlisFunction;
import ch.interlis.iox_j.validator.ObjectPool;
import ch.interlis.iox_j.validator.Value;

/**
 * Funktioniert nur wenn in den Settings der Name der zu prüfenden
 * Datei mitgeliefert wird. Im ilivalidator web service wird das 
 * gemacht. Standalone wird es nicht funktionieren.
 * 
 * @author Stefan Ziegler / AGI SO
 *
 */
public class PdfnameMatchesDateinameplanIoxPlugin implements InterlisFunction {
    private LogEventFactory logger = null;
    
    private Settings settings = null;

    @Override
    public Value evaluate(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        if (actualArguments[0].skipEvaluation()) {
            return actualArguments[0];
        }
        if (actualArguments[0].isUndefined()) {
            return Value.createSkipEvaluation();
        }
        
        String dateinameplanValue = actualArguments[0].getValue();

        if (!dateinameplanValue.toLowerCase().endsWith(".pdf")) {
            logger.addEvent(logger.logErrorMsg("Attribut 'DateinamePlan' endet nicht mit '.pdf': " + dateinameplanValue, mainObj.getobjectoid()));
            return new Value(false);
        }
                
        String dateinameplanNoExt = dateinameplanValue.substring(0, dateinameplanValue.length()-4);
//        String dataFileName = settings.getValue();
        
        
        
        
        logger.addEvent(logger.logInfoMsg("dateinameplanValue: " + dateinameplanValue, mainObj.getobjectoid()));
        logger.addEvent(logger.logErrorMsg("fubar"));
        logger.addEvent(logger.logErrorMsg(dateinameplanValue));

        
        return new Value(false); 
    }

    @Override
    public String getQualifiedIliName() {
        return "SO_AVGBS_FunctionsExt.pdfnameMatchesDateinameplan";
    }

    @Override
    public void init(TransferDescription td, Settings settings, 
            IoxValidationConfig validationConfig, ObjectPool objectPool, 
            LogEventFactory logEventFactory) {

        logger=logEventFactory;        
        logger.setValidationConfig(validationConfig);       
        
        this.settings = settings;
    }

}
