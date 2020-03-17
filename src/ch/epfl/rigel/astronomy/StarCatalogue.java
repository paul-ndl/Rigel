package ch.epfl.rigel.astronomy;

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
        return map.get(asterism);
    }
}
