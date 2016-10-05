package com.pawmot.hajsback.debtGraph

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.pawmot.hajsback.debtGraph.DebtsAndCreditsActor.GetDebtsAndCredits
import com.pawmot.hajsback.debtGraph.model.{CreditsAndDebitsDto, SumOfTransactions}
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration._

class DebtGraphServer(private val debtsAndCredits: ActorRef)(implicit private val system: ActorSystem) extends SprayJsonSupport with DefaultJsonProtocol {
  implicit private val materializer = ActorMaterializer()

  implicit private val timeout = Timeout(2 seconds)

  implicit private val sumOfTransactionsFormat = jsonFormat2(SumOfTransactions)
  implicit private val creditsAndDebitsDtoFormat = jsonFormat2(CreditsAndDebitsDto)

  private val route =
      pathPrefix("debtsAndCredits" / Segment) { email =>
        pathEnd {
          get {
            complete (
              (debtsAndCredits ? GetDebtsAndCredits(email)).mapTo[CreditsAndDebitsDto]
            )
          }
        }
      }

  // TODO: configuration
  Http().bindAndHandle(route, "localhost", 8081)
}

object DebtGraphServer {
  def apply(debtsAndCredits: ActorRef)(implicit system: ActorSystem) = new DebtGraphServer(debtsAndCredits)
}