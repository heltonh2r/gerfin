package models

import play.api.db.DB
import anorm._
import scala.Some
import play.api.Play.current
import collection.immutable.Stream.Empty

/**
 * classificação da despesa, exemplos: Veiculo, Alimentação, etc
 */
case class CostType(id: Int, name: String, fatherCost: CostType) {
  override def toString = name + " " + id + " -> " + fatherCost

  def persist = {
    DB.withConnection {
      implicit c =>
        if (fatherCost == null)
          SQL("insert into tipo_despesa(name) values({name})").on(
            'name -> this.name).executeUpdate()
        else
          SQL("insert into tipo_despesa(name, id_despesa_pai) values({name}, {id_despesa_pai})").on(
            'name -> this.name, 'id_despesa_pai -> fatherCost.id).executeUpdate()
    }
  }
}

object CostType {
  val mapRow: (SqlRow) => CostType = row => CostType(row[Int]("id"), row[String]("name"), findById(row[Option[Int]]("id_despesa_pai").getOrElse(0)))

  def all = {
    DB.withConnection {
      implicit c =>
        SQL("select * from tipo_despesa").apply().map(mapRow).toList
    }
  }

  def findById(id: Int): CostType = {
    if (id == 0) null
    else
      DB.withConnection {
        implicit c =>
          SQL("select * from tipo_despesa where id = {id}").on('id -> id).apply().map(mapRow) match {
            case Empty => null
            case head #:: _ => head
          }
      }
  }

  def allNames = all.map(_.name)

  def fromName(name: String) = {
    all.filter(_.name == name) match {
      case Nil => None
      case xs :: _ => Some(xs)
    }
  }

}