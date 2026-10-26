package Aplicacao;



// --- REQUISITO: ENTRADA DE DADOS com java.util.Scanner ---
import Aplicacao.GerenciadorAgencia;
import java.util.Scanner;

/**
 * Classe principal do sistema.
 * 
 * Responsável APENAS por:
 *   - Exibir o menu no terminal.
 *   - Capturar a entrada do usuário via Scanner.
 *   - Delegar as operações ao GerenciadorAgencia.
 * 
 * --- REQUISITO: A lógica de negócio NÃO está aqui ---
 * Esta classe não contém regras de validação nem manipula listas diretamente.
 */
public class Main {

    public static void main(String[] args) {

        // --- REQUISITO: Scanner instanciado na Main ---
        Scanner scanner = new Scanner(System.in);

        // --- Instancia o gerenciador que contém toda a lógica de negócios ---
        GerenciadorAgencia gerenciador = new GerenciadorAgencia();

        int opcao;

        System.out.println("========================================================");
        System.out.println("   SISTEMA DE GESTAO DE CAMPANHAS - AGENCIA MARKETING");
        System.out.println("========================================================");

        // --- REQUISITO: ESTRUTURA DE REPETIÇÃO (do-while no menu principal) ---
        do {
            System.out.println("\n------------------ MENU PRINCIPAL ------------------");
            System.out.println("  1 - Cadastrar Cliente");
            System.out.println("  2 - Cadastrar Campanha para um Cliente");
            System.out.println("  3 - Listar todos os Clientes e suas Campanhas");
            System.out.println("  4 - Ativar/Pausar Campanha");
            System.out.println("  5 - Adicionar Saldo ao Orcamento de Campanha");
            System.out.println("  0 - Sair");
            System.out.println("----------------------------------------------------");
            System.out.print("  Escolha uma opcao: ");

            // Tratamento para entrada não numérica
            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consome o '\n' pendente após nextInt()
            } else {
                scanner.nextLine(); // Descarta entrada inválida
                opcao = -1;         // Força cair no default do switch
            }

            // --- REQUISITO: ESTRUTURA DE CONTROLE (switch) para navegação do menu ---
            switch (opcao) {

                case 1:
                    // =================== CADASTRAR CLIENTE ===================
                    System.out.println("\n--- CADASTRO DE CLIENTE ---");
                    System.out.print("  Nome completo: ");
                    String nome = scanner.nextLine();

                    System.out.print("  Documento (CPF/CNPJ): ");
                    String documento = scanner.nextLine();

                    System.out.print("  Email: ");
                    String email = scanner.nextLine();

                    // Validações simples de campos vazios
                    if (nome.trim().isEmpty() || documento.trim().isEmpty() || email.trim().isEmpty()) {
                        System.out.println("\n[!] Todos os campos sao obrigatorios. Cadastro cancelado.");
                    } else {
                        // Delega ao gerenciador (a lógica de duplicata está lá)
                        boolean sucesso = gerenciador.cadastrarCliente(nome.trim(), documento.trim(), email.trim());
                        if (sucesso) {
                            System.out.println("\n[OK] Cliente \"" + nome.trim() + "\" cadastrado com sucesso!");
                        } else {
                            System.out.println("\n[!] Ja existe um cliente com o documento " + documento.trim() + ".");
                        }
                    }
                    break;

                case 2:
                    // =================== CADASTRAR CAMPANHA ===================
                    // --- REGRA DE NEGÓCIO: Só permite cadastrar se houver clientes ---
                    if (!gerenciador.existemClientes()) {
                        System.out.println("\n[!] Nenhum cliente cadastrado. Cadastre um cliente antes de criar campanhas.");
                        break;
                    }

                    System.out.println("\n--- CADASTRO DE CAMPANHA ---");
                    System.out.print("  Titulo da campanha: ");
                    String titulo = scanner.nextLine();

                    System.out.print("  Orcamento inicial (R$): ");
                    double orcamento;
                    if (scanner.hasNextDouble()) {
                        orcamento = scanner.nextDouble();
                        scanner.nextLine(); // Consome '\n'
                    } else {
                        scanner.nextLine();
                        System.out.println("\n[!] Valor de orcamento invalido. Cadastro cancelado.");
                        break;
                    }

                    System.out.print("  Plataforma (Instagram/Google Ads/Facebook/TikTok): ");
                    String plataforma = scanner.nextLine();

                    System.out.print("  Documento do cliente vinculado: ");
                    String docCliente = scanner.nextLine();

                    // Delega ao gerenciador (validações de orçamento e cliente estão lá)
                    int resultado = gerenciador.cadastrarCampanha(titulo.trim(), orcamento, plataforma.trim(), docCliente.trim());

                    // --- REQUISITO: CONDICIONAL (if/else if/else) ---
                    if (resultado == 0) {
                        System.out.println("\n[OK] Campanha \"" + titulo.trim() + "\" cadastrada com sucesso!");
                    } else if (resultado == 1) {
                        System.out.println("\n[!] Orcamento deve ser maior que zero. Cadastro cancelado.");
                    } else if (resultado == 2) {
                        System.out.println("\n[!] Cliente com documento \"" + docCliente.trim() + "\" nao encontrado.");
                    }
                    break;

                case 3:
                    // =================== LISTAR CLIENTES E CAMPANHAS ===================
                    // Delega completamente ao gerenciador, que já trata lista vazia
                    System.out.println(gerenciador.listarClientesECampanhas());
                    break;

                case 4:
                    // =================== ATIVAR / PAUSAR CAMPANHA ===================
                    if (!gerenciador.existemCampanhas()) {
                        System.out.println("\n[!] Nenhuma campanha cadastrada no sistema.");
                        break;
                    }

                    System.out.println("\n--- ATIVAR / PAUSAR CAMPANHA ---");
                    System.out.print("  Titulo da campanha: ");
                    String tituloBusca = scanner.nextLine();

                    // Delega ao gerenciador
                    String msgStatus = gerenciador.alternarStatusCampanha(tituloBusca.trim());
                    System.out.println("\n" + msgStatus);
                    break;

                case 5:
                    // =================== ADICIONAR SALDO AO ORÇAMENTO ===================
                    if (!gerenciador.existemCampanhas()) {
                        System.out.println("\n[!] Nenhuma campanha cadastrada no sistema.");
                        break;
                    }

                    System.out.println("\n--- ADICIONAR SALDO AO ORCAMENTO ---");
                    System.out.print("  Titulo da campanha: ");
                    String tituloSaldo = scanner.nextLine();

                    System.out.print("  Valor a adicionar (R$): ");
                    double valorSaldo;
                    if (scanner.hasNextDouble()) {
                        valorSaldo = scanner.nextDouble();
                        scanner.nextLine(); // Consome '\n'
                    } else {
                        scanner.nextLine();
                        System.out.println("\n[!] Valor invalido. Operacao cancelada.");
                        break;
                    }

                    // Delega ao gerenciador
                    String msgSaldo = gerenciador.adicionarSaldoCampanha(tituloSaldo.trim(), valorSaldo);
                    System.out.println("\n" + msgSaldo);
                    break;

                case 0:
                    // =================== SAIR ===================
                    System.out.println("\n========================================================");
                    System.out.println("  Obrigado por utilizar o sistema! Ate logo.");
                    System.out.println("========================================================");
                    break;

                default:
                    System.out.println("\n[!] Opcao invalida. Tente novamente.");
                    break;
            }

        } while (opcao != 0);

        // Fecha o Scanner ao encerrar
        scanner.close();
    }
}
