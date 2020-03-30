package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

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
        PlanetModel.ALL.stream().filter(p -> p.name()!="Terre").forEach(p -> planets.add(p.at(daysSinceJ2010, eclipticToEquatorialConversion)));
        stars = List.copyOf(catalogue.stars());

        sunPosition = stereographicProjection.apply(equatorialToHorizontalConversion.apply(sun.equatorialPos()));
        coordMap.put(sun, sunPosition);

        moonPosition = stereographicProjection.apply(equatorialToHorizontalConversion.apply(moon.equatorialPos()));
        coordMap.put(moon, moonPosition);

        for (int i = 0; i < planets.size(); i++) {
            planetPositions[2*i] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(planets.get(i).equatorialPos())).x();
            planetPositions[2*i+1] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(planets.get(i).equatorialPos())).y();
            coordMap.put(planets.get(i), CartesianCoordinates.of(planetPositions[2*i], planetPositions[2*i+1]));
        }

        starPositions = new double[planets.size()*2];
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
        return planetPositions;
    }

    public List<Star> stars(){
        return stars;
    }

    public double[] starPositions(){
        return starPositions;
    }

    public Set<Asterism> asterism(){
        return catalogue.asterisms();
    }

    public List<Integer> asterismIndices(Asterism asterism){
        return catalogue.asterismIndices(asterism);
    }

    public CelestialObject objectClosestTo(CartesianCoordinates point, double max){
        final Optional<CelestialObject> object;
        final Map<Double, CelestialObject> distMap = new HashMap<>();
        for(CelestialObject c : coordMap.keySet()){
            distMap.put(calculSquaredDistance(point, coordMap.get(c)), c);
        }
        final double distMin = Collections.min(distMap.keySet());
        object = (Math.sqrt(distMin)<max) ? Optional.of(distMap.get(distMin)) : Optional.empty();
        return object.get();
    }

    private double calculSquaredDistance(CartesianCoordinates point, CartesianCoordinates position){
        final double deltaX = point.x()-position.x();
        final double deltaY = point.y()-position.y();
        return deltaX*deltaX + deltaY*deltaY;
    }
}
