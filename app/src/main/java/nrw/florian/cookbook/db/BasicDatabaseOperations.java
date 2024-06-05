package nrw.florian.cookbook.db;

import java.util.List;
import java.util.Optional;

/**
 * Encapsulates basic database operations.
 * <p>
 *     Like: findAll(), findById(), saveOrUpdate(), remove(), exists()
 * </p>
 * @param <T> The type of the entity
 * @author Florian J. Kleine-Vorholt
 */
public interface BasicDatabaseOperations<T> {


    /**
     * Finds all entities for the defined EntityType and returns them as a list.
     *
     * @return the entities in a list. If no entity is found an empty list is returned.
     */
    List<T> findAll();


    /**
     * Finds an entity by its id.
     *
     * @param id The id of the entity to find
     * @return the entity if found, otherwise an empty optional
     */
    Optional<T> findById(final int id);


    /**
     * Saves or updates an entity.
     * @param entity The entity to save or update
     */
    boolean saveOrUpdate(final T entity);


    /**
     * Removes an entity.
     * @param entity The entity to remove.
     */
    boolean remove(final T entity);


    /**
     * Checks if an entity exists by its id.
     * @param id The id of the entity to check
     * @return true if the entity exists, false otherwise
     */
    boolean exists(final int id);


    /**
     * Returns the number of entities in the Table.
     * <p>
     *     <b>NOTE!:</b> This method should not be used in production because of a high cost of performance!</b>
     * </p>
     * @return number of entities in the table
     */
    default int count()
    {
        return findAll().size();
    }
}
