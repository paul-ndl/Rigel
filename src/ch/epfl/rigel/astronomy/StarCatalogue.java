package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Un catalogue d'étoiles et d'astérismes
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class StarCatalogue {

    private final List<Star> stars;
    private final Map<Asterism, List<Integer>> map = new HashMap<>();

    /**
     * Construit un catalogue d'étoiles et d'astérismes avec les arguments donnés
     * @param stars
     *          la liste d'étoiles
     * @param asterisms
     *          la liste d'astérismes
     * @throws IllegalArgumentException
     *          si un astérisme contient une étoile qui ne fait pas partie de la liste d'étoiles
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms){
        for(Asterism a : asterisms){
            Preconditions.checkArgument(stars.containsAll(a.stars()));
        }

        this.stars = List.copyOf(stars);

        List<Integer> list = new ArrayList<>();
        for(Asterism a : asterisms){
            for(Star s : a.stars()){
                list.add(stars.indexOf(s));
            }
            map.put(a, List.copyOf(list));
            list.clear();
        }
    }

    /**
     * Retourne la liste d'étoiles
     * @return la liste d'étoiles
     */
    public List<Star> stars(){
        return stars;
    }

    /**
     * Retourne le set d'astérismes
     * @return le set d'astérismes
     */
    public Set<Asterism> asterisms(){
        return map.keySet();
    }

    /**
     * Retourne la liste des indices des étoiles de l'astérisme donné dans le catalogue
     * @param asterism
     *          l'astérisme
     * @return la liste des indices des étoiles de l'astérisme donné dans le catalogue
     */
    public List<Integer> asterismIndices (Asterism asterism){
        Preconditions.checkArgument(asterisms().contains(asterism));
        return map.get(asterism);
    }

    /**
     * Un bâtisseur de catalogue d'étoiles et d'astérismes
     *
     * @author Paul Nadal (300843)
     * @author Alexandre Brun (302477)
     */
    public final static class Builder{

        private final List<Star> stars = new ArrayList();
        private final List<Asterism> asterisms = new ArrayList<>();

        /**
         * Ajoute l'étoile au catalogue en cours de construction
         * @param star
         *          l'étoile
         * @return le bâtisseur
         */
        public Builder addStar(Star star){
            stars.add(star);
            return this;
        }

        /**
         * Retourne une vue non modifiable sur les étoiles du catalogue en cours de construction
         * @return une vue non modifiable sur les étoiles du catalogue en cours de construction
         */
        public List<Star> stars(){
            return Collections.unmodifiableList(stars);
        }

        /**
         * Ajoute l'astérisme au catalogue en cours de construction
         * @param asterism
         *          l'astérisme
         * @return le bâtisseur
         */
        public Builder addAsterism(Asterism asterism){
            asterisms.add(asterism);
            return this;
        }

        /**
         * Retourne une vue non modifiable sur les astérismes du catalogue en cours de construction
         * @return une vue non modifiable sur les astérismes du catalogue en cours de construction
         */
        public List<Asterism> asterisms(){
            return Collections.unmodifiableList(asterisms);
        }

        /**
         * Demande au chargeur d'ajouter les étoiles et/ou astérismes qu'il obtient depuis le flot d'entrée
         * @param inputStream
         *          le flot d'entrée
         * @param loader
         *          le chargeur
         * @return le bâtisseur
         * @throws IOException
         *          en cas d'erreur d'entrée/sortie
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException{
            loader.load(inputStream, this);
            return this;
        }

        /**
         * Retourne le catalogue contenant les étoiles et astérismes ajoutés au bâtisseur
         * @return le catalogue contenant les étoiles et astérismes ajoutés au bâtisseur
         */
        public StarCatalogue build(){
            return new StarCatalogue(stars, asterisms);
        }

    }

    /**
     * Un chargeur de catalogue d'étoiles et d'astérismes
     *
     * @author Paul Nadal (300843)
     * @author Alexandre Brun (302477)
     */
    public interface Loader{

        /**
         * Charge les étoiles et/ou astérismes du flot d'entrée et les ajoute au bâtisseur
         * @param inputStream
         *          le flot d'entrée
         * @param builder
         *          le bâtisseur
         * @throws IOException
         *          en cas d'erreur entrée/sortie
         */
        void load(InputStream inputStream, Builder builder) throws IOException;

    }
}
