package ch.so.agi.ilivalidator.ext.nplso;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;

public class IsReferencingDocumentIoxPluginTest {
    private TransferDescription td=null;
    // OID
    private final static String OBJ_OID1 ="o1";
    private final static String OBJ_OID2 ="o2";
    private final static String OBJ_OID3 ="o3";
    private final static String OBJ_OID4 ="o4";
    private final static String OBJ_OID5 ="o5";
    private final static String OBJ_OID6 ="o6";
    private final static String OBJ_OID7 ="o7";
    private final static String OBJ_OID8 ="o8";
    private final static String OBJ_OID9 ="o9";
    private final static String OBJ_OID10 ="o10";
    private final static String OBJ_OID11 ="o11";
    private final static String OBJ_OID12 ="o12";
    private final static String OBJ_OID13 ="o13";
    private final static String OBJ_OID14 ="o14";
    // MODEL
    private final static String ILI_TOPIC1="OEREB.Vorschriften";
    private final static String ILI_TOPIC2="OEREB.Nutzungsplanung";
    // CLASS
    private final static String ILI_CLASSA=ILI_TOPIC1+".Dokument";
    private final static String ILI_CLASSB=ILI_TOPIC2+".Typ_Grundnutzung";
    private final static String ILI_CLASSC=ILI_TOPIC2+".Grundnutzung";
    // ASSOCIATION
    private final static String ILI_ASSOC_A_A=ILI_TOPIC1+".HinweisWeitereDokumente";
    private final static String ILI_ASSOC_AA_A_URSPRUNG = "Ursprung"; 
    private final static String ILI_ASSOC_AA_A_HINWEIS = "Hinweis"; 
    private final static String ILI_ASSOC_B_C = ILI_TOPIC2+".Typ_Grundnutzung_Grundnutzung";    
    // START BASKET EVENT
    private final static String BID1="b1";
    private final static String BID2="b2";

    @Before
    public void setUp() throws Exception {
        Configuration ili2cConfig = new Configuration();
        {
            FileEntry fileEntry = new FileEntry("src/test/data/SO_NPLSO_FunctionsExt.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
        }
        {
            FileEntry fileEntry = new FileEntry("src/test/data/NPLSO-mini.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry); 
        }
        td = ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    
    @Test
    public void fubar() {        
        Iom_jObject iomObjTyp=new Iom_jObject(ILI_CLASSB, OBJ_OID2);
        iomObjTyp.setattrvalue("Bezeichnung", "N111");

        Iom_jObject iomObjGeometry=new Iom_jObject(ILI_CLASSC, OBJ_OID1);
        iomObjGeometry.setattrvalue("Bemerkungen", "Geometrieobjekt");
        iomObjGeometry.addattrobj(ILI_ASSOC_B_C, "REF").setobjectrefoid(iomObjTyp.getobjectoid());
        
        System.out.println(iomObjGeometry);

    }

}
