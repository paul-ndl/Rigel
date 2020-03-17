package ch.epfl.rigel.astronomy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class StarCatalogue {

    private List<Star> stars;
    private List<Asterism> asterisms;

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
    }

    public List<Star> stars(){
        return stars;
    }

    public Set<Asterism> asterisms(){
        return new HashSet<>(asterisms);
    }

    public List<Integer> asterismIndices (Asterism asterism){
        List<Integer> indices = new ArrayList<>();
        for(Star a : asterism.stars()){
            for (int s=0; s<stars.size(); ++s){
                if(a.hipparcosId()==stars.get(s).hipparcosId()){
                    indices.add(s);
                }
            }
        }
        return indices;
    }
}
