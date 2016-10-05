package com.pawmot.hajsback.debtGraph.model.graph

import org.scalatest.{Matchers, WordSpec}

class TransactionSummaryGraphSpec extends WordSpec with Matchers {
  "Transaction summary graph" when {
    "receives a transaction" should {
      "return a proper summary" in {
        val graph = TransactionSummaryGraph()
        graph.addTransaction("em1@dom.com", "em2@dom.com", 100)
        graph.getTransactionSummary("em1@dom.com") should be (List("em2@dom.com" -> 100))
      }
    }
  }
}
