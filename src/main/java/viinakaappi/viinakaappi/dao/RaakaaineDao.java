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

public class RaakaaineDao implements Dao<Raakaaine, Integer> {

    private Database database;

    public RaakaaineDao(Database database) {
        this.database = database;
    }

    @Override
    public Raakaaine findOne(Integer key) throws SQLException {
        String query = "SELECT ID, nimi, saldo FROM Raakaaine WHERE ID = ?";

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
    public List<Raakaaine> findAll() throws SQLException {
        List<Raakaaine> ra = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet result = conn.prepareStatement(
                        "SELECT ID, nimi, saldo FROM Raakaaine"
                ).executeQuery()) {

            while (result.next()) {
                ra.add(createFromRow(result));
            }
        }

        return ra;
    }

    public Raakaaine createFromRow(ResultSet resultSet) throws SQLException {
        return new Raakaaine(resultSet.getInt("ID"), resultSet.getString("nimi"), resultSet.getInt("saldo"));
    }

    @Override
    public Raakaaine saveOrUpdate(Raakaaine object) throws SQLException {
        if (object.getNimi().equals("")) {
            System.out.println("Tyhjä");
            return null;
        }
        Raakaaine ra = findByName(object.getNimi());
        if (ra != null) {
            try (Connection conn = database.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE Raakaaine SET saldo = ? WHERE id = ?");
                stmt.setInt(1, object.getSaldo());
                stmt.setInt(2, object.getId());
                stmt.executeUpdate();
            }
            return null;
        }
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Raakaaine (nimi, saldo) VALUES (?, ?)");
            stmt.setString(1, object.getNimi());
            stmt.setInt(2, 0);
            stmt.executeUpdate();
        }
        return null;

    }

    @Override
    public void delete(Integer key) throws SQLException {
        String query = "DELETE FROM Raakaaine WHERE ID = ?";
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }

    private Raakaaine findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi, saldo FROM Raakaaine WHERE nimi = ?");
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
