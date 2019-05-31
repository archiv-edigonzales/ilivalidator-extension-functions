package ch.so.agi.ilivalidator.ext.avgbs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ValidationConfig;
import ch.interlis.iox_j.validator.Validator;
import ch.so.agi.ilivalidator.ext.LogCollector;

public class PdfnameMatchesDateinameplanIoxPluginTest {
    private TransferDescription td=null;
    // OID
    private final static String OBJ_OID1 ="o1";
    // MODEL
    private final static String ILI_TOPIC="GB2AV.Mutationstabelle";
    // CLASS
    private final static String ILI_CLASSA=ILI_TOPIC+".AVMutation";
    // START BASKET EVENT
    private final static String BID1="b1";
    
    public final String SETTING_PDFFILE="ch.so.agi.ilivalidator.ext.avgbs.pdffile";

    @Before
    public void setUp() throws Exception {
        Configuration ili2cConfig = new Configuration();
        {
            FileEntry fileEntry = new FileEntry("src/test/data/SO_AVGBS_FunctionsExt.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
        }
        {
            FileEntry fileEntry = new FileEntry("src/test/data/GB2AV-mini.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry); 
        }
        td = ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    @Test
    public void pdfnameMatchesDateinameplan_Ok() {
        Iom_jObject iomObjA = new Iom_jObject(ILI_CLASSA, OBJ_OID1);
        iomObjA.setattrvalue("Beschrieb", "Vereinigung.");
        iomObjA.setattrvalue("DateinamePlan", "SO0200002401_1622_20190416.pdf");
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Map<String,Class> newFunctions = new HashMap<String,Class>();
        newFunctions.put("SO_AVGBS_FunctionsExt.pdfnameMatchesDateinameplan", PdfnameMatchesDateinameplanIoxPlugin.class);
        settings.setTransientObject(Validator.CONFIG_CUSTOM_FUNCTIONS, newFunctions);
        settings.setValue(SETTING_PDFFILE, "SO0200002401_1622_20190416.pdf");
        Validator validator=new Validator(td, modelConfig, logger, errFactory, new PipelinePool(), settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());

        assertTrue(logger.getErrs().size()==0);
    }
    
    @Test
    public void pdfnameMatchesDateinameplan_Fail() {
        Iom_jObject iomObjA = new Iom_jObject(ILI_CLASSA, OBJ_OID1);
        iomObjA.setattrvalue("Beschrieb", "Vereinigung.");
        iomObjA.setattrvalue("DateinamePlan", "SO0200002401_1622_20190416.pdf");
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Map<String,Class> newFunctions = new HashMap<String,Class>();
        newFunctions.put("SO_AVGBS_FunctionsExt.pdfnameMatchesDateinameplan", PdfnameMatchesDateinameplanIoxPlugin.class);
        settings.setTransientObject(Validator.CONFIG_CUSTOM_FUNCTIONS, newFunctions);
        settings.setValue(SETTING_PDFFILE, "XX0200002401_1622_20190416.pdf");
        Validator validator=new Validator(td, modelConfig, logger, errFactory, new PipelinePool(), settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());

        assertTrue(logger.getErrs().size()==2);
        assertTrue(logger.getErrs().get(0).getEventMsg().equals("SO0200002401_1622_20190416.pdf <-> XX0200002401_1622_20190416.pdf"));
        assertTrue(logger.getErrs().get(1).getEventMsg().equals("Dateinameplan 'SO0200002401_1622_20190416.pdf' im XML passt nicht zum Namen des gelieferten PDF."));
    }
}
