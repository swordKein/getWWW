package test.main.java.com.unus.beans.getwww_mod1.util;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import com.google.gson.Gson;

import main.java.com.unus.beans.getwww_mod1.util.AES256Util;
import main.java.com.unus.beans.getwww_mod1.util.DateUtils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class AES256UtilTest {

    @Test
    public void testEncDecTest() throws Exception {
    	String key = "sdjnfio2390dsvjklwwe90jf2";
    	AES256Util aes256 = new AES256Util(key);
    	
    	String orgText = "SMT SKT";
    	String encText = aes256.aesEncode(orgText);
    	String decText = aes256.aesDecode(encText);
    	System.out.println(" result ::   orgText : "+orgText+" :: encText : "+encText+" :: decText : "+decText);
    }
    
    @Test
    public void testGetPassTime() throws Exception {
    	String sDate = "";
    	String eDate = "20151019112159";
    	long diffTime = DateUtils.getPassTime2(sDate, eDate);
    	System.out.println(" result txt :: " + diffTime);
    }
}