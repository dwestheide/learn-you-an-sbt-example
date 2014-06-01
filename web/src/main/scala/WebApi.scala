package com.hipstercoffee
package web

import unfiltered.filter.Plan
import unfiltered.request._
import unfiltered.response._
import unfiltered.filter.Plan.Intent

object WebApi extends Plan {
  override def intent = {
    case req @ Path("beans") => req match {
      case GET(_) =>
        Ok ~> ResponseString("the list of beans we have on offer")
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
}