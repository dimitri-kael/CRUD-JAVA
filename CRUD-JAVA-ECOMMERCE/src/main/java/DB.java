import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author estevaolins
 * Classe responsável pela conexão com o banco de dados MySQL.
 * Fornece métodos para estabelecer e fechar a conexão.
 */
public class DB {

    private final String url = "jdbc:mysql://localhost:3307/catolica";
    private final String usuario = "root";
    private final String senha = System.getenv("DB_PASSWORD"); // Usando variável de ambiente

    /**
     * Estabelece uma conexão com o banco de dados MySQL.
     * @author estevaolins
     * @return A conexão estabelecida, ou {@code null} se ocorrer um erro.
     * @throws SQLException se ocorrer um erro ao conectar ao banco de dados.
     */
    public Connection conectarDB() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexão estabelecida com sucesso!");
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("Driver não encontrado: " + e.getMessage());
            throw new SQLException("Erro ao carregar o driver de conexão", e);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw e; // Propaga a exceção para tratamento em outros níveis
        }
    }

    /**
     * Fecha a conexão com o banco de dados, caso esteja aberta.
     *
     * @author estevaolins
     * @param connection A conexão a ser fechada.
     */
    public void fecharConexao(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada.");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
