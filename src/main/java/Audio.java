import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Audio {

    public static final Map<String, String> listaDeSons = new HashMap<>();

    private static void carregarArquivosDeAudio() throws URISyntaxException {
        URL pastaWav = Audio.class.getClassLoader().getResource("wav");
        File folder = new File(pastaWav.toURI());
        File[] arquivos = folder.listFiles((dir, file) -> file.toLowerCase().endsWith(".wav"));

        if (arquivos != null) {
            for (File arquivo : arquivos) {
                String nomeArquivo = arquivo.getName();
                String som = nomeArquivo.substring(0, nomeArquivo.lastIndexOf('.'));
                listaDeSons.put(som, arquivo.getAbsolutePath());
            }
        } else {
            System.out.println("Nenhum arquivo .wav encontrado na pasta: " + pastaWav);
        }
    }

    //carregar sons sem precisar criar objeto
    static {
        try {
            carregarArquivosDeAudio();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}