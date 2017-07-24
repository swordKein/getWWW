package kr.kein.getwww.util;

import org.junit.*;
import com.google.gson.Gson;

import kr.kein.getwww.util.DateUtils;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class DateUtilsTest{

    @Test
    public void testGetRemoveStringBySet() throws Exception {
    	String thisTime = DateUtils.getLocalDateTime();
    	System.out.println(" result txt :: " + thisTime);
    }
    
    @Test
    public void testGetPassTime() throws Exception {
    	String sDate = "";
    	String eDate = "20151019112159";
    	long diffTime = DateUtils.getPassTime2(sDate, eDate);
    	System.out.println(" result txt :: " + diffTime);
    }
}