package nrw.florian.cookbook.db.recipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.db.ingredient.IngredientEntity;
import nrw.florian.cookbook.db.ingredient.IngredientUnit;
import nrw.florian.cookbook.db.recipeproperty.RecipeProperty;
import nrw.florian.cookbook.db.recipeproperty.RecipePropertyEntity;

public class RecipeDatabaseOpenHelperTest {

    private Context context;

    private RecipeEntity recipe1;
    private RecipeEntity recipe2;
    private RecipeEntity recipe3;

    @Before
    public void setUp() {

        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Recipe 1
        recipe1 = new RecipeEntity("Cordon Bleu", null, 4, "Machen 1\nMachen 2", RecipeBaseCategory.MAIN, RecipeDifficulty.MEDIUM, 50, 600, new ArrayList<>(), new ArrayList<>());
        recipe1.getIngredients().add(new IngredientEntity("Schnitzel", 4, IngredientUnit.PIECE));
        recipe1.getIngredients().add(new IngredientEntity("Käse", 4, IngredientUnit.SLICE));
        recipe1.getIngredients().add(new IngredientEntity("Schinken", 4, IngredientUnit.SLICE));
        recipe1.getIngredients().add(new IngredientEntity("Paniermehl", 100, IngredientUnit.GRAMS));
        recipe1.getRecipePropertyEntities().add(new RecipePropertyEntity(RecipeProperty.MEAT));
        recipe1.getRecipePropertyEntities().add(new RecipePropertyEntity(RecipeProperty.WARM));

        // Recipe 2
        recipe2 = new RecipeEntity("Schokomilch", BitmapFactory.decodeResource(context.getResources(), R.drawable.exampleimage_recipe), 4, "Machen 1\nMachen 2", RecipeBaseCategory.DRINK, RecipeDifficulty.EASY, 20, 800, new ArrayList<>(), new ArrayList<>());
        recipe2.getIngredients().add(new IngredientEntity("Zartbitterschokolade", 200, IngredientUnit.GRAMS));
        recipe2.getIngredients().add(new IngredientEntity("Milch", 2, IngredientUnit.LITER));
        recipe2.getIngredients().add(new IngredientEntity("Zucker", 50, IngredientUnit.GRAMS));
        recipe2.getRecipePropertyEntities().add(new RecipePropertyEntity(RecipeProperty.VEGETARIAN));
        recipe2.getRecipePropertyEntities().add(new RecipePropertyEntity(RecipeProperty.COLD));

        // Recipe 3
        recipe3 = new RecipeEntity("Lasagne", BitmapFactory.decodeResource(context.getResources(), R.drawable.exampleimage_recipe), 4, "Machen 1\nMachen 2", RecipeBaseCategory.MAIN, RecipeDifficulty.MEDIUM, 30, 500, new ArrayList<>(), new ArrayList<>());
        recipe3.getIngredients().add(new IngredientEntity("Käse", 4, IngredientUnit.PIECE));
        recipe3.getIngredients().add(new IngredientEntity("Lasagneplatten", 15, IngredientUnit.SLICE));
        recipe3.getIngredients().add(new IngredientEntity("Tomatensauce", 200, IngredientUnit.MILLILITERS));
        recipe3.getRecipePropertyEntities().add(new RecipePropertyEntity(RecipeProperty.VEGETARIAN));
    }

    @Test
    public void testSaveRecipe()
    {
        try (RecipeDatabaseOpenHelper db = new RecipeDatabaseOpenHelper(context)) {
            assertTrue(db.saveOrUpdate(recipe1));
            assertTrue(db.saveOrUpdate(recipe2));
            assertTrue(db.saveOrUpdate(recipe3));

            assertEquals(1, recipe1.getId());
            assertEquals(2, recipe2.getId());
            assertEquals(3, recipe3.getId());

            // Get
            RecipeEntity recipe1a = db.findById(recipe1.getId()).orElseThrow(RuntimeException::new);
            RecipeEntity recipe2a = db.findById(recipe2.getId()).orElseThrow(RuntimeException::new);
            RecipeEntity recipe3a = db.findById(recipe3.getId()).orElseThrow(RuntimeException::new);

            assertNotNull(recipe1a);
            assertNotNull(recipe2a);
            assertNotNull(recipe3a);

            assertNull(recipe1a.getImage());
            assertNotNull(recipe2a.getImage());
            assertNotNull(recipe3a.getImage());

            assertEquals(recipe1.getIngredients().size(), recipe1a.getIngredients().size());
            assertEquals(recipe2.getIngredients().size(), recipe2a.getIngredients().size());
            assertEquals(recipe3.getIngredients().size(), recipe3a.getIngredients().size());

            assertEquals(recipe1.getRecipePropertyEntities().size(), recipe1a.getRecipePropertyEntities().size());
            assertEquals(recipe2.getRecipePropertyEntities().size(), recipe2a.getRecipePropertyEntities().size());
            assertEquals(recipe3.getRecipePropertyEntities().size(), recipe3a.getRecipePropertyEntities().size());
        }
    }
}