package example;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Javadocs
 */
public class JUnit4Rule implements TestRule {

    private final StartStoppable[] startStoppables;

    private JUnit4Rule(StartStoppable... startStoppables) {
        this.startStoppables = startStoppables;
    }

    public static JUnit4Rule rule(StartStoppable... startStoppables) {
        return new JUnit4Rule(startStoppables);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                List<Throwable> errors = new ArrayList<Throwable>();

                startingQuietly(description, errors);
                try {
                    base.evaluate();
                    succeededQuietly(description, errors);
                } catch (@SuppressWarnings("deprecation") org.junit.internal.AssumptionViolatedException e) {
                    errors.add(e);
                    skippedQuietly(e, description, errors);
                } catch (Throwable e) {
                    errors.add(e);
                    failedQuietly(e, description, errors);
                } finally {
                    finishedQuietly(description, errors);
                }

                MultipleFailureException.assertEmpty(errors);
            }
        };
    }

    private void startingQuietly(Description description, List<Throwable> errors) {
        for (StartStoppable startStoppable : startStoppables) {
            startStoppable.start();
        }
    }

    private void succeededQuietly(Description description, List<Throwable> errors) {

    }

    private void skippedQuietly(AssumptionViolatedException e, Description description, List<Throwable> errors) {

    }

    private void failedQuietly(Throwable e, Description description, List<Throwable> errors) {

    }

    private void finishedQuietly(Description description, List<Throwable> errors) {
        for (StartStoppable startStoppable : startStoppables) {
            startStoppable.stop();
        }
    }
}
