package nrw.florian.cookbook.db.recipe;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import nrw.florian.cookbook.db.ingredient.IngredientEntity;
import nrw.florian.cookbook.db.recipeproperty.RecipeProperty;
import nrw.florian.cookbook.db.recipeproperty.RecipePropertyEntity;

/**
 * @author Florian J. Kleine-Vorholt
 */
public class RecipeEntity implements Serializable {

    private static final long serialVersionUID = 4345678900830242L;

    /**
     * The id of the recipe.
     */
    private int _id;

    /**
     * The title of the recipe.
     */
    private String title;

    /**
     * The image of the recipe.
     */
    private Bitmap image;

    /**
     * The default servings of the recipe.
     */
    private int defaultServings;

    /**
     * The instructions of the recipe.
     */
    private String instructions;

    /**
     * The category of the recipe.
     */
    private RecipeBaseCategory category;

    /**
     * The difficulty of the recipe.
     */
    private RecipeDifficulty difficulty;

    /**
     * The work time of the recipe.
     */
    private int workTime;

    /**
     * The calories of the recipe.
     */
    private int calories;

    /**
     * The properties of the recipe.
     */
    private List<RecipePropertyEntity> recipeProperties;

    /**
     * The ingredients of the recipe.
     */
    private List<IngredientEntity> ingredients;



    /**
     * Creates a new recipe.
     * @param title             The title of the recipe.
     * @param image             The image of the recipe.
     * @param defaultServings   The default servings of the recipe.
     * @param instructions      The instructions of the recipe.
     * @param category          The category of the recipe.
     * @param difficulty        The difficulty of the recipe.
     * @param workTime          The work time of the recipe.
     * @param calories          The calories of the recipe.
     * @param recipeProperties  The properties of the recipe.
     * @param ingredients       The ingredients of the recipe.
     */
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


    /**
     * Creates a new recipe.
     * @param _id               The id of the recipe.
     * @param title             The title of the recipe.
     * @param image             The image of the recipe.
     * @param defaultServings   The default servings of the recipe.
     * @param instructions      The instructions of the recipe.
     * @param category          The category of the recipe.
     * @param difficulty        The difficulty of the recipe.
     * @param workTime          The work time of the recipe.
     * @param calories          The calories of the recipe.
     * @param recipeProperties  The properties of the recipe.
     * @param ingredients       The ingredients of the recipe.
     */
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


    /**
     * Sets the default servings of the recipe.
     * @param defaultServings The default servings of the recipe.
     */
    public void setDefaultServings(int defaultServings)
    {
        this.defaultServings = defaultServings;
    }


    /**
     * Sets the instructions of the recipe.
     * @param instructions The instructions of the recipe.
     */
    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }


    /**
     * Sets the category of the recipe.
     * @param category The category of the recipe.
     */
    public void setCategory(RecipeBaseCategory category)
    {
        this.category = category;
    }


    /**
     * Sets the difficulty of the recipe.
     * @param difficulty The difficulty of the recipe.
     */
    public void setDifficulty(RecipeDifficulty difficulty)
    {
        this.difficulty = difficulty;
    }


    /**
     * Sets the work time of the recipe.
     * @param workTime The work time of the recipe.
     */
    public void setWorkTime(int workTime)
    {
        this.workTime = workTime;
    }


    /**
     * Sets the calories of the recipe
     * @param calories The calories of the recipe.
     */
    public void setCalories(int calories)
    {
        this.calories = calories;
    }


    /**
     * Sets the title of the recipe.
     * @param title The title of the recipe.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * Sets the image of the recipe.
     * @param image The image of the recipe.
     */
    public void setImage(Bitmap image)
    {
        this.image = image;
    }


    /**
     * Sets the properties of the recipe.
     * @param recipeProperties The properties of the recipe.
     */
    public void setRecipeProperties(List<RecipePropertyEntity> recipeProperties)
    {
        this.recipeProperties = recipeProperties;
    }


    /**
     * Sets the ingredients of the recipe.
     * @return The ingredients of the recipe.
     */
    public int getId()
    {
        return _id;
    }


    /**
     * Gets the title of the recipe.
     * @return The title of the recipe.
     */
    public Bitmap getImage()
    {
        return image;
    }


    /**
     * Gets the default servings of the recipe.
     * @return The default servings of the recipe.
     */
    public int getDefaultServings()
    {
        return defaultServings;
    }


    /**
     * Gets the instructions of the recipe.
     * @return The instructions of the recipe.
     */
    public int getCalories()
    {
        return calories;
    }


    /**
     * Gets the work time of the recipe.
     * @return The work time of the recipe.
     */
    public int getWorkTime()
    {
        return workTime;
    }


    /**
     * Gets the category of the recipe.
     * @return The category of the recipe.
     */
    public RecipeBaseCategory getCategory()
    {
        return category;
    }


    /**
     * Gets the difficulty of the recipe.
     * @return The difficulty of the recipe.
     */
    public RecipeDifficulty getDifficulty()
    {
        return difficulty;
    }


    /**
     * Gets the instructions of the recipe.
     * @return The instructions of the recipe.
     */
    public String getInstructions()
    {
        return instructions;
    }


    /**
     * Gets the title of the recipe.
     * @return The title of the recipe.
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Gets the properties of the recipe.
     * <p>
     *     <b>NOTE!</b> Gets them as the enum object!
     * </p>
     * @return The properties of the recipe.
     */
    public List<RecipeProperty> getRecipeProperties()
    {
        return this.recipeProperties.stream().map(RecipePropertyEntity::getProperty)
                .collect(Collectors.toList());
    }


    /**
     * Gets the properties of the recipe.
     * <p>
     *     <b>NOTE!</b> Gets them as the entity object!
     * </p>
     * @return The properties of the recipe.
     */
    public List<RecipePropertyEntity> getRecipePropertyEntities()
    {
        return this.recipeProperties;
    }


    /**
     * Sets the ingredients of the recipe.
     * @param ingredients The ingredients of the recipe.
     */
    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }


    /**
     * Gets the ingredients of the recipe.
     * @return The ingredients of the recipe.
     */
    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }


    /**
     * Sets the id of the recipe.
     * @param _id The id of the recipe.
     */
    void setId(int _id) {
        this._id = _id;
    }
}