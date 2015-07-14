package br.com.hugme.clustered.app;

import akka.actor.ActorSystem;
import akka.actor.Props;
import br.com.giullianomorroni.clustered.actor.ActorClusterListener;
import br.com.hugme.clustered.actor.external.DataLoad;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AkkaHugMeClusterApp {

	public static void main(String[] args) {
		if (args.length == 0)
			startup(new String[] { "2551", "2552", "0" });
		else
			startup(args);
	}

	public static void startup(String[] ports) {
		for (String port : ports) {
			// Override the configuration of the port
			Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load());

			// Create an Akka system
			ActorSystem system = ActorSystem.create("AkkaHugMeClusterSystem", config);

			// Create an actor that handles cluster domain events
			system.actorOf(Props.create(ActorClusterListener.class), "clusterListener");

			//init data load
			system.actorOf(Props.create(DataLoad.class));
		}
	}

}