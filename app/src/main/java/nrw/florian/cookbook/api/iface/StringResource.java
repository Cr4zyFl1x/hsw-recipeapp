package nrw.florian.cookbook.api.iface;

import android.content.Context;

/**
 * Interface for getting string resources.
 * @author Florian J. Kleine-Vorholt
 */
public interface StringResource {

    /**
     * Returns a matching string resource for a given context.
     * @param ctx The context to get the string from.
     * @return The string resource.
     */
    String getString(final Context ctx);
}