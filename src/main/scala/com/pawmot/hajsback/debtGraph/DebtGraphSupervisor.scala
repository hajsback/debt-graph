package com.pawmot.hajsback.debtGraph

import akka.actor.{Actor, Props}
import com.pawmot.hajsback.debtGraph.AMQConsumerActor.StartConsumption
import com.pawmot.hajsback.debtGraph.DebtGraphSupervisor.Start

class DebtGraphSupervisor extends Actor {
  override def receive = {
    case Start =>
      context.actorOf(AMQConsumerActor.props, "JmsConsumer") ! StartConsumption(context.actorOf(MessageUnmarshallerActor.props))
  }
}

object DebtGraphSupervisor {
  def props = Props[DebtGraphSupervisor]

  case class Start()
}

