package br.com.hugme.clustered.actor.external;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import br.com.hugme.clustered.actor.TicketActor;
import br.com.hugme.clustered.app.AkkaHugMeClusterApp;
import br.com.hugme.clustered.domain.Ticket;

public class DataLoad extends UntypedActor {

	private Cluster cluster = Cluster.get(AkkaHugMeClusterApp.hugmeSystem);
	private Cancellable thread;

	@Override
	public void preStart() {

		//FIXME mock
		List<Ticket> tiqts = new ArrayList<Ticket>();
		for(int i=500; i > 0; i--) {
			Ticket ticket = new Ticket(new Long(i), "Novo Ticket");
			tiqts.add(ticket);
		}

		FiniteDuration delay = Duration.create(500, TimeUnit.MILLISECONDS);
		FiniteDuration interval = Duration.create(1, TimeUnit.MINUTES);
		thread = getContext()
				.system()
				.scheduler()
				.schedule(delay, interval, getSelf(), tiqts, getContext().dispatcher(), null);
	}

	@Override
	public void postStop() throws Exception {
		super.postStop();
		thread.cancel();
	}

	@Override
	public void onReceive(Object arg) throws Exception {
		ActorRef actorOf = cluster.system().actorOf(Props.create(TicketActor.class));
		actorOf.tell(arg, actorOf);
	}

}
