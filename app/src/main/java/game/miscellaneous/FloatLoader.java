package game.miscellaneous;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Class utility for quickly load a float from dimen resources.
 *
 * @author De Pace
 */
public class FloatLoader {

    private final Resources resources;

    /**
     * Creates a new <code>FloatLoader</code> with the given Context.
     *
     * @param context <code>Context</code> from which to retrieve resources
     */
    public FloatLoader(Context context) {
        this.resources = context.getResources();
    }

    /**
     * Returns a float value from resources.
     *
     * @param id ID of the float value in resources, from R.dimen
     * @return the requested float value
     */
    public float getFloat(int id) {
        TypedValue typedValue = new TypedValue();
        resources.getValue(id, typedValue, true);
        return typedValue.getFloat();
    }

}
