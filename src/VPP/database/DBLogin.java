package VPP.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBLogin {

    public static boolean checkdn(String user, String pass) {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";

        try (Connection con = ketnoidb.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean checkdk(String username) {
        String sql = "SELECT * FROM account WHERE username = ?";

        try (Connection con = ketnoidb.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean chenuser(String username, String password) {
        String sql = "INSERT INTO account(username, password) VALUES (?, ?)";

        try (Connection con = ketnoidb.getConnection();
             PreparedStatement ps1 = con.prepareStatement(sql)) {

            ps1.setString(1, username);
            ps1.setString(2, password);
            return ps1.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
