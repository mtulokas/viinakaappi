package viinakaappi.viinakaappi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import viinakaappi.viinakaappi.database.Database;
import viinakaappi.viinakaappi.domain.Raakaaine;

public class DrinkkiRaakaaineDao {

    private Database database;

    public DrinkkiRaakaaineDao(Database database) {
        this.database = database;
    }

    public List<Raakaaine> findAllByDrinkkiId(int drinkki_id) throws SQLException {
        List<Raakaaine> ra = new ArrayList<>();
        //String query = "SELECT id, nimi FROM Raakaaine WHERE id IN (SELECT raakaaine_id FROM DrinkkiRaakaaine WHERE drinkki_id = ?)";
        String query = "SELECT Raakaaine.id, Raakaaine.nimi, maara FROM DrinkkiRaakaaine LEFT JOIN Raakaaine ON raakaaine_id = id WHERE drinkki_id = ? ORDER BY jarjestys";

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, drinkki_id);
            try (ResultSet result = stmt.executeQuery()) {
                while (result.next()) {
                    ra.add(createFromRow(result));
                }
            }
        }
        return ra;
    }

    public Raakaaine createFromRow(ResultSet resultSet) throws SQLException {
        return new Raakaaine(resultSet.getInt("ID"), resultSet.getString("nimi"), resultSet.getString("maara"));
    }

    public Raakaaine insertRow(int drinkki_id, int raakaaine_id, String maara) throws SQLException {
        String testQuery = "SELECT drinkki_id, raakaaine_id FROM DrinkkiRaakaaine WHERE drinkki_id = ? AND raakaaine_id = ?";
        String insertQuery = "INSERT INTO DrinkkiRaakaaine (drinkki_id, raakaaine_id, maara) VALUES (?,?,?)";

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(testQuery);
            stmt.setInt(1, drinkki_id);
            stmt.setInt(2, raakaaine_id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                System.out.println("Rivi löytyy jo!");
                return null;
            }
            System.out.println("Lisätään rivi");
            stmt = conn.prepareStatement(insertQuery);
            stmt.setInt(1, drinkki_id);
            stmt.setInt(2, raakaaine_id);
            stmt.setString(3, maara);
            stmt.executeUpdate();
        }

        return null;

    }

    public void moveUp(int drinkki_id, int raakaaine_id) throws SQLException {
        String testQuery = "SELECT jarjestys FROM DrinkkiRaakaaine WHERE drinkki_id = ? AND raakaaine_id = ?";
        String updateQuery = "UPDATE DrinkkiRaakaaine SET jarjestys = ? WHERE drinkki_id = ? AND raakaaine_id = ?";

        int nykyinenJarjestys = 0;

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(testQuery);
            stmt.setInt(1, drinkki_id);
            stmt.setInt(2, raakaaine_id);
            try (ResultSet result = stmt.executeQuery()) {
                while (result.next()) {
                    nykyinenJarjestys = result.getInt("jarjestys");
                }
            }
            System.out.println("Nykyinen on " + nykyinenJarjestys);
            nykyinenJarjestys--;
            System.out.println("uusi on " + nykyinenJarjestys);
            stmt = conn.prepareStatement(updateQuery);
            stmt.setInt(1, nykyinenJarjestys);
            stmt.setInt(2, drinkki_id);
            stmt.setInt(3, raakaaine_id);
            stmt.executeUpdate();
        }

    }

    public void delete(int drinkki_id) throws SQLException {
        String query = "DELETE FROM DrinkkiRaakaaine WHERE drinkki_id = ?";

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, drinkki_id);
            stmt.executeUpdate();
        }
    }

    public void delete(int drinkki_id, int raakaaine_id) throws SQLException {
        String query = "DELETE FROM DrinkkiRaakaaine WHERE drinkki_id = ? AND raakaaine_id = ?";

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, drinkki_id);
            stmt.setInt(2, raakaaine_id);
            stmt.executeUpdate();
        }
    }

}
