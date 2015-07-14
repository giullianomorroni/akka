package br.com.giullianomorroni.clustered.transformation;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import br.com.giullianomorroni.clustered.message.TransformationMessages;
import br.com.giullianomorroni.clustered.message.TransformationMessages.TransformationJob;
import br.com.giullianomorroni.clustered.message.TransformationMessages.TransformationResult;

public class TransformationBackend extends UntypedActor {

	Cluster cluster = Cluster.get(getContext().system());

	// subscribe to cluster changes, MemberUp
	@Override
	public void preStart() {
		cluster.subscribe(getSelf(), MemberUp.class);
	}

	// re-subscribe when restart
	@Override
	public void postStop() {
		cluster.unsubscribe(getSelf());
	}

	@Override
	public void onReceive(Object message) {
		if (message instanceof TransformationJob) {
			TransformationJob job = (TransformationJob) message;
			getSender().tell(new TransformationResult(job.getText().toUpperCase()), getSelf());

		} else if (message instanceof CurrentClusterState) {
			CurrentClusterState state = (CurrentClusterState) message;
			for (Member member : state.getMembers()) {
				if (member.status().equals(MemberStatus.up())) {
					register(member);
				}
			}
		} else if (message instanceof MemberUp) {
			MemberUp mUp = (MemberUp) message;
			register(mUp.member());
		} else {
			unhandled(message);
		}
	}

	void register(Member member) {
		if (member.hasRole("frontend")) {
			getContext().actorSelection(member.address() + "/user/frontend").tell(TransformationMessages.BACKEND_REGISTRATION, getSelf());
		}
	}

}