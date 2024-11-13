import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * Classe principal que executa um sistema de CRUD para uma aplicação de e-commerce.
 * O sistema permite adicionar, remover, editar e selecionar registros na tabela de clientes.
 *@author estevaolins
 */
public class Main {

    static boolean ligado = true;

    // Mapeia opções do menu para nomes de tabelas no banco de dados
    private static final Map<Integer, String> tabelas = Map.of(
        1, "clientes"
    );

    /**
     * Método principal que inicia o sistema de CRUD.
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) throws SQLException {
        Scanner ler = new Scanner(System.in);
        DB conexao = new DB();
        Connection connection = conexao.conectarDB();

        if (connection == null) {
            System.out.print("\nFalha ao conectar ao banco de dados. O programa será encerrado.");
            return;
        }

        CRUD crud = new CRUD();

        while (ligado) {
            System.out.print("""
                    
                    ------ MENU DE OPÇÕES ------
                    1 - ADICIONAR
                    2 - REMOVER
                    3 - EDITAR
                    4 - VISUALIZAR TABELA
                    5 - SAIR
                    -----------------------------
                    Digite o número da opção: """);

            int opcao;
            try {
                opcao = ler.nextInt();
                ler.nextLine(); // Consumir o '\n'

                switch (opcao) {
                    case 1 -> adicionarRegistro(crud, ler);
                    case 2 -> removerRegistro(crud, ler);
                    case 3 -> editarRegistro(crud, ler);
                    case 4 -> selecionarRegistro(crud, ler);
                    case 5 -> {
                        System.out.print("\nSaindo do sistema...");
                        ligado = false;
                    }
                    default -> System.out.println("\nOpção inválida! Tente novamente.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("######## Entrada inválida! Por favor, insira um número. ########\n");
                ler.nextLine(); // Limpar o buffer
            }
        }

        ler.close();
    }

    /**
     * Adiciona um registro à tabela de clientes no banco de dados.
     *
     * @param crud Instância da classe CRUD para realizar operações no banco de dados.
     * @param ler Scanner para leitura dos valores de entrada do usuário.
     */
    private static void adicionarRegistro(CRUD crud, Scanner ler) {
        String tabela = selecionarTabela(ler);
        if (tabela == null) return;

        crud.setTabela(tabela);
        String[] colunas = {"Nome", "Email", "Telefone", "Data_Cadastro"};
        String[] valores = new String[colunas.length];

        System.out.println("\n-------- Criando Cadastro --------");
        for (int i = 0; i < colunas.length; i++) {
            System.out.printf("Digite o valor para %s: ", colunas[i]);
            valores[i] = ler.nextLine();
        }
        System.out.println("\n");

        crud.insert(colunas, valores);
    }

    /**
     * Remove um registro da tabela de clientes no banco de dados.
     *
     * @param crud Instância da classe CRUD para realizar operações no banco de dados.
     * @param ler Scanner para leitura dos valores de entrada do usuário.
     */
    private static void removerRegistro(CRUD crud, Scanner ler) {
        String tabela = selecionarTabela(ler);
        if (tabela == null) return;

        crud.setTabela(tabela);
        System.out.println("Qual o ID do valor que você gostaria de remover?");
        int id = ler.nextInt();
        ler.nextLine();
        System.out.println("\n");
        crud.drop(id);
    }

    /**
     * Edita um atributo de um registro na tabela de clientes no banco de dados.
     *
     * @param crud Instância da classe CRUD para realizar operações no banco de dados.
     * @param ler Scanner para leitura dos valores de entrada do usuário.
     * @author dimitriKael
     */
    private static void editarRegistro(CRUD crud, Scanner ler) {
        String tabela = selecionarTabela(ler);
        if (tabela == null) return;

        crud.setTabela(tabela);
        System.out.println("Qual o atributo que você gostaria de alterar?");
        String atributo = ler.nextLine();
        System.out.println("Qual o valor antigo que você gostaria de alterar?");
        String valorAntigo = ler.nextLine();
        System.out.println("Qual o novo valor?");
        String valorNovo = ler.nextLine();

        crud.update(atributo, valorAntigo, valorNovo);
    }

    /**
     * Exibe os registros de uma tabela específica do banco de dados.
     *
     * @param crud Instância da classe CRUD para realizar operações no banco de dados.
     * @param ler Scanner para leitura dos valores de entrada do usuário.
     * @author estevaolins
     */
    private static void selecionarRegistro(CRUD crud, Scanner ler) {
        String tabela = selecionarTabela(ler);
        if (tabela == null) return;

        crud.setTabela(tabela);
        crud.select();
    }

    /**
     * Permite ao usuário selecionar uma tabela específica para operações CRUD.
     * Atualmente, apenas a tabela "cliente" está disponível.
     *
     * @param ler Scanner para leitura dos valores de entrada do usuário.
     * @return O nome da tabela selecionada pelo usuário, ou null se a opção for inválida.
     * @author dimitriKael
     */
    private static String selecionarTabela(Scanner ler) {
        System.out.print("""
                
                
                ------ SELECIONAR TABELA ------
                1 - CLIENTE
                -------------------------------
                Digite o número da tabela: """);
        
        int opcao = ler.nextInt();
        ler.nextLine(); // Consumir o '\n'
        System.out.println("\n");
        return tabelas.getOrDefault(opcao, null);
    }
}
