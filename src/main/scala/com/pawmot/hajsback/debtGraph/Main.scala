package com.pawmot.hajsback.debtGraph

import java.util.concurrent.Executors

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import com.pawmot.hajsback.debtGraph.DebtGraphSupervisor.Start

import scala.concurrent.ExecutionContext

object Main extends App {
  implicit val ctx = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))
  val system = ActorSystem("debt-graph")

  val root = system.actorOf(DebtGraphSupervisor.props)
  system.actorOf(DeathWatchActor.props(root))

  root ! Start
}

class DeathWatchActor(actorToWatch: ActorRef) extends Actor {
  context.watch(actorToWatch)

  override def receive = {
    case Terminated(`actorToWatch`) =>
      println("Terminating the system!")
      context.system.terminate()
  }
}

object DeathWatchActor {
  def props(actorToWatch: ActorRef) = Props(new DeathWatchActor(actorToWatch))
}