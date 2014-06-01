package com.hipstercoffee
package application

import org.joda.money.Money
import org.joda.time.DateTime

package object model {
  case class Beans(
    identity: String,
    name: String,
    availability: Availability,
    price: Money)

  sealed trait Availability
  object Availability {
    case object Immediately extends Availability
    case object SoldOut extends Availability
  }

}