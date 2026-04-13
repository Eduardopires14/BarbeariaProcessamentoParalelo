// Classe principal que inicia toda a simulação da barbearia
// Responsável por criar as threads de barbeiros e clientes e coordenar o fluxo geral
public class Main {
    public static void main(String[] args) {
        // 🔹 Cria uma instância da barbearia compartilhada entre todos os barbeiros e clientes
        Barbearia barbearia = new Barbearia();

        // 🔹 Define o número total de barbeiros que trabalharão simultaneamente
        int totalBarbeiros = 3;

        // 🔹 Cria um vetor para armazenar as threads dos barbeiros
        Thread[] barbeiros = new Thread[totalBarbeiros];

        // 🔹 Inicializa e inicia cada barbeiro (cada um é uma thread independente)
        for (int i = 0; i < totalBarbeiros; i++) {
            // Cria o barbeiro com ID e referência à barbearia
            barbeiros[i] = new Barbeiro(i + 1, barbearia);
            // Inicia a execução da thread (barbeiro começa a trabalhar)
            barbeiros[i].start();
        }

        // 🔹 Define o número total de clientes que visitarão a barbearia
        int totalClientes = 30;

        // Cria um vetor para armazenar as threads dos clientes
        Thread[] clientes = new Thread[totalClientes];

        // Inicializa e inicia cada cliente com um pequeno intervalo entre chegadas
        for (int i = 0; i < totalClientes; i++) {
            // Cria o cliente com ID e referência à barbearia
            clientes[i] = new Cliente(i + 1, barbearia);
            // Inicia a execução da thread (cliente tenta entrar na barbearia)
            clientes[i].start();

            try {
                // Pausa de 300 ms entre a chegada de cada cliente
                // Isso simula o tempo real entre clientes chegando à barbearia
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // Caso a thread principal seja interrompida, imprime o erro
                e.printStackTrace();
            }
        }

        // Aguarda todas as threads de clientes terminarem (join bloqueia até o fim)
        for (Thread cliente : clientes) {
            try {
                cliente.join(); // Espera o cliente terminar seu ciclo (entrada → saída)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Aguarda todas as threads de barbeiros terminarem o expediente
        for (Thread barbeiro : barbeiros) {
            try {
                barbeiro.join(); // Espera o barbeiro concluir seu trabalho
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Exibe o relatório final (opcional, pois já é impresso dentro da barbearia)
        MonitorBarbearia.exibirResumo();

        // Mensagem final indicando o encerramento da simulação
        LogUtil.log("Simulação encerrada. Todos os 30 clientes foram atendidos.");
    }
}
