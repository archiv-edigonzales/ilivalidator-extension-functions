package ch.so.agi.ilivalidator.ext;

import static org.junit.Assert.assertNotNull;

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

public class HasUniqueAttributePairsIoxPluginTest {
    private TransferDescription td=null;
    // OID
    private final static String OBJ_OID1 ="o1";
    private final static String OBJ_OID2 ="o2";
    private final static String OBJ_OID3 ="o3";
    // MODEL
    private final static String ILI_TOPIC="SO_FunctionsExt.Topic";
    // CLASS
    private final static String ILI_CLASSB=ILI_TOPIC+".ClassB";
    // START BASKET EVENT
    private final static String BID1="b1";

    @Before
    public void setUp() throws Exception {
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry("src/test/data/SO_FunctionsExt.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        td = ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);
    }

    @Test
    public void dummy() throws Exception {
        Iom_jObject iomObjA = new Iom_jObject(ILI_CLASSB, OBJ_OID1);
        iomObjA.setattrvalue("attr1", "foo");
        iomObjA.setattrvalue("attr2", "bar");
        Iom_jObject iomObjB = new Iom_jObject(ILI_CLASSB, OBJ_OID2);
        iomObjB.setattrvalue("attr1", "ying");
        iomObjB.setattrvalue("attr2", "yang");
        Iom_jObject iomObjC = new Iom_jObject(ILI_CLASSB, OBJ_OID3);
        iomObjC.setattrvalue("attr1", "bar");
        iomObjC.setattrvalue("attr2", "foo");

        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.mergeIliMetaAttrs(td);
        LogCollector logger = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        Settings settings = new Settings();
        Map<String,Class> newFunctions = new HashMap<String,Class>();
        newFunctions.put("SO_FunctionsExt.hasUniqueAttributePairs", HasUniqueAttributePairsIoxPlugin.class);
        settings.setTransientObject(Validator.CONFIG_CUSTOM_FUNCTIONS, newFunctions);
        Validator validator=new Validator(td, modelConfig, logger, errFactory, new PipelinePool(), settings);
        validator.validate(new StartTransferEvent());
        validator.validate(new StartBasketEvent(ILI_TOPIC,BID1));
        validator.validate(new ObjectEvent(iomObjA));
        validator.validate(new ObjectEvent(iomObjB));
        validator.validate(new ObjectEvent(iomObjC));
        validator.validate(new EndBasketEvent());
        validator.validate(new EndTransferEvent());


    }
}
