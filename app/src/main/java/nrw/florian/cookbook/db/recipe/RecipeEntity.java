package nrw.florian.cookbook.db.recipe;

import android.graphics.Bitmap;

import java.util.List;
import java.util.stream.Collectors;

import nrw.florian.cookbook.db.ingredient.IngredientEntity;
import nrw.florian.cookbook.db.recipeproperty.RecipeProperty;
import nrw.florian.cookbook.db.recipeproperty.RecipePropertyEntity;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class RecipeEntity {

    private int _id;

    private String title;

    private Bitmap image;

    private int defaultServings;

    private String instructions;

    private RecipeBaseCategory category;

    private RecipeDifficulty difficulty;

    private int workTime;

    private int calories;

    private List<RecipePropertyEntity> recipeProperties;

    private List<IngredientEntity> ingredients;


    public RecipeEntity(final String title,
                        final Bitmap image,
                        final int defaultServings,
                        final String instructions,
                        final RecipeBaseCategory category,
                        final RecipeDifficulty difficulty,
                        final int workTime,
                        final int calories,
                        final List<RecipePropertyEntity> recipeProperties,
                        final List<IngredientEntity> ingredients)
    {

        this.title = title;
        this.image = image;
        this.defaultServings = defaultServings;
        this.instructions = instructions;
        this.category = category;
        this.difficulty = difficulty;
        this.workTime = workTime;
        this.calories = calories;
        this.recipeProperties = recipeProperties;
        this.ingredients = ingredients;
    }

    public RecipeEntity(final int _id,
                        final String title,
                        final Bitmap image,
                        final int defaultServings,
                        final String instructions,
                        final RecipeBaseCategory category,
                        final RecipeDifficulty difficulty,
                        final int workTime,
                        final int calories,
                        final List<RecipePropertyEntity> recipeProperties,
                        final List<IngredientEntity> ingredients)
    {
        this._id = _id;
        this.title = title;
        this.image = image;
        this.defaultServings = defaultServings;
        this.instructions = instructions;
        this.category = category;
        this.difficulty = difficulty;
        this.workTime = workTime;
        this.calories = calories;
        this.recipeProperties = recipeProperties;
        this.ingredients = ingredients;
    }

    public void setDefaultServings(int defaultServings)
    {
        this.defaultServings = defaultServings;
    }

    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }

    public void setCategory(RecipeBaseCategory category)
    {
        this.category = category;
    }

    public void setDifficulty(RecipeDifficulty difficulty)
    {
        this.difficulty = difficulty;
    }

    public void setWorkTime(int workTime)
    {
        this.workTime = workTime;
    }

    public void setCalories(int calories)
    {
        this.calories = calories;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public void setRecipeProperties(List<RecipePropertyEntity> recipeProperties)
    {
        this.recipeProperties = recipeProperties;
    }

    public int getId()
    {
        return _id;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public int getDefaultServings()
    {
        return defaultServings;
    }

    public int getCalories()
    {
        return calories;
    }

    public int getWorkTime()
    {
        return workTime;
    }

    public RecipeBaseCategory getCategory()
    {
        return category;
    }

    public RecipeDifficulty getDifficulty()
    {
        return difficulty;
    }

    public String getInstructions()
    {
        return instructions;
    }

    public String getTitle()
    {
        return title;
    }

    public List<RecipeProperty> getRecipeProperties()
    {
        return this.recipeProperties.stream().map(RecipePropertyEntity::getProperty)
                .collect(Collectors.toList());
    }

    public List<RecipePropertyEntity> getRecipePropertyEntities()
    {
        return this.recipeProperties;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    void setId(int _id) {
        this._id = _id;
    }
}
