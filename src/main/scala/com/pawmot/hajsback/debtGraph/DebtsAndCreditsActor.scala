package com.pawmot.hajsback.debtGraph

import akka.actor.{Actor, ActorRef, Props}
import com.pawmot.hajsback.debtGraph.DebtGraphActor.{GetTransactionSummary, TransactionSummary}
import com.pawmot.hajsback.debtGraph.DebtsAndCreditsActor.{GetDebtsAndCredits, UseDebtGraph}
import com.pawmot.hajsback.debtGraph.model.{CreditsAndDebitsDto, SumOfTransactions}

class DebtsAndCreditsActor extends Actor {
  var debtGraph: ActorRef = _

  override def receive = {
    case UseDebtGraph(debtGraph) =>
      this.debtGraph = debtGraph

    case GetDebtsAndCredits(email) =>
      debtGraph ! GetTransactionSummary(email, sender)

    case TransactionSummary(summary, originalSender) =>
      val data = summary.foldRight(CreditsAndDebitsDto(Nil, Nil))((p, dto) => {
        if (p._2 > 0) CreditsAndDebitsDto(SumOfTransactions(p._1, p._2) :: dto.credits, dto.debts)
        else if (p._2 < 0) CreditsAndDebitsDto(dto.credits, SumOfTransactions(p._1, -p._2) :: dto.debts)
        else dto
      })

      originalSender ! data
  }
}

object DebtsAndCreditsActor {
  def props = Props[DebtsAndCreditsActor]

  case class UseDebtGraph(debtGraph: ActorRef)
  case class GetDebtsAndCredits(email: String)
}