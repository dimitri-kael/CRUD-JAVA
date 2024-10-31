import java.util.Scanner;
import java.sql.Connection;
import java.util.InputMismatchException;

public class Main {

    static boolean ligado = true;

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        DB conection = new DB();
        Connection connection = conection.conectarDB();

        if (connection == null) {
            System.out.println("Falha ao conectar ao banco de dados. O programa será encerrado.");
            return;
        }

        CRUD crud = new CRUD();

        while (ligado) {
            System.out.println("""
                    ------ MENU DE OPÇÕES ------
                    1 - ADICIONAR
                    2 - REMOVER
                    3 - EDITAR
                    4 - SELECIONAR
                    5 - SAIR
                    """);

            int opcao;
            try {
                opcao = ler.nextInt();
                ler.nextLine(); // Consumir o '\n'

                switch (opcao) {
                    case 1: // Adicionar
                        adicionarRegistro(crud, ler);
                        break;
                    case 2: // Remover
                        removerRegistro(crud, ler);
                        break;
                    case 3: // Editar
                        editarRegistro(crud, ler);
                        break;
                    case 4: // Selecionar
                        selecionarRegistro(crud, ler);
                        break;
                    case 5: // Sair
                        System.out.println("Saindo do sistema...");
                        ligado = false;
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                ler.nextLine(); // Limpar o buffer
            }
        }

        ler.close();
    }

    private static void adicionarRegistro(CRUD crud, Scanner ler) {
        System.out.println("""
                ------ ADICIONAR ------
                QUAL TABELA VOCÊ GOSTARIA DE ADICIONAR INFORMAÇÕES?
                1 - CLIENTE
                """);

        int opcao = ler.nextInt();
        ler.nextLine();

        String[] colunas = null;
        switch (opcao) {
            case 1:
                crud.setTabela("cliente");
                colunas = new String[]{"Nome", "Email", "Telefone", "Data_Cadastro"};
                break;
            default:
                System.out.println("Opção inválida!");
                return;
        }

        String[] valores = new String[colunas.length];
        for (String coluna : colunas) {
            System.out.printf("Digite o valor para %s: ", coluna);
            valores[colunas.length] = ler.nextLine();
        }
        crud.insert(colunas, valores);
    }

    private static void removerRegistro(CRUD crud, Scanner ler) {
        System.out.println("Qual tabela você gostaria de remover informações?");
        System.out.println("""
                ------ REMOVER ------
                1 - CLIENTE
                """);

        int idOpcao = ler.nextInt();
        System.out.println("Qual o ID do valor que você gostaria de remover?");
        int id = ler.nextInt();
        ler.nextLine();

        switch (idOpcao) {
            case 1:
                crud.setTabela("cliente");
                break;
            default:
                System.out.println("Opção inválida!");
                return;
        }

        crud.drop(id);
    }

    private static void editarRegistro(CRUD crud, Scanner ler) {
        System.out.println("Qual tabela você gostaria de editar informações?");
        System.out.println("""
                ------ EDITAR ------
                1 - CLIENTE
                """);

        int editOpcao = ler.nextInt();
        ler.nextLine();
        System.out.println("Qual o atributo que você gostaria de alterar?");
        String atributo = ler.nextLine();
        System.out.println("Qual o valor antigo que você gostaria de alterar?");
        String valorAntigo = ler.nextLine();
        System.out.println("Qual o novo valor?");
        String valorNovo = ler.nextLine();

        switch (editOpcao) {
            case 1:
                crud.setTabela("cliente");
                break;
            default:
                System.out.println("Opção inválida!");
                return;
        }

        crud.update(atributo, valorAntigo, valorNovo);
    }

    private static void selecionarRegistro(CRUD crud, Scanner ler) {
        System.out.println("Qual tabela você gostaria de selecionar as informações?");
        System.out.println("""
                ------ SELECIONAR ------
                1 - CLIENTE
                """);

        int selectOpcao = ler.nextInt();
        ler.nextLine();

        switch (selectOpcao) {
            case 1:
                crud.setTabela("cliente");
                break;
            default:
                System.out.println("Opção inválida!");
                return;
        }

        crud.select();
    }
}
