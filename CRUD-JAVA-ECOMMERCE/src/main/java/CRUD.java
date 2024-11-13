import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author dimitriKael
 * Classe CRUD genérica para manipular registros em tabelas de um banco de dados MySQL.
 * Opera em qualquer tabela, desde que as colunas sejam especificadas.
 */
public class CRUD {

    private String tabela;

    /**
     * Define a tabela em que o CRUD irá operar.
     *
     * @param tabela Nome da tabela
     */
    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    /**
     * Insere um novo registro na tabela.
     *
     * @param colunas Nomes das colunas
     * @param valores Valores correspondentes às colunas
     */
    public void insert(String[] colunas, String[] valores) {
        if (colunas.length != valores.length) {
            System.err.println("Número de colunas e valores não corresponde.");
            return;
        }

        String sql = "INSERT INTO " + tabela + " (" + String.join(", ", colunas) + ") VALUES (" +
                String.join(", ", Arrays.stream(valores).map(v -> "?").toArray(String[]::new)) + ")";

        try (Connection conn = new DB().conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < valores.length; i++) {
                stmt.setString(i + 1, valores[i]);
            }

            stmt.executeUpdate();
            System.out.println("Registro adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir registro: " + e.getMessage());
        }
    }

    /**
     * Atualiza um registro na tabela.
     *
     * @param atributo Coluna a ser atualizada
     * @param valorAntigo Valor antigo da coluna
     * @param valorNovo Novo valor a ser atribuído
     */
    public void update(String atributo, String valorAntigo, String valorNovo) {
        String sql = "UPDATE " + tabela + " SET " + atributo + " = ? WHERE " + atributo + " = ?";

        try (Connection conn = new DB().conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valorNovo);
            stmt.setString(2, valorAntigo);
            stmt.executeUpdate();
            System.out.println("Registro atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar registro: " + e.getMessage());
        }
    }

    /**
     * Remove um registro da tabela pelo ID.
     *
     * @param id ID do registro a ser removido
     */
    public void drop(int id) {
        String colunaId = "id";  // Ajuste este valor conforme a estrutura da tabela

        String sql = "DELETE FROM " + tabela + " WHERE " + colunaId + " = ?";

        try (Connection conn = new DB().conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Registro removido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover registro: " + e.getMessage());
        }
    }

    /**
     * @author dimitriKael
     * Exibe todos os registros da tabela.
     */
    public void select() {
        String sql = "SELECT * FROM " + tabela;

        try (Connection conn = new DB().conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Ajuste conforme a estrutura da tabela
                System.out.println("Registro: " + rs.getInt("id") + ", Nome: " + rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao selecionar registros: " + e.getMessage());
        }
    }
}
