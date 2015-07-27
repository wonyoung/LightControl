package com.wonyoung.lightcontrol.control;

/**
 * Created by wonyoung.jang on 15. 7. 27..
 */
public interface LightController {
    void color(int i);

    void black();

    void white();

    void rainbow();

    void gradient();

    void pause();

    void blink();

    void period(int p);

    void brightness(int brightness);

    void resume();

    void stop();

    void connect(String address);

    boolean isConnected();
}
