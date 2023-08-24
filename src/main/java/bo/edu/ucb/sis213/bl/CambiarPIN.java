package bo.edu.ucb.sis213.bl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CambiarPIN {
    private Connection connection;
    private int usuarioId;

    public CambiarPIN(Connection connection) {
        this.connection = connection;
    }

    public void cambiarPIN(int pinIngresado, int nuevoPin, int confirmacionPin) throws SQLException {
        try (PreparedStatement updateStatement = connection
                .prepareStatement("UPDATE usuarios SET pin = ? WHERE id = ?")) {
            updateStatement.setInt(1, nuevoPin);
            updateStatement.setInt(2, usuarioId);
            updateStatement.executeUpdate();
        }

    }
}
