package sample;

import com.jfoenix.controls.JFXComboBox;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

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
 //HelpMethod.Message(HelpMethod.WHITE,"JDBC Driver has been registered!");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //HelpMethod.Message(HelpMethod.RED, "Where is your MySQL JDBC Driver?");
            Logger.getLogger(HandlerDb.class.getName()).log(Level.SEVERE, null, e);
        }
 */

        HelpMethod.Message(COLORS.WHITE, "Робота з базою данних(З'єднання)");
        try {
            connection = DriverManager.getConnection(connectionString, "root", "");
        } catch (SQLException e) {
            HelpMethod.Message(COLORS.RED, "Connection Failed! Check output console" + e);
            e.printStackTrace();
        }
        return connection;
    }
    public static void autoIncZero(String table) {
        String query = "ALTER TABLE " + table + " AUTO_INCREMENT = 0;";
        HandlerDb.executeQuery(query);
    }

    public static void execute(PreparedStatement preparedStatement) {
        try {
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PreparedStatement getPreparedStatement(String query) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {            preparedStatement = connection.prepareStatement(query); }
        catch (SQLException e) {  e.printStackTrace();  }
        return preparedStatement;
    }
    public static PreparedStatement getPreparedStatement(String query, Object[] o) {
        if (o instanceof String[])  return getPreparedStatement(query, (String[]) o);
        if (o instanceof Integer[]) return getPreparedStatement(query, Arrays.stream(o).mapToInt(ob -> (int)ob).toArray());
        return null;
    }
    public static PreparedStatement getPreparedStatement(String query, String[] s) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < s.length; i++)
                preparedStatement.setString(i + 1, s[i]);
        }
        catch (SQLException e) {  e.printStackTrace();  }
        return preparedStatement;
    }
    public static PreparedStatement getPreparedStatement(String query, int[] x) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < x.length; i++)
                preparedStatement.setInt(i + 1, x[i]);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }
    public static PreparedStatement getPreparedStatement(String query,String[] s, int[] x) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < s.length; i++)
                preparedStatement.setString(1 + i, s[i]);
            for (int i = 0; i < x.length; i++)
                preparedStatement.setInt(s.length + i + 1, x[i]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    public static void executeQuery(String query) {
        PreparedStatement preparedStatement = getPreparedStatement(query);
        execute(preparedStatement);
    }
    public static void executeQuery(String query, Object[] o) {
        PreparedStatement preparedStatement = getPreparedStatement(query, o);
        execute(preparedStatement);
    }
    public static void executeQuery(String query,String[] s, int[] x) {
        PreparedStatement preparedStatement = getPreparedStatement(query, s, x);
        execute(preparedStatement);
    }


    public static ResultSet getResultSet(String query) {
        PreparedStatement preparedStatement = getPreparedStatement(query);
        ResultSet resultSet = null;
        try {  resultSet = preparedStatement.executeQuery(); }
        catch (SQLException e) { e.printStackTrace();  }
        return resultSet;
    }
    public static ResultSet getResultSet(String query, Object[] o) {
        PreparedStatement preparedStatement = getPreparedStatement(query, o);
        ResultSet resultSet = null;
        try {   resultSet = preparedStatement.executeQuery(); }
        catch (SQLException e) { e.printStackTrace();  }
        return resultSet;
    }

    public static List<Integer> getList(String query) {
        ResultSet resultSet = getResultSet(query);
        List<Integer> list = new ArrayList<>();
        try {
            while (resultSet.next())  list.add(resultSet.getInt(1));
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static boolean checkIsUnique(String query, Object[] o) {
        ResultSet resultSet = getResultSet(query, o);
        try {
            if (resultSet.next()) return false;
         return true;
        } catch (SQLException e) {
            return true;
            //e.printStackTrace();
        }
    }

    public static String getOneStr(String query, Object[] o) {
        ResultSet resultSet = getResultSet(query, o);
        String res = "";
        try {
            resultSet.last();
            res = resultSet.getString(1);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    private static String getAllStr(String query, Object[] o) {
        ResultSet resultSet = getResultSet(query, o);
        StringBuilder res = new StringBuilder("");
        try {
            resultSet.last();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int colNumber = rsmd.getColumnCount();
            for (int i = 0; i < colNumber; i++)
                res.append(resultSet.getString(i + 1) + ",");
            res.deleteCharAt(res.length() - 1);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    public static int getUserID(String log, String pass) {
        String query = "SELECT user_id FROM users_table WHERE  login = ? && hash_password = ?;";
        ResultSet resultSet = getResultSet(query, new String[]{log, pass});
        int res = -1;
        try {
            resultSet.last();
            res = resultSet.getInt(1);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int getOneValue(String query, Object[] o) {
        ResultSet resultSet = getResultSet(query, o);
        int res = -1;
        try {
            resultSet.last();
            res = resultSet.getInt(1);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

 /*   public static String[] getInformationStudent2(int user_id) {
        String query = "SELECT s.user_id, s.first_name, s.last_name, g.group_name, s.year, u.accessLevel " +
                "FROM students_table s LEFT JOIN groups_table g on s.group_id = g.group_id " +
                "LEFT JOIN users_table u ON u.user_id = s.user_id WHERE s.user_id = ?;";
        String inf = HandlerDb.getAllStr(query, new Integer[]{user_id});
        String[] res = inf.split(",");
        return res;
    }*/

    public static Map getInformationUser(int user_id) {
        String query = "SELECT accessLevel FROM users_table WHERE user_id = ?";
        int access = getOneValue(query, new Integer[]{user_id});
        String[] res = null;
        Map<String, String> information = new HashMap<>();
        information.put("Access", Integer.toString(access));
        StringBuilder name = new StringBuilder();

        if (access == 0) {
            query = "SELECT student_id, s.first_name, s.last_name, g.group_name, s.year " +
                    "FROM students_table s LEFT JOIN groups_table g on s.group_id = g.group_id " +
                    "LEFT JOIN users_table u ON u.user_id = s.user_id WHERE s.user_id = ?;";
            String inf = HandlerDb.getAllStr(query, new Integer[]{user_id});
            res = inf.split(",");
            information.put("Group", res[3]);
            information.put("Year", res[4]);
        }
        else {
            query = "SELECT teacher_id, first_name, last_name FROM teachers_table WHERE user_id = ?;";
            if (checkIsUnique(query, new Integer[]{user_id})) {
                    information.put("Id", "");
                    information.put("Name", "Невідомий");
                    return information;
            } else {
                String inf = HandlerDb.getAllStr(query, new Integer[]{user_id});
                res = inf.split(",");
            }
        }

        information.put("Id", res[0]);
        information.put("Name", name.append(res[1] + " " + res[2]).toString());
        return information;
    }

    public static ResultSet fillComboBox(JFXComboBox comboBox, String query, int k, int z) {
        ResultSet resultSet = getResultSet(query);
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}