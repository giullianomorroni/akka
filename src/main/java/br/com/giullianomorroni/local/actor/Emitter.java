package br.com.giullianomorroni.local.actor;

/**
 *
 * (C) Copyright 2014 Roy Russo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import br.com.giullianomorroni.local.command.Command;
import br.com.giullianomorroni.local.event.Event;


/**
 * The Emitter receives a Command and emits Events that are picked up by subscribed Handler. The Handler is listening for emitted Event.class
 */
public class Emitter extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof Command) {
            log.info("Emitting Event: " + msg); // never seems to print to log. wtf?
            String data = ((Command) msg).getData();
            getContext().system().eventStream().publish(new Event(data));
        }
    }


}
