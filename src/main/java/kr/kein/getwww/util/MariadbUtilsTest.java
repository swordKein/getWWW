package kr.kein.getwww.util;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class MariadbUtilsTest {

    @Test
    public void insertBatch() throws Exception {
    	MariadbUtils.insertBatch();
    }

    @Test
    public void selectGeoData() throws Exception {
        System.out.println("result:"+MariadbUtils.selectGeoData(1, 10).toString());
    }

}