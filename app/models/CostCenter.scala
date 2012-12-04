package models

import org.joda.time.LocalDate

/**
 * Centros de custo que pagarão as despesas
 */
case class CostCenter(nome: String, dataFechamento: LocalDate, valorPlanejado: Double) {
  override def toString = nome
}

object CostCenter{
  def all = List(CostCenter("Dinheiro", LocalDate.now(), 0), CostCenter("Cartao Credito - Master", LocalDate.now(), 0))

  def allNames = all.map(_.nome)

  def fromName(name: String): Option[CostCenter] = {
    all.filter(_.nome == name) match {
      case Nil => None
      case first :: _ => Some(first)
    }
  }
}
