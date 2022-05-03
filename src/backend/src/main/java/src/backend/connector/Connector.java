package src.backend.connector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class Connector {
    Connection connection = null;

    private String driver;
    private String username;
    private String password;
    private String databaseName;

    public Connector(@Value("${connector.driver}") String driver,
                     @Value("${connector.username}") String username,
                     @Value("${connector.password}") String password,
                     @Value("${connector.databaseName}") String databaseName) {
        try {
            this.driver = driver;
            this.username = username;
            this.password = password;
            this.databaseName = databaseName;
            Class.forName(driver);
            connection = DriverManager.getConnection("jdbc:mysql://dijkstra.ug.bcc.bilkent.edu.tr/" + databaseName, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<HashMap<String, Object>> executeQuery(String sql) {
        try {
            Statement statement = connection.createStatement();
            return parseToHashMap(statement.executeQuery(sql));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    public void executeUpdate(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    private List<HashMap<String, Object>> parseToHashMap(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<String, Object>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }
}
