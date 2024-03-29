package game.data;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import game.net.communication.JSONd;
import game.net.communication.RequestMaker;

/**
 * Singleton which maintains user data while advancing in the activities.
 * It also creates {@link RequestMaker} with all the added data, or with the requested data.
 *
 * @author De Pace
 */
public enum UserData {

    DATA;

    private static final String LOG = UserData.class.getSimpleName();

    private final HashMap<Enum<?>, Object> dataMap = new HashMap<>();

    /**
     * Adds some data, mapped with a name.
     *
     * @param name  name to map the data.
     * @param value data to add to the UserData.
     */
    public void addData(Enum<?> name, Object value) {
        dataMap.put(name, value);
    }

    /**
     * Removes some data, previously mapped with a name. If the name is not mapped, it does nothing.
     *
     * @param name name to map the data.
     */
    public void removeData(Enum<?> name) {
        dataMap.remove(name);
    }

    /**
     * Returns some data from a given name.
     *
     * @param name name that maps the requested data.
     * @return the data requested, mapped by tha given name, or null if no mapping is present with that name.
     */
    public Object getData(Enum<?> name) {
        return dataMap.get(name);
    }

    /**
     * Creates a new <code>RequestMaker</code> with default requests given by all the added data.
     *
     * @return the new generated <code>RequestMaker</code>.
     */
    public RequestMaker getRequestMaker() {
        return getRequestMaker(dataMap.keySet());
    }

    /**
     * Creates a new <code>RequestMaker</code> with default requests given by the data mapped by the given names.
     *
     * @param names names of the data previously added to use as default requests for the <code>RequestMaker</code>.
     * @return the new generated <code>RequestMaker</code>.
     */
    public RequestMaker getRequestMakerWithData(Enum<?>... names) {
        return getRequestMaker(Arrays.asList(names));
    }

    private RequestMaker getRequestMaker(Iterable<Enum<?>> it) {
        LinkedList<JSONd> jsonDs = new LinkedList<>();
        for (Enum<?> s : it)
            jsonDs.add(new JSONd(s, dataMap.get(s)));
        return new RequestMaker(jsonDs.toArray(new JSONd[0]));
    }

    @NonNull
    @Override
    public String toString() {
        return "[USERDATA: " + dataMap + "]";
    }

}
