package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
import static ch.epfl.rigel.astronomy.MoonModel.MOON;
import static ch.epfl.rigel.astronomy.SunModel.SUN;

/**
 * Un ciel observé
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class ObservedSky {

    private final EquatorialToHorizontalConversion equatorialToHorizontalConversion;
    private final StereographicProjection stereographicProjection;

    private final Sun sun;
    private final CartesianCoordinates sunPosition;

    private final Moon moon;
    private final CartesianCoordinates moonPosition;

    private final List<Planet> planets = new ArrayList<>();
    private final double[] planetPositions = new double[14];

    private final List<Star> stars;
    private final double[] starPositions;

    private final StarCatalogue catalogue;
    private final Map<CelestialObject, CartesianCoordinates> coordMap = new HashMap<>();

    /**
     * Construit un ciel observé
     *
     * @param when                    l'instant d'observation
     * @param where                   la position d'observation
     * @param stereographicProjection la projection stéréographique
     * @param catalogue               le catalogue contenant les étoiles et les astérismes
     */
    public ObservedSky(ZonedDateTime when, GeographicCoordinates where, StereographicProjection stereographicProjection, StarCatalogue catalogue) {
        double daysSinceJ2010 = J2010.daysUntil(when);
        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(when);
        equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(when, where);
        this.stereographicProjection = stereographicProjection;
        this.catalogue = catalogue;

        //Calcul des coordonnées du Soleil
        sun = SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        sunPosition = applyProjection(sun.equatorialPos());
        coordMap.put(sun, sunPosition);

        //Calcul des coordonnées de la Lune
        moon = MOON.at(daysSinceJ2010, eclipticToEquatorialConversion);
        moonPosition = applyProjection(moon.equatorialPos());
        coordMap.put(moon, moonPosition);

        //Calcul des coordonnées des planètes
        PlanetModel.ALL.stream().filter(p -> p != PlanetModel.EARTH).forEach(p -> planets.add(p.at(daysSinceJ2010, eclipticToEquatorialConversion)));
        for (int i = 0; i < 7; i++) {
            CartesianCoordinates position = applyProjection(planets.get(i).equatorialPos());
            planetPositions[2 * i] = position.x();
            planetPositions[2 * i + 1] = position.y();
            coordMap.put(planets.get(i), position);
        }

        //Calcul des coordonnées des étoiles
        this.stars = this.catalogue.stars();
        starPositions = new double[2 * stars.size()];
        for (int i = 0; i < stars().size(); i++) {
            CartesianCoordinates position = applyProjection(stars.get(i).equatorialPos());
            starPositions[2 * i] = position.x();
            starPositions[2 * i + 1] = position.y();
            coordMap.put(stars.get(i), position);
        }

    }

    /**
     * Retourne le Soleil
     *
     * @return le Soleil
     */
    public Sun sun() {
        return sun;
    }

    /**
     * Retourne la position du Soleil
     *
     * @return la position du Soleil
     */
    public CartesianCoordinates sunPosition() {
        return sunPosition;
    }

    /**
     * Retourne la Lune
     *
     * @return la Lune
     */
    public Moon moon() {
        return moon;
    }

    /**
     * Retourne la position de la Lune
     *
     * @return la position de la Lune
     */
    public CartesianCoordinates moonPosition() {
        return moonPosition;
    }

    /**
     * Retourne la liste des planètes
     *
     * @return la liste des planètes
     */
    public List<Planet> planets() {
        return planets;
    }

    /**
     * Retourne le tableau des positions des planètes
     *
     * @return le tableau des positions des planètes
     */
    public double[] planetPositions() {
        return planetPositions.clone();
    }

    /**
     * Retourne la liste des étoiles
     *
     * @return la liste des étoiles
     */
    public List<Star> stars() {
        return stars;
    }

    /**
     * Retourne le tableau des positions des étoiles
     *
     * @return le tableau des positions des étoiles
     */
    public double[] starPositions() {
        return starPositions.clone();
    }

    /**
     * Retourne le set d'astérismes
     *
     * @see StarCatalogue#asterisms()
     */
    public Set<Asterism> asterism() {
        return catalogue.asterisms();
    }

    /**
     * Retourne la liste des indices des étoiles de l'astérisme donné dans le catalogue
     *
     * @param asterism l'astérisme
     * @see StarCatalogue#asterismIndices(Asterism)
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        return catalogue.asterismIndices(asterism);
    }

    /**
     * Retourne l'objet le plus proche du point de référence avec une distance inférieure à la distance maximale donnée
     *
     * @param point le point de référence
     * @param max   la distance maximale
     * @return l'objet le plus proche du point de référence avec une distance inférieure à la distance maximale donnée
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates point, double max) {
        CelestialObject closest = Collections.min(coordMap.keySet(),
                                                  Comparator.comparingDouble(a -> squaredDistance(point, coordMap.get(a))));
        return (Math.sqrt(squaredDistance(point, coordMap.get(closest))) <= max) ? Optional.of(closest) : Optional.empty();
    }

    private double squaredDistance(CartesianCoordinates point, CartesianCoordinates c) {
        double distX = point.x() - c.x();
        double distY = point.y() - c.y();
        return distX * distX + distY * distY;
    }

    private CartesianCoordinates applyProjection(EquatorialCoordinates position) {
        return stereographicProjection.apply(equatorialToHorizontalConversion.apply(position));
    }

}
