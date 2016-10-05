package com.pawmot.hajsback.debtGraph

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.pawmot.hajsback.debtGraph.DebtGraphActor.{GetTransactionSummary, TransactionSummary}
import com.pawmot.hajsback.debtGraph.model.Transaction
import com.pawmot.hajsback.debtGraph.model.graph.TransactionSummaryGraph

class DebtGraphActor extends Actor with ActorLogging {
  val graph = TransactionSummaryGraph()

  override def receive = {
    case t @ Transaction(source, target, amount) =>
      log.info(s"Received $t")

      graph.addTransaction(source, target, amount)

    case GetTransactionSummary(email, originalSender) =>
      sender ! TransactionSummary(graph.getTransactionSummary(email), originalSender)
  }
}

object DebtGraphActor {
  def props = Props[DebtGraphActor]

  case class GetTransactionSummary(email: String, sender: ActorRef)
  case class TransactionSummary(summary: List[(String, Int)], originalSender: ActorRef)
}
