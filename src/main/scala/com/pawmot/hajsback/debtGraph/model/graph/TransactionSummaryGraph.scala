package com.pawmot.hajsback.debtGraph.model.graph

import scala.util.Try

class TransactionSummaryGraph {
  type VertexIdx = Int
  type Amount = Int

  private var vertices = Map.empty[String, VertexIdx]
  private var verticesToEmails = Map.empty[VertexIdx, String]
  private var edges = Map.empty[VertexIdx, Map[VertexIdx, Amount]].withDefaultValue(Map.empty.withDefaultValue(0))

  def addTransaction(from: String, to: String, amount: Int): Try[Unit] = {
    Try {
      addUser(from)
      addUser(to)

      modifyEdge(from, to, amount)
      modifyEdge(to, from, -amount)

      edges(vertices(from))(vertices(to))
    }
  }

  def getTransactionSummary(email: String): List[(String, Int)] = {
    edges(vertices(email)).toList.map(p => (verticesToEmails(p._1), p._2))
  }

  private def addUser(email: String): Unit = {
    if (!vertices.contains(email)) {
      val vertexId = vertices.size
      vertices = vertices + (email -> vertexId)
      verticesToEmails = verticesToEmails + (vertexId -> email)
    }
  }

  private def modifyEdge(from: String, to: String, amount: Amount) = {
    edges = edges + (vertices(from) -> (edges(vertices(from)) + (vertices(to) -> (edges(vertices(from))(vertices(to)) + amount))))
  }
}

object TransactionSummaryGraph {
  def apply() = new TransactionSummaryGraph()
}
