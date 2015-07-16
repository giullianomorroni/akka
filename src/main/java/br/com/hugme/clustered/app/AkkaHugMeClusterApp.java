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
			startup(new String[] { "2551", "2552" });
		else
			startup(args);
	}

	public static ActorSystem hugmeSystem;
	public static ActorSystem hugmeDataLoadSystem;

	public static void startup(String[] ports) {
		for (String port : ports) {
			// Override the configuration of the port
			Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load());
			// Create an Akka system
			hugmeSystem = ActorSystem.create("AkkaHugMeClusterSystem", config);
			// Create an actor that handles cluster domain events
			hugmeSystem.actorOf(Props.create(ActorClusterListener.class), "clusterListener");
			//init data load
			//system.actorOf(Props.create(DataLoad.class, ""));
		}

		{
			// Override the configuration of the port
			Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + 0).withFallback(ConfigFactory.load("dataload"));
			hugmeDataLoadSystem = ActorSystem.create("AkkaHugMeDataLoadClusterSystem", config);
			//init data load
			hugmeDataLoadSystem.actorOf(Props.create(DataLoad.class), "dataLoad");
		}

	}

}
