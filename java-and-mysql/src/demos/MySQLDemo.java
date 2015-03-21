package demos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDemo {

	private Connection conn;

	public static void main(String args[]) {
		new MySQLDemo();
	}

	public MySQLDemo() {
		// Verbindung aufbauen
		setUpConnection();
		// Abfrage senden und Ergebnisse ausgeben
		testQuery();
		// Datensatz einfuegen
		testInsertQuery();
	}

	public void testQuery() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet resultSet = stmt
					.executeQuery("SELECT buchung_id, flug_id, preis FROM buchung LIMIT 100;");
			while (resultSet.next()) {
				System.out.println("Buchung " + resultSet.getInt("buchung_id")
						+ " auf Flug " + resultSet.getInt("flug_id") + " um "
						+ resultSet.getDouble("preis") + " EUR");
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
		}
	}

	public void testInsertQuery() {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO fluglinie (iata,firmenname,heimat_flughafen) VALUES (?,?,?)");
			stmt.setString(1, "MF");
			stmt.setString(2, "Meine Fluglinie");
			stmt.setInt(3, 1);
			stmt.execute();
			stmt.close();
			Statement stmtLast = conn.createStatement();
			ResultSet resultSet = stmtLast
					.executeQuery("SELECT LAST_INSERT_ID() as id");
			resultSet.first();
			System.out.println("Id meiner Fluglinie " + resultSet.getInt("id"));
			resultSet.close();
			stmtLast.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
		}
	}

	public void setUpConnection() {
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/FlughafenDB", "kuf",
					"lukas");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
		}
	}

}
