package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class StarCatalogue {

    private List<Star> stars;
    private List<Asterism> asterisms;
    private Map<Asterism, List<Integer>> map;

    public StarCatalogue(List<Star> stars, List<Asterism> asterisms){
        for(Asterism a : asterisms){
            for(Star s : a.stars()){
                if(!stars.contains(s)){
                    throw new IllegalArgumentException();
                }
            }
        }

        this.stars = List.copyOf(stars);
        this.asterisms = List.copyOf(asterisms);

        List<Integer> list = new ArrayList<>();
        for(Asterism a : asterisms){
            for(Star s : a.stars()){
                for (int i=0; i<stars.size(); ++i){
                    if(s.hipparcosId()==stars.get(i).hipparcosId()){
                        list.add(i);
                    }
                }
            }
            map.put(a, list);
            list.clear();
        }

    }

    public List<Star> stars(){
        return stars;
    }

    public Set<Asterism> asterisms(){
        return new HashSet<>(asterisms);
    }

    public List<Integer> asterismIndices (Asterism asterism){
        Preconditions.checkArgument(asterisms.contains(asterism));
        return map.get(asterism);
    }

    public final static class Builder{
        private List<Star> stars = new ArrayList();
        private List<Asterism> asterisms = new ArrayList<>();

        public Builder addStar(Star star){
            this.stars.add(star);
            return this;
        }

        public List<Star> stars(){
            return Collections.unmodifiableList(stars);
        }

        public Builder addAsterism(Asterism asterism){
            this.asterisms.add(asterism);
            return this;
        }

        public List<Asterism> asterisms(){
            return Collections.unmodifiableList(asterisms);
        }

        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException{
            loader.load(inputStream, this);
            return this;
        }

        public StarCatalogue build(){
            return new StarCatalogue(stars, asterisms);
        }

    }

    public interface Loader{
        public void load(InputStream inputStream, Builder builder) throws IOException;
    }
}