import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Arquivos de áudio carregados: " + Audio.listaDeSons.size());
        HotkeyListener.run();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escreva algo ou selecione alguma frase e use a hotkey.");

        while (true) {
            try {
                String texto = scanner.nextLine().toLowerCase();
                var sons = TTS.converterTexto(texto);
                System.out.println("falando...");
                TTS.player(sons);
                System.out.println("--fim--");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}