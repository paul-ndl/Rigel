package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
import static ch.epfl.rigel.astronomy.MoonModel.MOON;
import static ch.epfl.rigel.astronomy.SunModel.SUN;

public final class ObservedSky {

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

    public ObservedSky(ZonedDateTime when, GeographicCoordinates where, StereographicProjection stereographicProjection, StarCatalogue catalogue){
        double daysSinceJ2010 = J2010.daysUntil(when);
        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(when);
        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(when, where);
        this.catalogue = catalogue;
        //Calcul des coordonnées du Soleil
        sun = SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        sunPosition = stereographicProjection.apply(equatorialToHorizontalConversion.apply(sun.equatorialPos()));
        coordMap.put(sun, sunPosition);
        //Calcul des coordonnées de la Lune
        moon = MOON.at(daysSinceJ2010, eclipticToEquatorialConversion);
        moonPosition = stereographicProjection.apply(equatorialToHorizontalConversion.apply(moon.equatorialPos()));
        coordMap.put(moon, moonPosition);
        //Calcul des coordonnées des planètes
        PlanetModel.ALL.stream().filter(p -> p!=PlanetModel.EARTH).forEach(p -> planets.add(p.at(daysSinceJ2010, eclipticToEquatorialConversion)));
        for (int i = 0; i < 7; i++) {
                planetPositions[2*i] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(planets.get(i).equatorialPos())).x();
                planetPositions[2*i+1] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(planets.get(i).equatorialPos())).y();
                coordMap.put(planets.get(i), CartesianCoordinates.of(planetPositions[2*i], planetPositions[2*i+1]));
        }
        //Calcul des coordonnées des étoiles
        this.stars = this.catalogue.stars();
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
        return planets;
    }

    public double[] planetPositions(){
        return planetPositions.clone();
    }

    public List<Star> stars(){
        return stars;
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
        CelestialObject closest = Collections.min(coordMap.keySet(),
                                        Comparator.comparingDouble(a -> squaredDistance(point, coordMap.get(a))));
        return (Math.sqrt(squaredDistance(point, coordMap.get(closest))) <= max) ? Optional.of(closest) : Optional.empty();
    }

    private double squaredDistance(CartesianCoordinates point, CartesianCoordinates c){
        double distX = point.x()-c.x();
        double distY = point.y()-c.y();
        return distX*distX + distY*distY;
    }

}
