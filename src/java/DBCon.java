
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author olumidefatoki
 */
class DBCon {
    
    public DBCon() {
        getConnection();

    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
             conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/ussd","root", "root");

          /*  InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("jdbc/ussd");
            conn = ds.getConnection();*/
        } catch (Exception ex) {
            ex.printStackTrace();
            conn = null;
        }
        return conn;
    }
    
     public void closeConnection(PreparedStatement ps, Connection conn) {
         try {
            if (ps != null  ) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
String getSessionData(String sessionID, String msisdn) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sessionData = null;
        try {
            String sql = "SELECT value FROM test_ussd_session WHERE session_id = ? AND msisdn = ? ";
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, sessionID);
            ps.setString(2, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                sessionData = rs.getString(1).trim();
            }
            rs.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            closeConnection(ps, conn);

        }
        return sessionData;
    }

void insertUssdRequest(String session_id, String userdata, String msisdn, String network) {
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        try {
                String sql = "insert into  test_ussd_session (session_id, creation_date, value,msisdn,network)  values (?,NOW(),?,?,?) ";
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, session_id);
            ps.setString(2, userdata);
            ps.setString(3, msisdn);
            ps.setString(4, network);
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(ps, conn);
        }
    }

    void deleteRequest(String sessionid) {
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        try {
            String sql = "delete from  test_ussd_session   where session_id =? ";
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, sessionid);
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(ps, conn);
        }

    }
    void UpdateRequest(String value, String session_id) {
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        try {
            String sql = "Update   test_ussd_session  set value = ? where session_id = ? ";
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, value);
            ps.setString(2, session_id);
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(ps, conn);
        }

    }

}
