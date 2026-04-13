public class Barbeiro extends Thread {
    private final int id;
    private final Barbearia barbearia;

    public Barbeiro(int id, Barbearia barbearia) {
        this.id = id;
        this.barbearia = barbearia;
    }

    public int getIdBarbeiro() {
        return id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                barbearia.chamarCliente(this);
                Thread.sleep(1000); // pausa entre atendimentos
            }
        } catch (InterruptedException e) {
            System.out.println("Barbeiro " + id + " encerrando o expediente.");
        }
    }

    public void cortarCabelo(Cliente c) throws InterruptedException {
        System.out.println("Barbeiro " + id + " cortando cabelo do cliente " + c.getIdCliente());
        Thread.sleep(2000); // simula tempo de corte
        System.out.println("Barbeiro " + id + " terminou o corte do cliente " + c.getIdCliente());
    }
}
