package nrw.florian.cookbook;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import nrw.florian.cookbook.db.shoppinglist.ShoppingListItemDatabaseOpenHelper;

@RunWith(AndroidJUnit4.class)
public class AndroidContextTest {

    /**
     * Test-Context
     */
    private Context context;

    @Before
    public void setUp()
    {
        this.context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    protected final Context getContext()
    {
        return this.context;
    }


    @Test
    public void useAppContext() {

        ShoppingListItemDatabaseOpenHelper db = new ShoppingListItemDatabaseOpenHelper(context);

//        assertTrue(db.saveOrUpdate(new ShoppingListItemEntity( "1", false)));
 //       assertTrue(db.exists(1));
  //      assertTrue(db.remove(new ShoppingListItemEntity(1, "1", false)));
   //     assertFalse(db.exists(1));
    }
}