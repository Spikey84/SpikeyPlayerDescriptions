package me.spikey.playerdescriptions;

import com.google.common.collect.Maps;
import me.spikey.playerdescriptions.utils.UUIDUtils;
import org.bukkit.plugin.Plugin;

import javax.xml.transform.Result;
import java.io.File;
import java.io.StreamCorruptedException;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseManager {
    private static File databaseFile;
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void initDatabase(Plugin plugin) {

        File databaseFolder = new File(plugin.getDataFolder(), "core.db");
        if (!databaseFolder.exists()) {
            try {
                databaseFolder.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        databaseFile = databaseFolder;
    }

    public static void create(Connection connection) {
        try {
            Statement statement = null;
            Class.forName("org.sqlite.JDBC");

            statement = connection.createStatement();
            String query = """
                    CREATE TABLE IF NOT EXISTS traits (\
                    uuid VARCHAR NOT NULL,\
                    element VARCHAR NOT NULL,\
                    option VARCHAR NOT NULL\
                    );
                    """;

            statement.execute(query);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void add(Connection connection, UUID uuid, String element, String option) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO traits (uuid, element, option) VALUES (?,?,?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.setString(2, element);
            statement.setString(3, option);

            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void remove(Connection connection, UUID uuid, String element) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM traits WHERE uuid=? AND element=?;
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.setString(2, element);

            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> get(Connection connection, UUID uuid) {
        PreparedStatement statement = null;
        HashMap<String, String> elementOption = Maps.newHashMap();
        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    SELECT * FROM traits WHERE uuid=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                elementOption.put(resultSet.getString("element"), resultSet.getString("option"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return elementOption;
    }
}
