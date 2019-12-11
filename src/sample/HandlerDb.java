package sample;

import com.jfoenix.controls.JFXComboBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class HandlerDb {
    //protected  Connection connection;
    public static Connection getConnection() {
        Connection connection = null;
        final String connectionString = "jdbc:mysql://localhost:3306/managment?" +
                "serverTimezone=UTC&autoReconnect=true" +
                "&useSSL=false" +
                "&useUnicode=true&characterEncoding=UTF-8";
        //useUnicode=true&characterEncoding=UTF-8   для відображеня кирилиці у базі даних.
        /*
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            HelpMethod.Message(HelpMethod.RED, "Where is your MySQL JDBC Driver?");
            Logger.getLogger(HandlerDb.class.getName()).log(Level.SEVERE, null, e);
        }

         */
        //HelpMethod.Message(HelpMethod.WHITE,"JDBC Driver has been registered!");
        HelpMethod.Message(HelpMethod.WHITE, "Робота з базою данних");
        try {
            connection = DriverManager.getConnection(connectionString, "root", "");
        } catch (SQLException e) {
            HelpMethod.Message(HelpMethod.RED, "Connection Failed! Check output console" + e);
            e.printStackTrace();
        }
        return connection;
    }

    public static void autoIncZero(String table) {
        String query = "ALTER TABLE " + table + " AUTO_INCREMENT = 0;";
        HandlerDb.executeQuery(query);
    }

    public static void executeQuery(String query) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeQuery(String query, String[] s) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < s.length; i++) {
                preparedStatement.setString(i + 1, s[i]);
            }
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeQuery(String query,String[] s, int[] x) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < s.length; i++)
                preparedStatement.setString(1 + i, s[i]);
            for (int i = 0; i < x.length; i++)
                preparedStatement.setInt(s.length + i + 1, x[i]);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Integer> getList(String query) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Integer> list = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getInt(1));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void executeQuery(String query, int[] x) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < x.length; i++) {
                preparedStatement.setInt(1 + i, x[i]);
            }
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean checkIsUnique(String query, int[] a) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < a.length; i++)
                preparedStatement.setInt(i + 1, a[i]);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return false;
         return true;
        } catch (SQLException e) {
            return true;
            //e.printStackTrace();
        }
    }

    public static boolean checkIsUnique(String query, String[] a) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < a.length; i++) {
                preparedStatement.setString(i + 1, a[i]);
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return false;
            return true;
        } catch (SQLException e) {
            return true;
            //e.printStackTrace();
        }
    }
    public static String getOneStr(String query, String[] s) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String res = "";
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < s.length; i++)
                preparedStatement.setString(i + 1, s[i]);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            res = resultSet.getString(1);
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String getOneStr(String query, int[] x) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String res = "";
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < x.length; i++)
                preparedStatement.setInt(i + 1, x[i]);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            res = resultSet.getString(1);
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int getOneValue(String query, String[] s) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int res = -1;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < s.length; i++)
                preparedStatement.setString(i + 1, s[i]);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            res = resultSet.getInt(1);
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int getOneValue(String query, int[] x) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int res = -1;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < x.length; i++)
                preparedStatement.setInt(i + 1, x[i]);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            res = resultSet.getInt(1);
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static ResultSet fillComboBox(JFXComboBox comboBox, String query, int k, int z) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StringBuilder res = new StringBuilder("");
                for (int i = 1; i < 1 + k; i++) {
                    res.append(resultSet.getInt(i) + " ");
                }
                for (int i = 1 + k; i < 1 + k + z; i++) {
                    res.append(resultSet.getString(i) + " ");
                }
                comboBox.getItems().add(res.toString());
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}

