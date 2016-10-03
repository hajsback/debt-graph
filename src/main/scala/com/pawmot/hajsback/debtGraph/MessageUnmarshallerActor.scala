package com.pawmot.hajsback.debtGraph

import akka.actor.{Actor, Props}
import com.codemettle.reactivemq.model.AMQMessage
import com.pawmot.hajsback.debtGraph.model.Transaction
import play.api.libs.json.Json

class MessageUnmarshallerActor extends Actor {
  implicit val transactionFormat = Json.format[Transaction]

  override def receive = {
    case AMQMessage(body, properties, headers) =>
      val json = Json.parse(body.asInstanceOf[String])
      val transaction = Json.fromJson[Transaction](json).asOpt

      transaction.foreach(t => println(t))
      if (transaction.isEmpty) {
        println(s"Unable to unmarshal the message: $body")
      }
  }
}

object MessageUnmarshallerActor {
  def props = Props[MessageUnmarshallerActor]
}