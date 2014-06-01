package com.hipstercoffee
package web

import unfiltered.filter.Plan
import unfiltered.request._
import unfiltered.response._

import argonaut._
import Argonaut._
import argonaut.integrate.unfiltered._

import org.joda.money.Money

import application.model._
import application.BeansCatalogue

object WebApi extends Plan {
  override def intent = {
    case req @ Path("/beans") => req match {
      case GET(_) =>
        JsonResponse(BeansCatalogue.list)
      case _ =>
        MethodNotAllowed
    }
    case req @ Path(Seg("beans" :: beanId :: Nil)) => req match {
      case GET(_) =>
        BeansCatalogue.findByIdentity(beanId) match {
          case Some(bean) =>
            JsonResponse(bean)
          case None =>
            NotFound
        }
      case _ =>
        MethodNotAllowed
    }

    case req @ Path("equipment") => req match {
      case GET(_) =>
        ResponseString("the list of equipment we have on offer")
      case _ =>
        MethodNotAllowed
    }
    case req => req match {
      case GET(_) =>
        TeaPot ~> ResponseString("COME BACK LATER WHEN THE COFFEE IS READY!")
      case _ =>
        MethodNotAllowed ~> ResponseString("Don't try that again!")
    }
  }

  implicit def BeansEncodeJson: EncodeJson[Beans] =
    jencode4L((b: Beans) =>
      (b.identity, b.name, b.availability, b.price))(
        "id", "name", "availability", "price")

  implicit def AvailabilityEncodeJson: EncodeJson[Availability] =
    EncodeJson((a: Availability) => jString(a.toString))

  implicit def MoneyEncodeJson: EncodeJson[Money] =
    EncodeJson((m: Money) => jString(m.toString))

}