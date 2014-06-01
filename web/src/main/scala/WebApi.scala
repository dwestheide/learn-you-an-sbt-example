package com.hipstercoffee
package web

import unfiltered.filter.Plan
import unfiltered.request._
import unfiltered.response._
import unfiltered.filter.Plan.Intent

object WebApi extends Plan {
  override def intent = {
    case _ => TeaPot ~> ResponseString("Come back later when the coffee is ready")
  }
}