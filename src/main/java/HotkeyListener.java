import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;

public class HotkeyListener implements NativeKeyListener {

    public static void run() {
        try {
            GlobalScreen.registerNativeHook(); //listener
            GlobalScreen.addNativeKeyListener(new HotkeyListener());
            System.out.println("Hotkey: Ctrl + Shift + Alt + R");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_R &&
                NativeKeyEvent.getModifiersText(e.getModifiers()).contains("Ctrl") &&
                NativeKeyEvent.getModifiersText(e.getModifiers()).contains("Shift") &&
                NativeKeyEvent.getModifiersText(e.getModifiers()).contains("Alt")) {
            System.out.println("Hotkey pressionada...");
            try {
                Thread.sleep(500); //sem esses delays não funciona direito
                controlC();
                Thread.sleep(500);

                String clipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                if (clipboard != null) {
                    var sons = TTS.converterTexto(clipboard.toLowerCase());
                    TTS.player(sons);
                } else {
                    System.out.println("Erro no clipboard.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {}

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}

    private static void controlC() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

}
