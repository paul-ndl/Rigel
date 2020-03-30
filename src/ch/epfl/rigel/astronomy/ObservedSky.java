package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
import static ch.epfl.rigel.astronomy.MoonModel.MOON;
import static ch.epfl.rigel.astronomy.SunModel.SUN;

public class ObservedSky {

    private final double daysSinceJ2010;
    private final EclipticToEquatorialConversion eclipticToEquatorialConversion;
    private final EquatorialToHorizontalConversion equatorialToHorizontalConversion;
    private final StereographicProjection stereographicProjection;
    private final StarCatalogue catalogue;

    public ObservedSky(ZonedDateTime when, GeographicCoordinates where, StereographicProjection stereographicProjection, StarCatalogue catalogue){
        this.daysSinceJ2010 = J2010.daysUntil(when);
        this.eclipticToEquatorialConversion = new EclipticToEquatorialConversion(when);
        this.equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(when, where);
        this.stereographicProjection = stereographicProjection;
        this.catalogue = catalogue;
    }

    public Sun sun(){
        return SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
    }

    public CartesianCoordinates sunPosition(){
        return stereographicProjection.apply(equatorialToHorizontalConversion.apply(sun().equatorialPos()));
    }

    public Moon moon(){
        return MOON.at(daysSinceJ2010, eclipticToEquatorialConversion);
    }

    public CartesianCoordinates moonPosition(){
        return stereographicProjection.apply(equatorialToHorizontalConversion.apply(moon().equatorialPos()));
    }

    public List<Planet> planets(){
        final List<Planet> planets = new ArrayList<>();
        PlanetModel.ALL.stream().filter(p -> p.name()!="Terre").forEach(p -> planets.add(p.at(daysSinceJ2010, eclipticToEquatorialConversion)));
        return planets;
    }

    public double[] planetPositions(){
        double[] cartesianCoordinates = new double[14];
        for (int i = 0; i < planets().size(); i++) {
            cartesianCoordinates[2*i] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(planets().get(i).equatorialPos())).x();
            cartesianCoordinates[2*i+1] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(planets().get(i).equatorialPos())).y();
        }
        return cartesianCoordinates;
    }

    public List<Star> stars(){
        return List.copyOf(catalogue.stars());
    }

    public double[] starPositions(){
        double[] cartesianCoordinates = new double[2*stars().size()];
        for (int i = 0; i < stars().size(); i++) {
            cartesianCoordinates[2*i] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(stars().get(i).equatorialPos())).x();
            cartesianCoordinates[2*i+1] = stereographicProjection.apply(equatorialToHorizontalConversion.apply(stars().get(i).equatorialPos())).y();
        }
        return cartesianCoordinates;
    }

    public Set<Asterism> asterism(){
        return catalogue.asterisms();
    }

    public List<Integer> asterismIndices(Asterism asterism){
        return catalogue.asterismIndices(asterism);
    }

    public CelestialObject objectClosestTo(CartesianCoordinates point, double max){
        final Optional<CelestialObject> object;
        final Map<Double, CelestialObject> map = new HashMap<>();
        final double distMin;
        map.put(calculSquaredDistance(point, sunPosition()), sun());
        map.put(calculSquaredDistance(point,moonPosition()), moon());
        for(int i=0; i<planets().size(); ++i){
            map.put(calculSquaredDistance(point,CartesianCoordinates.of(planetPositions()[2*i], planetPositions()[2*i+1])), planets().get(i));
        }
        for(int j=0; j<stars().size(); ++j){
            map.put(calculSquaredDistance(point,CartesianCoordinates.of(starPositions()[2*j], starPositions()[2*j+1])), stars().get(j));
        }
        distMin = Collections.min(map.keySet());
        if(Math.sqrt(distMin)<max){
            object = Optional.of(map.get(distMin));
        } else {
            object = Optional.empty();
        }
        return (object.isPresent() ? object.get() : null);
    }

    private double calculSquaredDistance(CartesianCoordinates point, CartesianCoordinates position){
        final double deltaX = point.x()-position.x();
        final double deltaY = point.y()-position.y();
        return deltaX*deltaX + deltaY*deltaY;
    }
}
