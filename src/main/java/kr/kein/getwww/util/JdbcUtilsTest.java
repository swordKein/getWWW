package kr.kein.getwww.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.*;
import org.junit.runners.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class JdbcUtilsTest {

    @Test
    public void getData() throws Exception {
    	JdbcUtils.getConnection();
		JdbcUtils.getData();
		JdbcUtils.closeConnection();
    }

}