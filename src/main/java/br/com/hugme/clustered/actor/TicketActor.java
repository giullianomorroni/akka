package br.com.hugme.clustered.actor;

import java.util.List;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.Cluster.InfoLogger$;
import br.com.hugme.clustered.domain.Ticket;

public class TicketActor extends UntypedActor {

	private Cluster cluster = Cluster.get(getContext().system());

	@Override
	public void preStart() {
		cluster.subscribe(getSelf(), Ticket.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(Object arg) throws Exception {
		if (arg instanceof Ticket) {
			Ticket ticket = (Ticket) arg;
			InfoLogger$ infoLogger = cluster.InfoLogger();
			infoLogger.logInfo(" Processed " + ticket.toString());
		}
		if (arg instanceof List) {
			List<Ticket> tiqts = (List<Ticket>) arg;
			for (Ticket ticket : tiqts) {
				InfoLogger$ infoLogger = cluster.InfoLogger();
				infoLogger.logInfo(" Processed " + ticket.toString());
			}
		}
		
	}

}
