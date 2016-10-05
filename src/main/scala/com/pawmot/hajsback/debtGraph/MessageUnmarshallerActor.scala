package com.pawmot.hajsback.debtGraph

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.codemettle.reactivemq.model.AMQMessage
import com.pawmot.hajsback.debtGraph.MessageUnmarshallerActor.UseDebtGraphActor
import com.pawmot.hajsback.debtGraph.model.Transaction
import play.api.libs.json.Json

import scala.collection.mutable

class MessageUnmarshallerActor extends Actor with ActorLogging {
  implicit val transactionFormat = Json.format[Transaction]
  var debtGraphActor: ActorRef = _
  val transactionQueue = mutable.Queue[Transaction]()

  // TODO: refactor to context.become
  override def receive = {
    case UseDebtGraphActor(debtGraphActor) =>
      this.debtGraphActor = debtGraphActor
      transactionQueue.foreach(t => debtGraphActor ! t)
      transactionQueue.clear()

    case AMQMessage(body, properties, headers) =>
      val json = Json.parse(body.asInstanceOf[String])
      val transaction = Json.fromJson[Transaction](json).asOpt

      if (transaction.isEmpty) {
        log.warning(s"Unable to unmarshal the message: $body")
      } else {
        transaction.foreach(t => {
          if (debtGraphActor == null) {
            transactionQueue.enqueue(t)
          } else {
            debtGraphActor ! t
          }
        })
      }
  }
}

object MessageUnmarshallerActor {
  def props = Props[MessageUnmarshallerActor]

  case class UseDebtGraphActor(debtGraphActor: ActorRef)
}