package br.com.hugme.clustered.actor;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.Cluster.InfoLogger$;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import br.com.hugme.clustered.domain.Ticket;

public class TicketActor extends UntypedActor {

	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private Cluster cluster = Cluster.get(getContext().system());

	@Override
	public void preStart() {
		cluster.subscribe(getSelf(), Ticket.class);
	}

	@Override
	public void onReceive(Object arg) throws Exception {
		if (arg instanceof Ticket) {
			Ticket ticket = (Ticket) arg;
			InfoLogger$ infoLogger = cluster.InfoLogger();
			infoLogger.logInfo(" Processed " + ticket.toString());
		}
	}

}
