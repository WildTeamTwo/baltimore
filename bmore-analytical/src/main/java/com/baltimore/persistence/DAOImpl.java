package com.baltimore.persistence;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

/**
 * Created by paul on 10.09.18.
 */
public class DAOImpl {
    private static final String whereGeometryClause = " WHERE ST_AsText(coordinates) = ";
    private static final String whereAddressClause = " WHERE address = ";
    private static final String selectClause = " Select response from geocode ";
    private static final String selectClauseEncoded = " Select response_base64 from geocode ";
    private static final String insertGeometryClause = " insert ignore into geocode (coordinates, response_base64) ";
    private static final String insertAddressClause = " insert ignore into geocode (address, response_base64) ";
    private Connection connection;

    DAOImpl(Connection connection) {
        this.connection = connection;
    }

    public static DAOImpl init(String host, String port) throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/baltimore?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT", "bmore", "benutzenmaschine");
        return new DAOImpl(conn);
    }

    public static DAOImpl init() throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/baltimore?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT", "bmore", "benutzenmaschine");
        return new DAOImpl(conn);
    }

    private static String toValuesClause(String latitude, String longitude, String json) {
        String point = toPoint(latitude, longitude);
        point = point.replaceFirst("\u0020", "\u0020,");
        String responseCompressed = compressToBase64(json);
        return new StringBuilder(" values ").append(" ( ").append(point).append(",")
                .append("'").append(responseCompressed).append("'").append(" )").toString();

    }

    private static String toValuesClause(final String address, final String json) {
        String compressed = compressToBase64(json);
        return new StringBuilder(" values ").append(" ( ").append("'").append(escapeIfAny(address)).append("'").append(",")
                .append("'").append(compressed).append("'").append(" )").toString();

    }

    private static String compressToBase64(String json) {
        try {
            return Base64.getEncoder().encodeToString(json.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "UnsupportedEncodingException";
        }
    }

    private static String escapeIfAny(String string) {
        return string.replaceAll("'", "\\\\'");
    }

    private static String toWhereClause(String latitude, String longitude) {
        String point = toPoint(latitude, longitude);
        return new StringBuilder(whereGeometryClause).append("'").append(point).append("'").toString().trim();
    }

    private static String toWhereClause(String address) {
        return new StringBuilder(whereAddressClause).append("'").append(escapeIfAny(address)).append("'").toString().trim();
    }

    private static String toPoint(String latitude, String longitude) {
        return new StringBuilder("POINT").append("(").append(latitude).append("\u0020").append(longitude).append(")").toString();
    }

    public String readGeoCode(final String latitude, final String longitude) throws SQLException {
        Statement statement = connection.createStatement();
        String query = new StringBuilder(selectClause).append(toWhereClause(latitude, longitude)).toString();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            return rs.getString("response");
        }
        return null;

    }

    public String readGeoCodeEncoded(final String latitude, final String longitude) throws SQLException {
        Statement statement = connection.createStatement();
        String query = new StringBuilder(selectClauseEncoded).append(toWhereClause(latitude, longitude)).toString();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            return rs.getString("response_base64");
        }
        return null;

    }

    public String readGeoCodeEncoded(final String address) throws SQLException {
        Statement statement = connection.createStatement();
        String query = new StringBuilder(selectClauseEncoded).append(toWhereClause(address)).toString();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            return rs.getString("response_base64");
        }
        return null;

    }

    public boolean createGeoCode(final String latitude, final String longitude, final String json) throws SQLException {
        String query = null;
        try {
            Statement statement = connection.createStatement();
            query = new StringBuilder(insertGeometryClause).append(toValuesClause(latitude, longitude, json)).toString();
            int results = statement.executeUpdate(query);
            return results > 0 ? true : false;
        } catch (SQLException e) {
            e.addSuppressed(new SQLException("ERROR QUERY: " + query));
            throw e;
        }
    }

    public boolean createGeoCode(final String address, final String json) throws SQLException {
        String query = null;
        try {

            Statement statement = connection.createStatement();
            query = new StringBuilder(insertAddressClause).append(toValuesClause(address, json)).toString();
            int results = statement.executeUpdate(query);
            return results > 0 ? true : false;
        } catch (SQLException e) {
            e.addSuppressed(new SQLException("ERROR QUERY: " + query));
            throw e;
        }

    }

    void close() throws SQLException {
        connection.close();
    }
}
