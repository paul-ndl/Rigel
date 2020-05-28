package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * Un accélérateur de temps nommé
 * (paire [nom, accélérateur])
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public enum NamedTimeAccelerator {

    /**
     * L'accélérateur continu : x1
     */
    TIMES_1("1x", TimeAccelerator.continuous(1)),
    /**
     * L'accélérateur continu : x30
     */
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    /**
     * L'accélérateur continu : x300
     */
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    /**
     * L'accélérateur continu : x3000
     */
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    /**
     * L'accélérateur discret : un jour
     */
    DAY("jour", TimeAccelerator.discrete(60, Duration.ofHours(24))),
    /**
     * L'accélérateur discret : un jour sidéral
     */
    SIDERAL_DAY("jour sidéral", TimeAccelerator.discrete(60, Duration.parse("PT23H56M4S")));

    private final String name;
    private final TimeAccelerator accelerator;

    /**
     * Construit un accélérateur de temps nommé
     *
     * @param name        le nom
     * @param accelerator l'accélérateur
     */
    NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     * Retourne le nom de la paire
     *
     * @return le nom de la paire
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne l'accélérateur de la paire
     *
     * @return l'accélérateur de la paire
     */
    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    /**
     * Retourne une représentation textuelle de l'accélérateur nommé (le nom)
     *
     * @return une représentation textuelle de l'accélérateur nommé (le nom)
     */
    @Override
    public String toString() {
        return name;
    }
}
