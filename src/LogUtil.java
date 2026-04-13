// Classe utilitária responsável por padronizar a saída de logs no console
// Ela adiciona o horário atual em cada mensagem para facilitar o acompanhamento da simulação
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {

    // 🔹 Define o formato de hora que será exibido nos logs (HH:mm:ss)
    // Exemplo: 22:31:13
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    // 🔹 Método estático que imprime mensagens no console com o horário atual
    public static void log(String mensagem) {
        // Obtém o horário atual do sistema
        // Formata conforme o padrão definido acima
        // Concatena o horário com a mensagem recebida e imprime no console
        System.out.println("[" + LocalTime.now().format(formatter) + "] " + mensagem);
    }
}
