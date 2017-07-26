package kr.kein.getwww.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcUtils {
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    private static String url = "jdbc:mariadb://";
    private static String id = "";
    private static String pw = "";

    public JdbcUtils() {

        try {
            //드라이버 로딩 (Mysql 또는 Oracle 중에 선택하시면 됩니다.)
            //Class.forName("com.mysql.jdbc.Driver"); //mysql
            Class.forName("org.mariadb.jdbc.Driver"); //mariadb
            //Class.forName("oracle.jdbc.driver.OracleDriver"); //oracle
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getConnection() {

        try {
            //커넥션을 가져온다.
            con = DriverManager.getConnection(url, id, pw);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getData() {

        try {

            stmt = con.createStatement();
            //데이터를 가져온다.
            rs = stmt.executeQuery("select code1, day1, day2, price from stock_korea");

            while (rs.next()) {
                //출력
                System.out.println(rs.getString("code1"));
                System.out.println(rs.getDate("day1"));
                System.out.println(rs.getString("day2"));
                System.out.println(rs.getInt("price"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeConnection() {

        try {
            //자원 반환
            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//        public static void main(String[] args) {
//
//            JdbcExample jdbcExample = new JdbcExample();
//
//            jdbcExample.getConnection();
//            jdbcExample.getData();
//            jdbcExample.closeConnection();
//
//
//        }

}