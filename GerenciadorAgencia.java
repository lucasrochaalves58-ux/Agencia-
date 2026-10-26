package Aplicacao;



// --- REQUISITO: COLEÇÕES (ArrayList) ---
import java.util.ArrayList;

/**
 * Classe responsável por toda a LÓGICA DE NEGÓCIOS da agência.
 * Gerencia as listas de Clientes e Campanhas e realiza as operações/validações.
 * 
 * --- REQUISITO: A lógica de negócio NÃO fica na Main ---
 * Todos os métodos de cadastro, listagem, validação e operações de domínio
 * estão centralizados aqui, seguindo o princípio de responsabilidade única.
 */
public class GerenciadorAgencia {

    // --- REQUISITO: COLEÇÕES (ArrayList para armazenar múltiplos objetos) ---
    private ArrayList<Cliente> clientes;
    private ArrayList<Campanha> campanhas;

    // --- Construtor: inicializa as listas vazias ---
    public GerenciadorAgencia() {
        this.clientes = new ArrayList<>();
        this.campanhas = new ArrayList<>();
    }

    // =========================================================================
    // OPERAÇÕES COM CLIENTES
    // =========================================================================

    /**
     * Cadastra um novo cliente no sistema.
     * 
     * --- REGRA DE NEGÓCIO: Impede cadastro de cliente com documento duplicado ---
     * 
     * @param nome      Nome completo do cliente.
     * @param documento CPF ou CNPJ do cliente.
     * @param email     E-mail de contato do cliente.
     * @return true se cadastrado com sucesso, false se documento já existir.
     */
    public boolean cadastrarCliente(String nome, String documento, String email) {
        // --- REQUISITO: ESTRUTURA DE REPETIÇÃO (for-each) + CONDICIONAL (if) ---
        // Verifica se já existe cliente com o mesmo documento
        for (Cliente c : clientes) {
            if (c.getDocumento().equals(documento)) {
                return false; // Documento duplicado
            }
        }
        // Cria o objeto usando o construtor completo e adiciona à coleção
        Cliente novoCliente = new Cliente(nome, documento, email);
        clientes.add(novoCliente);
        return true;
    }

    /**
     * Lista todos os clientes cadastrados e suas respectivas campanhas.
     * 
     * --- REGRA DE NEGÓCIO: Se a lista estiver vazia, exibe mensagem amigável ---
     * --- REQUISITO: ESTRUTURA DE REPETIÇÃO (for-each aninhado) ---
     * 
     * @return String formatada com todos os clientes e campanhas, ou mensagem de aviso.
     */
    public String listarClientesECampanhas() {
        // --- REGRA DE NEGÓCIO: lista vazia ---
        if (clientes.isEmpty()) {
            return "\n[!] Nenhum cliente cadastrado no sistema. Cadastre um cliente primeiro.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n============================================\n");
        sb.append("       CLIENTES E SUAS CAMPANHAS\n");
        sb.append("============================================\n");

        int contadorCliente = 1;

        // --- REQUISITO: ESTRUTURA DE REPETIÇÃO (for-each) ---
        for (Cliente cliente : clientes) {
            sb.append("\n--- Cliente #").append(contadorCliente).append(" ---\n");
            sb.append("  Nome:      ").append(cliente.getNome()).append("\n");
            sb.append("  Documento: ").append(cliente.getDocumento()).append("\n");
            sb.append("  Email:     ").append(cliente.getEmail()).append("\n");

            // Busca campanhas vinculadas a este cliente
            ArrayList<Campanha> campanhasDoCliente = buscarCampanhasPorCliente(cliente.getDocumento());

            if (campanhasDoCliente.isEmpty()) {
                sb.append("  Campanhas: Nenhuma campanha cadastrada.\n");
            } else {
                sb.append("  Campanhas:\n");
                int contadorCampanha = 1;
                for (Campanha campanha : campanhasDoCliente) {
                    sb.append("    ").append(contadorCampanha).append(". ")
                      .append(campanha.getTitulo())
                      .append(" | R$ ").append(String.format("%.2f", campanha.getOrcamento()))
                      .append(" | ").append(campanha.getStatusTexto())
                      .append(" | ").append(campanha.getPlataforma())
                      .append("\n");
                    contadorCampanha++;
                }
            }
            contadorCliente++;
        }

        sb.append("\n============================================\n");
        return sb.toString();
    }

    /**
     * Verifica se existem clientes cadastrados no sistema.
     * Usado como pré-condição para cadastro de campanhas.
     * 
     * @return true se houver ao menos um cliente cadastrado.
     */
    public boolean existemClientes() {
        return !clientes.isEmpty();
    }

    /**
     * Busca um cliente pelo número do documento.
     * 
     * @param documento Documento do cliente a ser buscado.
     * @return O objeto Cliente encontrado, ou null se não existir.
     */
    public Cliente buscarClientePorDocumento(String documento) {
        // --- REQUISITO: ESTRUTURA DE REPETIÇÃO (for-each) + CONDICIONAL (if) ---
        for (Cliente c : clientes) {
            if (c.getDocumento().equals(documento)) {
                return c;
            }
        }
        return null;
    }

