// Define a classe Cliente, que representa cada cliente como uma thread independente
public class Cliente extends Thread {
    // Identificador único do cliente
    private final int id;
    // Referência à barbearia compartilhada entre todas as threads
    private final Barbearia barbearia;
    // Armazena o momento em que o cliente entrou (em nanossegundos)
    private long tempoEntrada;
    // Armazena o momento em que o cliente começou a ser atendido (em nanossegundos)
    private long tempoAtendimento;

    // Construtor: recebe o ID e a barbearia onde o cliente será atendido
    public Cliente(int id, Barbearia barbearia) {
        this.id = id;
        this.barbearia = barbearia;
    }

    // Retorna o identificador do cliente
    public int getIdCliente() {
        return id;
    }

    // Método principal da thread — executado quando o cliente "começa a agir"
    @Override
    public void run() {
        try {
            // Registra o instante exato em que o cliente chegou (em nanossegundos)
            tempoEntrada = System.nanoTime();
            // Solicita entrada na barbearia (pode bloquear se estiver cheia)
            barbearia.entrar(this);
        } catch (InterruptedException e) {
            // Caso a thread seja interrompida, registra o evento no log
            LogUtil.log("Cliente " + id + " foi interrompido.");
        }
    }

    // Registra o momento em que o cliente começa o atendimento
    public void registrarAtendimento() {
        // Marca o instante em que o barbeiro inicia o corte
        tempoAtendimento = System.nanoTime();
        // Calcula o tempo de espera em milissegundos (diferença entre entrada e atendimento)
        long espera = (tempoAtendimento - tempoEntrada) / 1_000_000;
        // Exibe no log o tempo que o cliente esperou
        LogUtil.log("Cliente " + id + " esperou " + espera + " ms até começar o corte.");
        // Registra esse tempo no monitor para estatísticas
        MonitorBarbearia.registrarEspera(id, espera);
    }
}
