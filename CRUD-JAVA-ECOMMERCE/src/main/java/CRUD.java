import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CRUD {

    private String tabela;

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public void insert(String[] colunas, String[] valores) {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            placeholders.append("?");
            if (i < valores.length - 1) {
                placeholders.append(", ");
            }
        }

        String sql = "INSERT INTO " + tabela + " (" + String.join(", ", colunas) + ") VALUES (" + placeholders + ")";

        try (Connection conn = new DB().conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < valores.length; i++) {
                stmt.setString(i + 1, valores[i]);
            }

            stmt.executeUpdate();
            System.out.println("Registro foi adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir registro: " + e.getMessage());
        }
    }

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

    public void drop(int id) {
        String colunaId;

        switch (tabela) {
            case "clientes":
                colunaId = "Cliente_ID";
                break;
            // Adicione outros casos conforme necessário
            default:
                System.out.println("Tabela inválida!");
                return;
        }

        String sql = "DELETE FROM " + tabela + " WHERE " + colunaId + " = ?";
        try (Connection conn = new DB().conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Registro foi removido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover registro: " + e.getMessage());
        }
    }

    public void select() {
        String sql = "SELECT * FROM " + tabela;

        try (Connection conn = new DB().conectarDB()) {
            if (conn == null) {
                System.out.println("Conexão não estabelecida. Verifique a configuração do banco de dados.");
                return;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    // Ajuste as colunas de acordo com o que sua tabela realmente possui
                    switch (tabela) {
                        case "clientes":
                            System.out.println("ID: " + rs.getInt("Cliente_ID") + ", Nome: " + rs.getString("Nome"));
                            break;
                        // Adicione outros casos conforme necessário
                        default:
                            System.out.println("Tabela inválida!");
                            return;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao selecionar registros: " + e.getMessage());
        }
    }
}
