package com.hipstercoffee
package application

import model._
import org.joda.money.{CurrencyUnit, Money}

object BeansCatalogue {
  private val beans = Map(
    "catuai-red-honey" -> Beans(
      "catuai-red-honey",
      "COSTA RICA Hacienda Sonora - Catuaí Red Honey",
      Availability.Immediately,
      Money.of(CurrencyUnit.of("EUR"), 5.9)),
    "yha-hauka-kopi-organic" -> Beans(
      "yha-hauka-kopi-organic",
      "PAPUA NEU GUINEA Yha Hauka Kopi Organic",
      Availability.SoldOut,
      Money.of(CurrencyUnit.of("EUR"), 5.9)
    )
  )

  def list: List[Beans] = beans.values.toList

  def findByIdentity(identity: String): Option[Beans] =
    beans.get(identity)

}
