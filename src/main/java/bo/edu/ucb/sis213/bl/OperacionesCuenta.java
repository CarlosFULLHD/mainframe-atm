package bo.edu.ucb.sis213.bl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OperacionesCuenta {
    private Connection connection;
    private int usuarioId;
    private double saldo;

    public OperacionesCuenta(Connection connection) {
        this.connection = connection;
    }

    public double getSaldo() {
        return saldo;
    }

    public void realizarDeposito(double cantidad) throws SQLException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad no válida.");
        }

        // Actualiza el saldo en la base de datos
        try (PreparedStatement updateStatement = connection
                .prepareStatement("UPDATE usuarios SET saldo = saldo + ? WHERE id = ?")) {
            updateStatement.setDouble(1, cantidad);
            updateStatement.setInt(2, usuarioId);
            updateStatement.executeUpdate();
        }

        // Registra la operación en el histórico
        registrarOperacionHistorico("Depósito", cantidad);

        saldo += cantidad;
    }

    public void realizarRetiro(double cantidad) throws SQLException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad no válida.");
        } else if (cantidad > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }

        // Actualiza el saldo en la base de datos
        try (PreparedStatement updateStatement = connection
                .prepareStatement("UPDATE usuarios SET saldo = saldo - ? WHERE id = ?")) {
            updateStatement.setDouble(1, cantidad);
            updateStatement.setInt(2, usuarioId);
            updateStatement.executeUpdate();
        }

        // Registra la operación en el histórico
        registrarOperacionHistorico("Retiro", cantidad);

        saldo -= cantidad;
    }

    public void registrarOperacionHistorico(String tipoOperacion, double cantidad) throws SQLException {
        try (PreparedStatement insertStatement = connection.prepareStatement(
                "INSERT INTO historicos (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)")) {
            insertStatement.setInt(1, usuarioId);
            insertStatement.setString(2, tipoOperacion);
            insertStatement.setDouble(3, cantidad);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void consultarSaldo() {
        System.out.println("Su saldo actual es: $" + saldo);
    }
}
