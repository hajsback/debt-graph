package com.pawmot.hajsback.debtGraph

import akka.actor.{Actor, ActorRef, Props}
import com.codemettle.reactivemq.ReActiveMQExtension
import com.codemettle.reactivemq.ReActiveMQMessages.{ConnectionEstablished, ConsumeFromQueue, GetAuthenticatedConnection}
import com.codemettle.reactivemq.model.AMQMessage
import com.pawmot.hajsback.debtGraph.AMQConsumerActor.StartConsumption

class AMQConsumerActor extends Actor {
  val debtGraphQueueName = "debt_graph"
  var unmarshaller: ActorRef = _

  override def receive = {
    case StartConsumption(unmarshaller) =>
      this.unmarshaller = unmarshaller
      // TODO: configuration
      ReActiveMQExtension(context.system).manager ! GetAuthenticatedConnection("nio://localhost:61616", "hajsback", "secr3t")

    case ConnectionEstablished(req, connActor) =>
      connActor ! ConsumeFromQueue(debtGraphQueueName)
      context become liveConnection
  }

  def liveConnection: Receive = {
    case msg @ AMQMessage(_, _, _) =>
      unmarshaller ! msg
  }
}

object AMQConsumerActor {
  def props = Props[AMQConsumerActor]

  case class StartConsumption(unmarshaller: ActorRef)
}
