package bo.edu.ucb.sis213.bl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VerificacionUsuario {
    private Connection connection;
    private int usuarioId;
    public double saldo;

    public VerificacionUsuario(Connection connection) {
        this.connection = connection;
    }

    public boolean validarPIN(String alias, int pin) {
        String query = "SELECT id, saldo FROM usuarios WHERE alias = ? AND pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, alias);
            preparedStatement.setInt(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usuarioId = resultSet.getInt("id");
                saldo = resultSet.getDouble("saldo");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int obtenerPinDesdeDB() {
        String query = "SELECT pin FROM usuarios WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Valor sentinela para indicar que no se pudo obtener el PIN
    }
}
