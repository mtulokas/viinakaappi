package viinakaappi.viinakaappi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import viinakaappi.viinakaappi.database.Database;
import viinakaappi.viinakaappi.domain.Drinkki;

public class DrinkkiDao implements Dao<Drinkki, Integer> {

    private Database database;

    public DrinkkiDao(Database database) {
        this.database = database;
    }

    @Override
    public Drinkki findOne(Integer key) throws SQLException {
        String query = "SELECT ID, Nimi, ohje FROM Drinkki WHERE ID = ?";

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, key);
            try (ResultSet result = stmt.executeQuery()) {
                while (result.next()) {
                    return createFromRow(result);
                }
            }
        }
        return null;
    }

    @Override
    public List<Drinkki> findAll() throws SQLException {
        List<Drinkki> headers = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet result = conn.prepareStatement(
                        "SELECT ID, nimi, ohje FROM Drinkki"
                ).executeQuery()) {

            while (result.next()) {
                headers.add(createFromRow(result));
            }
        }

        return headers;
    }
    
        public List<Drinkki> findDrinksWithAllIngredients() throws SQLException {
        List<Drinkki> headers = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet result = conn.prepareStatement(
                        "SELECT ID, nimi, ohje FROM Drinkki WHERE id NOT IN (SELECT drinkki_id FROM DrinkkiRaakaaine\n" +
                        "LEFT JOIN Raakaaine ON Raakaaine.id = DrinkkiRaakaaine.raakaaine_id\n" +
                        "WHERE saldo = 0)"
                ).executeQuery()) {

            while (result.next()) {
                headers.add(createFromRow(result));
            }
        }

        return headers;
    }

    public Drinkki createFromRow(ResultSet resultSet) throws SQLException {
        return new Drinkki(resultSet.getInt("ID"), resultSet.getString("nimi"), resultSet.getString("ohje"));
    }

    @Override
    public Drinkki saveOrUpdate(Drinkki object) throws SQLException {
        if (object.getNimi().equals("")){
            System.out.println("Tyhjä");
            return null;
        }
        
        Drinkki dr = findByName(object.getNimi());
        if (dr != null) {
            if (object.getOhje() != null && !object.getOhje().isEmpty()) {
                try (Connection conn = database.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement("UPDATE Drinkki SET ohje = ? WHERE ID = ?");
                    stmt.setString(1, object.getOhje());
                    stmt.setInt(2, object.getId());
                    stmt.executeUpdate();
                }
            } 
            return null;
        }
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Drinkki (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }
        return null;

    }

    @Override
    public void delete(Integer key) throws SQLException {
        String query = "DELETE FROM Drinkki WHERE ID = ?";
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }

    private Drinkki findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi, ohje FROM Drinkki WHERE nimi = ?");
            stmt.setString(1, nimi);

            try (ResultSet result = stmt.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                return createFromRow(result);
            }
        }
    }
}
