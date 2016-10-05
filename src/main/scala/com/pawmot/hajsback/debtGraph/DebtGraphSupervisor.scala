package com.pawmot.hajsback.debtGraph

import akka.actor.{Actor, Props}
import com.pawmot.hajsback.debtGraph.AMQConsumerActor.StartConsumption
import com.pawmot.hajsback.debtGraph.DebtGraphSupervisor.Start
import com.pawmot.hajsback.debtGraph.DebtsAndCreditsActor.UseDebtGraph
import com.pawmot.hajsback.debtGraph.MessageUnmarshallerActor.UseDebtGraphActor

class DebtGraphSupervisor extends Actor {
  override def receive = {
    case Start =>
      val jmsConsumer = context.actorOf(AMQConsumerActor.props, "JmsConsumer")
      val unmarshaller = context.actorOf(MessageUnmarshallerActor.props, "MessageUnmarshaller")
      val debtGraph = context.actorOf(DebtGraphActor.props, "DebtGraph")
      val debtsAndCreditsActor = context.actorOf(DebtsAndCreditsActor.props, "DebtsAndCreditsActor")

      jmsConsumer ! StartConsumption(unmarshaller)
      unmarshaller ! UseDebtGraphActor(debtGraph)
      debtsAndCreditsActor ! UseDebtGraph(debtGraph)

      implicit val system = context.system
      DebtGraphServer(debtsAndCreditsActor)
  }
}

object DebtGraphSupervisor {
  def props = Props[DebtGraphSupervisor]

  case class Start()
}

