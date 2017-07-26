package kr.kein.getwww.util;

import kr.kein.getwww.getwww;
import org.apache.commons.lang.StringUtils;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MariadbUtils {

    public static void loadDataFileToStockKorea(String filename) {

        Connection con = null;

        //PreparedStatement pstmt = null;
        Statement statement = null;
        String sql = "Insert Into stock_korea(code1, day1, day2, price, regdate) Values(?, ?, ?, ?, now())" +
                " ON DUPLICATE KEY UPDATE price = ? , regdate = now()";
        //String sql = "call py.insert_stock_korea(?, ?, ?, ?)";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://", "", "");

            statement = con.createStatement();
            statement.executeUpdate(
                    "LOAD DATA LOCAL INFILE '"+ filename +"' " +
                            "INTO TABLE stock_korea " +
                            "FIELDS TERMINATED BY '\t' " +
                            "LINES TERMINATED BY '\n' " +
                            "(code1, day1, day2, price, regdate)"
            );
            // 커밋
            con.commit();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try {
                statement.close();
                statement = null;
            } catch (SQLException ex) {
            }
            if (con != null) try {
                con.close();
                con = null;
            } catch (SQLException ex) {
            }
        }
    }


    public static void insertBatchStockKorea2(ArrayList<Object> objArr) {

        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "Insert Into stock_korea(code1, day1, day2, price, regdate) Values(?, ?, ?, ?, now())" +
                " ON DUPLICATE KEY UPDATE price = ? , regdate = now()";
        //String sql = "call py.insert_stock_korea(?, ?, ?, ?)";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://", "", "");

            pstmt = con.prepareStatement(sql);

            for (Object obj1 : objArr) {
                HashMap<String, Object> thisObj = (HashMap<String, Object>) obj1;

                pstmt.setString(1, (String) thisObj.get("code1"));
                pstmt.setTimestamp(2, (Timestamp) thisObj.get("day1"));
                pstmt.setString(3, (String) thisObj.get("day2"));
                //String thisPrice = (String) thisObj.get("price");
                //System.out.println("thisTime:"+thisObj.get("day1")
                //        + " // thisPrice:"+thisObj.get("price") + " // currTime:"+ DateUtils.getCurrTime());
                pstmt.setInt(4, (Integer) thisObj.get("price"));
                pstmt.setTimestamp(5, DateUtils.getCurrTimestamp());

                // addBatch에 담기
                pstmt.addBatch();

                // 파라미터 Clear
                pstmt.clearParameters();
            }

            // Batch 실행
            pstmt.executeBatch();
            // Batch 초기화
            pstmt.clearBatch();
            // 커밋
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();

            try {
                con.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        } finally {
            if (pstmt != null) try {
                pstmt.close();
                pstmt = null;
            } catch (SQLException ex) {
            }
            if (con != null) try {
                con.close();
                con = null;
            } catch (SQLException ex) {
            }
        }
    }



    public static void insertBatchStockKorea(ArrayList<Object> objArr) {

        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "Insert Into stock_korea(code1, day1, day2, price) Values(?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE code1 = ? ,day2 = ? ";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://", "", "");

            pstmt = con.prepareStatement(sql);

            for (Object obj1 : objArr) {
                HashMap<String, Object> thisObj = (HashMap<String, Object>) obj1;

                pstmt.setString(1, (String) thisObj.get("code1"));
                pstmt.setTimestamp(2, (Timestamp) thisObj.get("day1"));
                pstmt.setString(3, (String) thisObj.get("day2"));
                //String thisPrice = (String) thisObj.get("price");
                //System.out.println("thisTime:"+thisObj.get("day1") + " // thisPrice:"+thisObj.get("price"));
                pstmt.setInt(4, (Integer) thisObj.get("price"));
                pstmt.setString(5, (String) thisObj.get("code1"));
                pstmt.setString(6, (String) thisObj.get("day2"));

                // addBatch에 담기
                pstmt.addBatch();

                // 파라미터 Clear
                pstmt.clearParameters();
            }

            // Batch 실행
            pstmt.executeBatch();
            // Batch 초기화
            pstmt.clearBatch();
            // 커밋
            con.commit();

        } catch (Exception e) {
            e.printStackTrace();

            try {
                con.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        } finally {
            if (pstmt != null) try {
                pstmt.close();
                pstmt = null;
            } catch (SQLException ex) {
            }
            if (con != null) try {
                con.close();
                con = null;
            } catch (SQLException ex) {
            }
        }
    }

    public static void insertBatch() {

        Connection con = null;
        PreparedStatement pstmt = null ;

        String sql = "Insert Into stock_korea(code1, day1, day2, price) Values(?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE code1 = ? ,day2 = ? ";

        try{
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://", "", "");

            pstmt = con.prepareStatement(sql) ;

            for(int i=0; i < 10000; i++){
                int uid = 10000 + i ;
                String name = "홍길동_" + Integer.toString(i) ;
                String code1 = "test" + StringUtils.leftPad(String.valueOf(i), 10, "0");
                int age = i ;

                pstmt.setString(1, code1) ;
                pstmt.setTimestamp(2, java.sql.Timestamp.valueOf("2017-07-24 01:00:00"));
                pstmt.setString(3, "2017-07-24 01:00:00") ;
                pstmt.setInt(4, 5000);
                pstmt.setString(5, code1) ;
                pstmt.setString(6, "2017-07-24 01:00:00") ;

                // addBatch에 담기
                pstmt.addBatch();

                // 파라미터 Clear
                pstmt.clearParameters() ;


                // OutOfMemory를 고려하여 만건 단위로 커밋
                if( (i % 1000) == 0){
                    System.out.println("# "+ i + " memmory flushed!");
                    // Batch 실행
                    pstmt.executeBatch() ;

                    // Batch 초기화
                    pstmt.clearBatch();

                    // 커밋
                    con.commit() ;

                }
            }


            // 커밋되지 못한 나머지 구문에 대하여 커밋
            pstmt.executeBatch() ;
            con.commit() ;

        }catch(Exception e){
            e.printStackTrace();

            try {
                con.rollback() ;
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }finally{
            if (pstmt != null) try {pstmt.close();pstmt = null;} catch(SQLException ex){}
            if (con != null) try {con.close();con = null;} catch(SQLException ex){}
        }
    }

}