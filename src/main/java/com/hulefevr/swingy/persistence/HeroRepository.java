package com.hulefevr.swingy.persistence;

import com.hulefevr.swingy.model.hero.Hero;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour la persistence des héros
 */
public interface HeroRepository {
    
    /**
     * Sauvegarde un héros dans la base de données
     * Si le héros existe déjà (même nom), il est mis à jour
     * @param hero Le héros à sauvegarder
     */
    void save(Hero hero);
    
    /**
     * Récupère un héros par son nom
     * @param name Le nom du héros
     * @return Le héros, ou Optional.empty() si non trouvé
     */
    Optional<Hero> findByName(String name);
    
    /**
     * Récupère tous les héros
     * @return Liste de tous les héros
     */
    List<Hero> findAll();
    
    /**
     * Supprime un héros par son nom
     * @param name Le nom du héros à supprimer
     * @return true si le héros a été supprimé, false sinon
     */
    boolean deleteByName(String name);
    
    /**
     * Vérifie si un héros existe
     * @param name Le nom du héros
     * @return true si le héros existe
     */
    boolean exists(String name);
}