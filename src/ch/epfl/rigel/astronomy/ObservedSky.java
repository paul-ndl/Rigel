package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
import static ch.epfl.rigel.astronomy.MoonModel.MOON;
import static ch.epfl.rigel.astronomy.SunModel.SUN;

public class ObservedSky {

    private final StarCatalogue catalogue;

    private final Sun sun;
    private final Moon moon;
    private final List<Planet> planets = new ArrayList<>();
    private final List<Star> stars;
    private final CartesianCoordinates sunPosition;
    private final CartesianCoordinates moonPosition;
    private final double[] planetPositions = new double[14];
    private final double[] starPositions;

    private final Map<CelestialObject, CartesianCoordinates> coordMap = new HashMap<>();

    public ObservedSky(ZonedDateTime when, GeographicCoordinates where, StereographicProjection stereographicProjection, StarCatalogue catalogue){
        final double daysSinceJ2010 = J2010.daysUntil(when);
        final EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(when);
        final EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(when, where);
        this.catalogue = catalogue;

        sun = SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        moon = MOON.at(daysSinceJ2010, eclipticToEquatorialConversion);
        PlanetModel.ALL.stream().filter(p -> p!=PlanetModel.EARTH).forEach(p -> planets.add(p.at(daysSinceJ2010, eclipticToEquatorialConversion)));
        stars = List.copyOf(catalogue.stars());

        sunPosition = stereographicProjection.apply(equatorialToHorizontalConversion.apply(sun.equatorialPos()));
        coordMap.put(sun, sunPosition);

        moonPosition = stereographicProjection.apply(equatorialToHorizontalConversion.apply(moon.equatorialPos()));
        coordMap.put(moon, moonPosition);

        for (int i = 0; i < 7; i++) {
            planetPositions[2*i] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(planets.get(i).equatorialPos())).x();
            planetPositions[2*i+1] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(planets.get(i).equatorialPos())).y();
            coordMap.put(planets.get(i), CartesianCoordinates.of(planetPositions[2*i], planetPositions[2*i+1]));
        }

        starPositions = new double[stars.size()*2];
        for (int i = 0; i < stars().size(); i++) {
            starPositions[2*i] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(stars.get(i).equatorialPos())).x();
            starPositions[2*i+1] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(stars.get(i).equatorialPos())).y();
            coordMap.put(stars.get(i), CartesianCoordinates.of(starPositions[2*i], starPositions[2*i+1]));
        }

    }

    public Sun sun(){
        return sun;
    }

    public CartesianCoordinates sunPosition(){
        return sunPosition;
    }

    public Moon moon(){
        return moon;
    }

    public CartesianCoordinates moonPosition(){
        return moonPosition;
    }

    public List<Planet> planets(){
        return List.copyOf(planets);
    }

    public double[] planetPositions(){
        return planetPositions.clone();
    }

    public List<Star> stars(){
        return List.copyOf(stars);
    }

    public double[] starPositions(){
        return starPositions.clone();
    }

    public Set<Asterism> asterism(){
        return catalogue.asterisms();
    }

    public List<Integer> asterismIndices(Asterism asterism){
        return catalogue.asterismIndices(asterism);
    }

    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates point, double max) {
        final CelestialObject closest = Collections.min(coordMap.keySet(), (a, b) -> compare(point, coordMap.get(a), coordMap.get(b)));
        return (compare(point, coordMap.get(closest), point)<=max) ? Optional.of(closest) : Optional.empty();
    }

    private static Integer compare(CartesianCoordinates point, CartesianCoordinates a, CartesianCoordinates b){
        return Double.compare(
                (point.x()-a.x())*(point.x()-a.x()) + (point.y()-a.y())*(point.y()-a.y()),
                (point.x()-b.x())*(point.x()-b.x()) + (point.y()-b.y())*(point.y()-b.y()));
    }

}
