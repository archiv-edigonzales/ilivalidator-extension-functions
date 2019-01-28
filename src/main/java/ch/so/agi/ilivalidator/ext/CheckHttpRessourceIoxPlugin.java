package ch.so.agi.ilivalidator.ext;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        System.err.println("evaluate");

        if (actualArguments[0].skipEvaluation()) {
            return actualArguments[0];
        }
        if (actualArguments[0].isUndefined()) {
            return Value.createSkipEvaluation();
        }

        String value = actualArguments[0].getValue();
        
        try {
            URL siteURL = new URL(value);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            // HEAD does not work in our environment and returns a 405 status code. It seems a SES thing.
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.connect();
    
            int responseCode = connection.getResponseCode();

            System.err.println("*********");
            System.err.println(value);
            System.err.println(responseCode);

            if (200 <= responseCode && responseCode <= 399) {
                return new Value(true); 
            } else {
                return new Value(false);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            logger.addEvent(logger.logErrorMsg(e.getMessage()));
            return new Value(false);
        } 
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
