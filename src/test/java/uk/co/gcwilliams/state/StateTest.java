package uk.co.gcwilliams.state;

import org.junit.Test;

import java.util.function.Function;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * The state tests
 *
 * Created by GWilliams on 20/02/2016.
 */
public class StateTest {

    @Test
    public void of() {

        // arrange
        State<String, Integer> homer = State.of("Homer");

        // act
        BiTuple<String, Integer> ran = homer.run(0);

        // assert
        assertThat(ran._1, equalTo("Homer"));
        assertThat(ran._2, equalTo(0));
    }

    @Test
    public void chain() {

        // arrange
        State<String, Integer> homer = State.of("Homer");

        // act
        BiTuple<String, Integer> ran = homer.chain(message -> State.of(format("Hello %s", message))).run(0);

        // assert
        assertThat(ran._1, equalTo("Hello Homer"));
        assertThat(ran._2, equalTo(0));
    }

    @Test
    public void map() {

        // arrange
        State<String, Integer> homer = State.of("Homer");

        // act
        BiTuple<String, Integer> ran = homer.map(message -> format("Hello %s", message)).run(0);

        // assert
        assertThat(ran._1, equalTo("Hello Homer"));
        assertThat(ran._2, equalTo(0));
    }

    @Test
    public void get() {

        // arrange
        State<String, Integer> homer = State.of("Homer");

        // act
        BiTuple<String, Integer> ran = homer.chain(h -> State.<Integer>get().map(s -> format("Homer has state %s", s))).run(0);

        // assert
        assertThat(ran._1, equalTo("Homer has state 0"));
        assertThat(ran._2, equalTo(0));
    }

    @Test
    public void modify() {

        // arrange
        State<String, Integer> homer = State.of("Homer");

        // act
        BiTuple<String, Integer> ran = homer.chain(h -> State.<String, Integer>modify(s -> ++s).map(v -> h)).run(0);

        // assert
        assertThat(ran._1, equalTo("Homer"));
        assertThat(ran._2, equalTo(1));
    }

    @Test
    public void put() {

        // arrange
        State<String, Integer> homer = State.of("Homer");

        // act
        BiTuple<String, Integer> ran = homer.chain(h -> State.<String, Integer>put(100).map(v -> h)).run(0);

        // assert
        assertThat(ran._1, equalTo("Homer"));
        assertThat(ran._2, equalTo(100));
    }

    @Test
    public void leftIdentity() {

        // arrange
        Function<Integer, State<Integer, Object>> fn = i -> State.of(i * i);

        // act
        int left = State.of(10).chain(fn).run(null)._1;
        int right = fn.apply(10).run(null)._1;

        // assert
        assertThat(left, equalTo(right));
    }

    @Test
    public void rightIdentity() {

        // act
        int left = State.of(10).chain(State::<Integer, Object>of).run(null)._1;
        int right = State.of(10).run(null)._1;

        // assert
        assertThat(left, equalTo(right));
    }

    @Test
    public void associativity() {

        Function<Integer, State<Integer, Object>> addOne = i -> State.of(i + 1);
        Function<Integer, State<Integer, Object>> addTwo = i -> State.of(i + 2);
        Function<Integer, State<Integer, Object>> addThree = i -> State.of(i + 3);

        // act
        int left = State.of(10).chain(addOne).chain(addTwo).run(null)._1;
        int right = State.of(10).chain(addThree).run(null)._1;

        // assert
        assertThat(left, equalTo(right));
    }
}
