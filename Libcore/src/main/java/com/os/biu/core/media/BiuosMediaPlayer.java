package com.os.biu.core.media;

public final class BiuosMediaPlayer extends BiuosMediaPlayerImpl {

    private static BiuosMediaPlayer INSTANCE = null;

    public static BiuosMediaPlayer getPlayer() {
        if (null == INSTANCE) {
            synchronized (BiuosMediaPlayer.class) {
                if (null == INSTANCE) {
                    INSTANCE = new BiuosMediaPlayer();
                }
            }
        }
        return INSTANCE;
    }

    public static <T extends AbstractPlayer> BiuosMediaPlayer registerPlayer(T player) {
        synchronized (BiuosMediaPlayer.class) {
            getPlayer().registerPlayerImpl(player);
        }
        return INSTANCE;
    }

    private BiuosMediaPlayer() {
        super();
    }


}
