package info.gehrels.voting.singleTransferableVote;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import info.gehrels.voting.Candidate;
import org.apache.commons.math3.fraction.BigFraction;

import static info.gehrels.voting.singleTransferableVote.VotesForCandidateCalculation.calculateVotesForCandidate;

public class WeightedInclusiveGregoryMethod<CANDIDATE_TYPE extends Candidate> implements
	VoteWeightRedistributionMethod<CANDIDATE_TYPE> {
	private final STVElectionCalculationListener<?> electionCalculationListener;

	public WeightedInclusiveGregoryMethod(STVElectionCalculationListener<CANDIDATE_TYPE> electionCalculationListener) {
		this.electionCalculationListener = electionCalculationListener;
	}

	@Override
	public final VoteWeightRedistributor<CANDIDATE_TYPE> redistributorFor() {
		return new WigmVoteWeightRedistributor<CANDIDATE_TYPE>();
	}

	private final class WigmVoteWeightRedistributor<CANDIDATE_TYPE extends Candidate> implements VoteWeightRedistributor<CANDIDATE_TYPE> {
		@Override
		public ImmutableList<BallotState<CANDIDATE_TYPE>> redistributeExceededVoteWeight(CANDIDATE_TYPE winner, BigFraction quorum,
		                                                                       ImmutableCollection<BallotState<CANDIDATE_TYPE>> ballotStates) {
			Builder<BallotState<CANDIDATE_TYPE>> resultBuilder = ImmutableList.builder();

			BigFraction votesForCandidate = calculateVotesForCandidate(winner, ballotStates);
			BigFraction excessiveVotes = votesForCandidate.subtract(quorum);
			BigFraction excessiveFractionOfVoteWeight = excessiveVotes.divide(votesForCandidate);

			for (BallotState<CANDIDATE_TYPE> ballotState : ballotStates) {
				if (ballotState.getPreferredCandidate().orNull() == winner) {
					BigFraction newVoteWeight = ballotState.getVoteWeight().multiply(excessiveFractionOfVoteWeight);
					BallotState<CANDIDATE_TYPE> newBallotState = ballotState.withVoteWeight(newVoteWeight);
					electionCalculationListener.voteWeightRedistributed(excessiveFractionOfVoteWeight,
					                                                    newBallotState.getBallotId(),
					                                                    newBallotState.getVoteWeight());
					resultBuilder.add(newBallotState);
				} else {
					resultBuilder.add(ballotState);
				}
			}

			return resultBuilder.build();

		}

	}
}
