package net.cjsah.bot;

public final class Main {

    public static void main(String[] args) throws Exception {
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            if (!CurrentMainThread.stop) Main.sendSignal(SignalType.STOP);
//            try {
//                CurrentMainThread.join();
//            } catch (InterruptedException e) {
//                MainApplication.log.error("Error while shutting down", e);
//            }
//        }));
        MainApplication instance = MainApplication.getInstance();

    }
}
