package com.github.lrhel.rabbot.utility;

import org.javacord.api.entity.message.Message;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface Utility {
    /**
     * Delete the message automatically after timeUnit
     * (Method proposed by Bastian, founder of Javacord API
     * @param delay number of timeUnit to wait before deleting the message
     * @param timeUnit time unit of the delay
     * @return some magic :eyes:
     */
    static Consumer<Message> getMessageDeleter(long delay, TimeUnit timeUnit) {
        return msg -> msg.getApi().getThreadPool().getScheduler().schedule((Runnable) msg::delete, delay, timeUnit);
    }

    static String firstUpper(String str) {
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
}
