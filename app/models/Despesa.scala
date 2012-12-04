package models

import org.joda.time.LocalDateTime
import collection.mutable
import play.api.db.DB
import anorm._
import play.api.Play.current

/**
 * Represa uma despesa: compra de um sapato, pagamento de uma conta de energia, etc
 */
case class Despesa(description: String, data: LocalDateTime, valor: BigDecimal, categorias: Set[CostType], lacamentos: List[LancamentoDespesa]) {
  override def toString = (description + " = " + valor)
}


object Despesa {

  def persist(record: Despesa) {
    // repository.add(record)
    DB.withConnection {
      implicit c =>
        SQL("insert into despesa(description,cost_value) values({description},{cost_value})").on(
          'description -> record.description, 'cost_value -> record.valor.toDouble
        ).executeUpdate()
    }
  }

  def all: List[Despesa] = {
    DB.withConnection {
      implicit c =>
        SQL("select * from despesa").apply().map(row => Despesa(row[String]("description"), new LocalDateTime(), row[java.math.BigDecimal]("cost_value"), null, null)).toList
    }
    //repository.toList
  }

  def take(description: String, value: BigDecimal, costCenter: CostCenter, times: Int, categorias: CostType*): Despesa = {
    val now: LocalDateTime = LocalDateTime.now()
    new Despesa(description, now, value, categorias.toSet, LancamentoDespesa.parcelaDespesa(value, now, times, costCenter))
  }

}
