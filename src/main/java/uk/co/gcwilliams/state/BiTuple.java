package uk.co.gcwilliams.state;

/**
 * A tuple of two values
 *
 * Created by GWilliams on 20/02/2016.
 */
public class BiTuple<T1, T2> {

    public final T1 _1;

    public final T2 _2;

    /**
     * Constructor
     *
     * @param _1 the first value
     * @param _2 the second value
     */
    private BiTuple(T1 _1, T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }

    /**
     * Creates a tuple of the specified value
     *
     * @param _1 the first value
     * @param _2 the second value
     * @return the tuple
     */
    public static <T1, T2> BiTuple<T1, T2> of(T1 _1, T2 _2) {
        return new BiTuple<>(_1, _2);
    }
}
