// Classe principal que representa o ambiente da barbearia
// Controla entrada, atendimento, pagamento e saída dos clientes
import java.util.concurrent.*;

public class Barbearia {

    // 🔹 Semáforo que limita a capacidade total da barbearia (máx. 20 clientes simultâneos)
    private final Semaphore capacidade = new Semaphore(20);

    // 🔹 Semáforo que controla os 4 lugares disponíveis no sofá de espera
    private final Semaphore sofa = new Semaphore(4);

    // 🔹 Semáforo que controla as 3 cadeiras de atendimento dos barbeiros
    private final Semaphore cadeiras = new Semaphore(3);

    // 🔹 Semáforo que garante que apenas um cliente pague por vez no caixa
    private final Semaphore caixa = new Semaphore(1);

    // 🔹 Fila de clientes sentados no sofá (esperando atendimento)
    private final BlockingQueue<Cliente> filaSofa = new LinkedBlockingQueue<>();

    // 🔹 Fila de clientes em pé (aguardando vaga no sofá)
    private final BlockingQueue<Cliente> filaEmPe = new LinkedBlockingQueue<>();

    // 🔹 Fila de clientes prontos para serem atendidos pelos barbeiros
    private final BlockingQueue<Cliente> filaAtendimento = new LinkedBlockingQueue<>();

    // Método chamado quando um cliente tenta entrar na barbearia
    public void entrar(Cliente c) throws InterruptedException {
        // 🔹 Bloqueia se a barbearia estiver cheia (máx. 20 clientes)
        capacidade.acquire();
        LogUtil.log("Cliente " + c.getIdCliente() + " entrou na barbearia.");

        // 🔹 Se houver lugar no sofá, o cliente senta
        if (sofa.tryAcquire()) {
            filaSofa.put(c);
            LogUtil.log("Cliente " + c.getIdCliente() + " sentou no sofá.");
        } else {
            // 🔹 Caso contrário, fica em pé aguardando vaga
            filaEmPe.put(c);
            LogUtil.log("Cliente " + c.getIdCliente() + " está em pé.");
        }

        // 🔹 Adiciona o cliente à fila de atendimento (barbeiros vão buscar daqui)
        filaAtendimento.put(c);
    }

    // Método executado pelos barbeiros para chamar o próximo cliente
    public void chamarCliente(Barbeiro b) throws InterruptedException {
        // 🔹 Retira o próximo cliente da fila de atendimento (bloqueia se estiver vazia)
        Cliente c = filaAtendimento.take();

        // 🔹 Remove o cliente da fila correspondente (sofá ou em pé)
        if (filaSofa.contains(c)) {
            filaSofa.remove(c);
            sofa.release(); // libera o lugar no sofá
        } else if (filaEmPe.contains(c)) {
            filaEmPe.remove(c);
        }

        // 🔹 Se houver vaga no sofá e clientes em pé, promove um deles para o sofá
        if (sofa.availablePermits() > 0 && !filaEmPe.isEmpty()) {
            Cliente promovido = filaEmPe.take();
            sofa.acquire(); // ocupa o lugar no sofá
            filaSofa.put(promovido);
            LogUtil.log("Cliente " + promovido.getIdCliente() + " foi promovido para o sofá.");
        }

        // 🔹 Ocupa uma cadeira de atendimento
        cadeiras.acquire();
        LogUtil.log("Barbeiro " + b.getIdBarbeiro() + " chamou cliente " + c.getIdCliente());

        // 🔹 Registra o tempo de espera até o início do corte
        c.registrarAtendimento();

        // 🔹 Barbeiro realiza o corte (método da classe Barbeiro)
        b.cortarCabelo(c);

        // 🔹 Cliente realiza o pagamento
        pagar(c);

        // 🔹 Cliente sai da barbearia
        sair(c);
    }

    // Método que simula o pagamento do cliente
    private void pagar(Cliente c) throws InterruptedException {
        // 🔹 Garante exclusividade no caixa (apenas um cliente por vez)
        caixa.acquire();
        LogUtil.log("Cliente " + c.getIdCliente() + " está pagando...");
        Thread.sleep(500); // simula tempo de pagamento
        LogUtil.log("Cliente " + c.getIdCliente() + " terminou o pagamento.");
        caixa.release(); // libera o caixa para o próximo cliente
    }

    // Método que trata a saída do cliente após o pagamento
    private void sair(Cliente c) {
        // 🔹 Libera a cadeira e a capacidade total da barbearia
        cadeiras.release();
        capacidade.release();
        LogUtil.log("Cliente " + c.getIdCliente() + " saiu da barbearia.");

        // 🔹 Registra a saída no monitor
        MonitorBarbearia.registrarSaida();

        // 🔹 Se o último cliente saiu, imprime o relatório final e encerra a simulação
        if (MonitorBarbearia.getClientesSaindo() == 30 && !MonitorBarbearia.isRelatorioImpresso()) {
            MonitorBarbearia.setRelatorioImpresso(true);
            MonitorBarbearia.exibirResumo();
            LogUtil.log("Simulação encerrada. Todos os 30 clientes foram atendidos.");
            LogUtil.log("Barbearia fechada — fim do expediente.");
        }
    }
}