    // =========================================================================
    // OPERAÇÕES COM CAMPANHAS
    // =========================================================================

    /**
     * Cadastra uma nova campanha vinculada a um cliente existente.
     * 
     * --- REGRA DE NEGÓCIO: Orçamento deve ser maior que zero ---
     * --- REGRA DE NEGÓCIO: O cliente vinculado deve existir no sistema ---
     * 
     * @param titulo           Título da campanha.
     * @param orcamento        Orçamento inicial (deve ser > 0).
     * @param plataforma       Plataforma digital da campanha.
     * @param documentoCliente Documento do cliente proprietário.
     * @return 0 = sucesso, 1 = orçamento inválido, 2 = cliente não encontrado.
     */
    public int cadastrarCampanha(String titulo, double orcamento, String plataforma, String documentoCliente) {

        // --- REGRA DE NEGÓCIO: Impede orçamento negativo ou zero ---
        if (orcamento <= 0) {
            return 1; // Código de erro: orçamento inválido
        }

        // --- REGRA DE NEGÓCIO: Verifica se o cliente existe ---
        Cliente cliente = buscarClientePorDocumento(documentoCliente);
        if (cliente == null) {
            return 2; // Código de erro: cliente não encontrado
        }

        // Cria a campanha com o construtor completo e adiciona à coleção
        Campanha novaCampanha = new Campanha(titulo, orcamento, plataforma, documentoCliente);
        campanhas.add(novaCampanha);
        return 0; // Sucesso
    }

    /**
     * Busca todas as campanhas de um determinado cliente.
     * 
     * @param documentoCliente Documento do cliente.
     * @return Lista de campanhas do cliente.
     */
    private ArrayList<Campanha> buscarCampanhasPorCliente(String documentoCliente) {
        ArrayList<Campanha> resultado = new ArrayList<>();
        for (Campanha c : campanhas) {
            if (c.getDocumentoCliente().equals(documentoCliente)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    /**
     * Busca uma campanha pelo título exato.
     * 
     * @param titulo Título da campanha.
     * @return O objeto Campanha, ou null se não encontrado.
     */
    public Campanha buscarCampanhaPorTitulo(String titulo) {
        for (Campanha c : campanhas) {
            if (c.getTitulo().equalsIgnoreCase(titulo)) {
                return c;
            }
        }
        return null;
    }

    // =========================================================================
    // OPERAÇÕES DE DOMÍNIO (LÓGICA DE NEGÓCIO)
    // =========================================================================

    /**
     * Ativa ou pausa uma campanha existente (toggle).
     * 
     * --- OPERAÇÃO DE DOMÍNIO: Ativar/Pausar Campanha ---
     * Demonstra lógica de negócio com condicional (if/else).
     * 
     * @param titulo Título da campanha a ser alterada.
     * @return Mensagem descrevendo o resultado da operação.
     */
    public String alternarStatusCampanha(String titulo) {
        Campanha campanha = buscarCampanhaPorTitulo(titulo);

        // --- REQUISITO: CONDICIONAL (if/else) ---
        if (campanha == null) {
            return "[!] Campanha \"" + titulo + "\" nao encontrada no sistema.";
        }

        // Inverte o status atual
        if (campanha.isStatusAtiva()) {
            campanha.setStatusAtiva(false);
            return "[OK] Campanha \"" + titulo + "\" foi PAUSADA com sucesso.";
        } else {
            campanha.setStatusAtiva(true);
            return "[OK] Campanha \"" + titulo + "\" foi ATIVADA com sucesso.";
        }
    }

    /**
     * Adiciona saldo ao orçamento de uma campanha existente.
     * 
     * --- OPERAÇÃO DE DOMÍNIO: Adicionar Saldo ao Orçamento ---
     * --- REGRA DE NEGÓCIO: Valor do saldo deve ser positivo ---
     * 
     * @param titulo Título da campanha.
     * @param valor  Valor a ser adicionado (deve ser > 0).
     * @return Mensagem descrevendo o resultado da operação.
     */
    public String adicionarSaldoCampanha(String titulo, double valor) {
        Campanha campanha = buscarCampanhaPorTitulo(titulo);

        if (campanha == null) {
            return "[!] Campanha \"" + titulo + "\" nao encontrada no sistema.";
        }

        // --- REGRA DE NEGÓCIO: Valor de saldo deve ser positivo ---
        if (valor <= 0) {
            return "[!] O valor do saldo deve ser maior que zero.";
        }

        campanha.adicionarOrcamento(valor);
        return "[OK] R$ " + String.format("%.2f", valor)
                + " adicionados a campanha \"" + titulo
                + "\". Novo orcamento: R$ " + String.format("%.2f", campanha.getOrcamento()) + ".";
    }

    /**
     * Verifica se existem campanhas cadastradas no sistema.
     * 
     * @return true se houver ao menos uma campanha.
     */
    public boolean existemCampanhas() {
        return !campanhas.isEmpty();
    }
}
