package info.gehrels.voting;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class FemalePredicateTest {
	private static final Candidate ALICE = new Candidate("Alice", true);
	private static final Candidate BOB = new Candidate("Bob", false);
	private final ElectionCalculationListener mock = mock(ElectionCalculationListener.class);
	private final FemalePredicate condition = new FemalePredicate(mock);

	@Test
	public void femaleCandidatesAreQualified() {
		assertThat(condition.apply(ALICE), is(true));
	}

	@Test
	public void doesNotReportToElectionCalculationListenerWhenCandidatesAreQualified() {
		condition.apply(ALICE);

		verify(mock, never()).candidateNotQualified(isA(Candidate.class), anyString());
	}

	@Test
	public void nonFemaleCandidatesAreNotQualified() {
		assertThat(condition.apply(BOB), is(false));
	}

	@Test
	public void reportsToElectionCalculationListenerWhenCandidatesAreNotQualified() {
		condition.apply(BOB);

		verify(mock).candidateNotQualified(BOB, "The candidate is not female.");
	}
}