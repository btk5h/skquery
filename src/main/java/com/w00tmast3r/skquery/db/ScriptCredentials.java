package com.w00tmast3r.skquery.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class ScriptCredentials {

    private static HashMap<File, ScriptCredentials> credentials = new HashMap<File, ScriptCredentials>();
    private String url = null;
    private String username = null;
    private String password = null;
    private Connection connection = null;

    private ScriptCredentials() {

    }

    public static void setURL(File script, String url) {
        ScriptCredentials sc = get(script);
        sc.url = url;
        sc.validate();
    }

    public static void setUsername(File script, String username) {
        ScriptCredentials sc = get(script);
        sc.username = username;
        sc.validate();
    }

    public static void setPassword(File script, String password) {
        ScriptCredentials sc = get(script);
        sc.password = password;
        sc.validate();
    }

    public Connection getConnection() {
        return connection;
    }

    public static ScriptCredentials get(File script) {
        if (!credentials.containsKey(script)) credentials.put(script, new ScriptCredentials());
        ScriptCredentials c = credentials.get(script);
        try {
            if (c.connection != null && !c.connection.isValid(1)) {
                 c.validate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void clear() {
        for (ScriptCredentials sc : credentials.values()) {
            if (sc.connection != null) try {
                sc.connection.close();
            } catch (SQLException ignored) {

            }
        }
    }

    private void validate() {
        if (url != null && username != null && password != null) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
