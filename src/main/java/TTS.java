import javax.sound.sampled.*;
import java.io.File;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TTS {

    //entender mais a fundo isso aqui e fazer esquema de áudio 2x
    public static void player(List<String> sons) {
        List<AudioInputStream> audioInputStreamList = new ArrayList<>();
        try {
            // Carregar todos os arquivos de áudio em AudioInputStreams
            for (String som : sons) {
                File audioFile = new File(som);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                audioInputStreamList.add(audioStream);
            }
            // Combinar todos os AudioInputStreams em um único SequenceInputStream
            AudioInputStream combinedStream = new AudioInputStream(
                    new SequenceInputStream(Collections.enumeration(audioInputStreamList)),
                    audioInputStreamList.get(0).getFormat(),
                    audioInputStreamList.stream().mapToLong(AudioInputStream::getFrameLength).sum()
            );
            // Configurar o SourceDataLine para tocar o áudio
            AudioFormat format = combinedStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(format);
            sourceLine.start();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = combinedStream.read(buffer, 0, buffer.length)) != -1) {
                sourceLine.write(buffer, 0, bytesRead);
            }
            sourceLine.drain();
            sourceLine.close();
            combinedStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<String> converterTexto(String texto) {
        String[] palavras = texto.trim().split("\\s+"); //regex para remover vários espaços entre palavras
        List<String> sons = new ArrayList<>();

        for (String palavra : palavras) {
            int i = 0;
            while (i < palavra.length()) {
                boolean somExiste = false;

                //aparentemente 5 letras é o máximo possível por sílaba, pelo menos em portugues
                for (int silaba = 5; silaba > 0; silaba--) {
                    if (i + silaba <= palavra.length()) {
                        String som = palavra.substring(i, i + silaba); //dívide a palavra de 5~1 e vai checando pra ver se o som existe nos arquivos wav
                        if (Audio.listaDeSons.containsKey(som)) {
                            sons.add(Audio.listaDeSons.get(som));
                            i += silaba; //se o som existir, i vira index da proxima letra após último som adicionado
                            somExiste = true;
                            break;
                        }
                    }
                }

                //se mesmo com 1 caracter não encontrar nenhum arquivo com o mesmo nome, ignora o som e avança uma letra(index)
                if (!somExiste) {
                    i++;
                }

            }
            sons.add(Audio.listaDeSons.get("pausa"));
        }
        return sons;
    }

}
