package ru.sbt.drtmn.lab.bus;

/**
 * Интрефейс для отправки сообщения
 *
 * @author SBT-Kranchev-DF
 * @since 18.09.2013
 */
public interface ExchangeCallback {
    void send(Object message);
}
