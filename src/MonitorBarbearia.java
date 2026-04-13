import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

// Classe responsável por monitorar o desempenho da barbearia durante a simulação
// Ela registra tempos de espera, número de clientes atendidos e controla o relatório final
public class MonitorBarbearia {

    // Contador atômico que registra quantos clientes já foram atendidos (thread-safe)
    private static final AtomicInteger clientesAtendidos = new AtomicInteger(0);

    // Soma total dos tempos de espera de todos os clientes (em milissegundos)
    private static final AtomicLong tempoTotalEspera = new AtomicLong(0);

    // Mapa que associa o ID de cada cliente ao seu tempo de espera individual
    // ConcurrentHashMap garante segurança de acesso simultâneo entre threads
    private static final Map<Integer, Long> temposEspera = new ConcurrentHashMap<>();

    // Número total de clientes esperados no dia (não usado diretamente, mas pode servir para validações)
    private static final int TOTAL_CLIENTES_DIA = 30;

    // Flag booleana que indica se o relatório final já foi impresso (evita duplicação)
    private static boolean relatorioImpresso = false;

    // Contador atômico que registra quantos clientes já saíram da barbearia
    // Serve para determinar o momento exato de encerrar a simulação
    private static final AtomicInteger clientesSaindo = new AtomicInteger(0);

    // Método chamado sempre que um cliente sai da barbearia
    // Incrementa o contador de saídas de forma segura entre threads
    public static void registrarSaida() {
        clientesSaindo.incrementAndGet();
    }

    // Retorna o número atual de clientes que já saíram
    public static int getClientesSaindo() {
        return clientesSaindo.get();
    }

    // Registra o tempo de espera de um cliente específico
    // Também atualiza o total e o contador de atendimentos
    public static void registrarEspera(int idCliente, long tempoMs) {
        clientesAtendidos.incrementAndGet(); // incrementa número de clientes atendidos
        tempoTotalEspera.addAndGet(tempoMs); // soma o tempo de espera ao total
        temposEspera.put(idCliente, tempoMs); // associa o tempo ao ID do cliente
    }

    // Exibe o relatório final com estatísticas da simulação
    public static void exibirResumo() {
        int total = clientesAtendidos.get(); // obtém total de clientes atendidos
        if (total > 0) {
            // Calcula a média de espera (em milissegundos)
            long media = tempoTotalEspera.get() / total;

            // Exibe cabeçalho do relatório
            LogUtil.log("===== RELATÓRIO FINAL =====");
            LogUtil.log("Clientes atendidos: " + total);
            LogUtil.log("Tempo médio de espera até o início do corte: " + media + " ms");
            LogUtil.log("Tempo de espera por cliente:");

            // Percorre o mapa e imprime o tempo de espera de cada cliente
            for (Map.Entry<Integer, Long> entry : temposEspera.entrySet()) {
                LogUtil.log("Cliente " + entry.getKey() + " esperou " + entry.getValue() + " ms.");
            }

            // Finaliza o relatório
            LogUtil.log("============================");
        } else {
            // Caso nenhum cliente tenha sido atendido (situação rara)
            LogUtil.log("Nenhum cliente foi atendido.");
        }
    }

    // Retorna o número total de clientes atendidos
    public static int getClientesAtendidos() {
        return clientesAtendidos.get();
    }

    // Verifica se o relatório já foi impresso
    public static boolean isRelatorioImpresso() {
        return relatorioImpresso;
    }

    // Define se o relatório foi impresso (usado para evitar duplicação)
    public static void setRelatorioImpresso(boolean valor) {
        relatorioImpresso = valor;
    }
}
