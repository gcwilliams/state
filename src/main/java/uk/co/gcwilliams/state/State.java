package uk.co.gcwilliams.state;

import java.util.function.Function;

/**
 * The state monad
 *
 * Created by GWilliams on 20/02/2016.
 */
public class State<T, S> {

    private final Function<S, BiTuple<T, S>> run;

    /**
     * Constructor
     *
     * @param run the function to run
     */
    public State(Function<S, BiTuple<T, S>> run) {
        this.run = run;
    }

    /**
     * Creates a state of the specified value
     *
     * @param value the value
     * @return the state
     */
    public static <T, S> State<T, S> of(T value) {
        return new State<>(s -> BiTuple.of(value, s));
    }

    /**
     * Gets the state
     *
     * @return the state
     */
    public static <S> State<S, S> get() {
        return new State<>((s) -> BiTuple.of(s, s));
    }

    /**
     * Modifies the state
     *
     * @param fn the function which will modify the state
     * @return the state
     */
    public static <T, S> State<T, S> modify(Function<S, S> fn) {
        return new State<>((s) -> BiTuple.of(null, fn.apply(s)));
    }

    /**
     * Puts the state
     *
     * @param state the state
     * @return the state
     */
    public static <T, S> State<T, S> put(S state) {
        return new State<>((s) -> BiTuple.of(null, state));
    }

    /**
     * Chain (aka flatMap, bind)
     *
     * @param fn the function to chain
     * @return the new state
     */
    public <NT> State<NT, S> chain(Function<T, State<NT, S>> fn) {
        return new State<>(s -> {
            BiTuple<T, S> result = this.run.apply(s);
            return fn.apply(result._1).run(result._2);
        });
    }

    /**
     * Map
     *
     * @param fn the function
     * @return the new state
     */
    public <NT> State<NT, S> map(Function<T, NT> fn) {
        return this.chain(v -> State.of(fn.apply(v)));
    }

    /**
     * Runs the state
     *
     * @param state the starting state
     * @return the result
     */
    public BiTuple<T, S> run(S state) {
        return this.run.apply(state);
    }

    /**
     * Gets the result
     *
     * @param state the starting state
     * @return the result
     */
    public T exec(S state) {
        return run(state)._1;
    }

    /**
     * Gets the state
     *
     * @param state the starting state
     * @return the state
     */
    public S execState(S state) {
        return run(state)._2;
    }
}
