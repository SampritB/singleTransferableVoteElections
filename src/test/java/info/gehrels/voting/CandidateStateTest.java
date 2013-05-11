package info.gehrels.voting;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;

public class CandidateStateTest {

	public static final Candidate ARBITRARY_CANDIDATE = new Candidate("Adam", false);

	@Test
	public void hasNotALooserAndNotElectedAsInitialState() {
		CandidateState initialState = new CandidateState(ARBITRARY_CANDIDATE);
		assertThat(initialState.isLooser(), is(false));
		assertThat(initialState.isElected(), is(false));
	}

	@Test
	public void returnsCandidate() {
		CandidateState state = new CandidateState(ARBITRARY_CANDIDATE);
		assertThat(state.getCandidate(), is(ARBITRARY_CANDIDATE));
	}

	@Test
	public void isInitiallyHopefull() {
		CandidateState state = new CandidateState(ARBITRARY_CANDIDATE);
		assertThat(state.isHopeful(), is(true));
	}


	@Test
	public void createsNewStateAsElectedCandidate() {
		CandidateState initialState = new CandidateState(ARBITRARY_CANDIDATE);
		CandidateState electedState = initialState.asElected();

		assertThat(electedState, is(not(sameInstance(initialState))));
		assertThat(electedState.isElected(), is(true));
	}


	@Test
	public void winnersAreNotHopful() {
		CandidateState state = new CandidateState(ARBITRARY_CANDIDATE).asElected();
		assertThat(state.isHopeful(), is(false));
	}

	@Test
	public void createsNewStateAsLooserCandidate() {
		CandidateState initialState = new CandidateState(ARBITRARY_CANDIDATE);
		CandidateState looserState = initialState.asLooser();

		assertThat(looserState, is(not(sameInstance(initialState))));
		assertThat(looserState.isLooser(), is(true));
	}


	@Test
	public void loosersAreNotHopeful() {
		CandidateState state = new CandidateState(ARBITRARY_CANDIDATE).asLooser();
		assertThat(state.isHopeful(), is(false));
	}


}