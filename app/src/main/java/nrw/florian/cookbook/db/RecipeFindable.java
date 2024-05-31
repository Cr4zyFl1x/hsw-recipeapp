package nrw.florian.cookbook.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nrw.florian.cookbook.db.recipe.RecipeEntity;

/**
 * @author Florian J. Kleine-Vorholt
 * @param <T> The entity type to find by recipe
 */
public interface RecipeFindable<T> {

    /**
     * Finds all entities by recipe
     * @param recipe The recipe
     * @return A list of entities. An empty list if no entities were found
     */
    default List<T> findByRecipe(RecipeEntity recipe)
    {
        if (recipe == null || recipe.getId() <= 0) {
            return new ArrayList<>();
        }
        return findByRecipeId(recipe.getId());
    }

    /**
     * Finds all entities by recipe id
     * @param recipeId The recipe id
     * @return A list of entities. An empty list if no entities were found
     */
    List<T> findByRecipeId(int recipeId);
}