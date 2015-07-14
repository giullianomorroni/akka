package br.com.hugme.clustered.actor.external;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import br.com.hugme.clustered.actor.TicketActor;
import br.com.hugme.clustered.domain.Ticket;

public class DataLoad extends UntypedActor {

	private Cluster cluster = Cluster.get(getContext().system());
	private Cancellable thread;

	@Override
	public void preStart() {

		//FIXME mock
		Ticket ticket = new Ticket(Calendar.getInstance().getTimeInMillis(), "Novo Ticket");

		FiniteDuration delay = Duration.create(500, TimeUnit.MILLISECONDS);
		FiniteDuration interval = Duration.create(1, TimeUnit.MINUTES);
		thread = getContext()
				.system()
				.scheduler()
				.schedule(delay, interval, getSelf(), ticket, getContext().dispatcher(), null);
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
