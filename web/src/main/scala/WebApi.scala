package com.hipstercoffee
package web

import unfiltered.filter.Plan
import unfiltered.request._
import unfiltered.response._
import unfiltered.filter.Plan.Intent

object WebApi extends Plan {
  override def intent = {
    case req => req match {
      case GET(_) =>
        TeaPot ~> ResponseString("COME BACK LATER WHEN THE COFFEE IS READY!")
      case _ =>
        MethodNotAllowed ~> ResponseString("Don't try that again!")
    }
  }
}