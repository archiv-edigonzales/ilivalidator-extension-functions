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
 * Die gleiche Prüfung kann eventuell (zu prüfen) mit den im ilivalidator eingebauten
 * Text-Funktionen durchgeführt werden. 
 * 
 * @author Stefan Ziegler / AGI SO
 *
 */
public class DateinameplanEndsWithPdfIoxPlugin implements InterlisFunction {
    private LogEventFactory logger = null;
    
    @Override
    public Value evaluate(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        if (actualArguments[0].skipEvaluation()) {
            return actualArguments[0];
        }
        if (actualArguments[0].isUndefined()) {
            return Value.createSkipEvaluation();
        }
        
        String dateinameplanValue = actualArguments[0].getValue();                
        
        if (dateinameplanValue.toLowerCase().endsWith(".pdf")) {
            return new Value(true);
        } else {
            //logger.addEvent(logger.logErrorMsg("Die Dateiendung von 'Dateinameplan' ist nicht '.pdf'.", mainObj.getobjectoid()));
            return new Value(false);
        }
    }

    @Override
    public String getQualifiedIliName() {
        return "SO_AVGBS_FunctionsExt.dateinameplanEndsWithPdf";
    }

    @Override
    public void init(TransferDescription td, Settings settings, 
            IoxValidationConfig validationConfig, ObjectPool objectPool, 
            LogEventFactory logEventFactory) {

        logger=logEventFactory;
        logger.setValidationConfig(validationConfig);        
    }

}