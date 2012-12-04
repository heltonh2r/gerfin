package controllers

import play.api._
import data.Form
import data.Forms._
import play.api.mvc._
import models._
import org.joda.time.LocalDate
import models.CostType
import java.util.{Locale, Date}
import java.text.{DecimalFormatSymbols, DecimalFormat, NumberFormat}

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def costForm = Form(tuple("categoria" -> nonEmptyText, "valor" -> nonEmptyText, "data" -> date("dd/MM/yyyy"), "vezes" -> number(1, 100), "centroCusto" -> nonEmptyText))
  def costTypeForm = Form(tuple("name" -> nonEmptyText, "fatherCost" -> text))

  val filledForm = costForm.fill((CostType.allNames match {
    case Nil => ""
    case xs :: tail => xs
  }, "", new Date(), 1, CostCenter.allNames.head))


  def cost = Action.apply({
    Ok(views.html.expense(Despesa.all, CostType.allNames, CostCenter.allNames, filledForm))
  })

  val BR_LOCALE: Locale = new Locale("pt", "BR")
  val BR_CURRENCY_PREFIX: String = "R$ "

  def launchCost = Action { implicit request =>
    costForm.bindFromRequest().fold(
      errors => {
        BadRequest(views.html.expense(Despesa.all, CostType.allNames, CostCenter.allNames, errors))
      },
      (param: (String, String, Date, Int, String)) => {
        println(param)
        val (costType, value, costDate, times, costCenter) = param
        val valor = NumberFormat.getCurrencyInstance(BR_LOCALE).parse(BR_CURRENCY_PREFIX + value).doubleValue()
        Despesa.persist(Despesa.take(costType + costDate, BigDecimal(valor), CostCenter.fromName(costCenter).get, times, CostType.fromName(costType).get))
        Redirect(routes.Application.cost())
      })
  }

  def listCostTypes = Action.apply(
    Ok(views.html.costType(CostType.all, costTypeForm))
  )

  def newCostType = Action { implicit request => {
    costTypeForm.bindFromRequest().fold(
      erros => BadRequest(views.html.costType(CostType.all, erros)),
      (form: (String, String)) => {
        val (costTypeName, fatherCost) = form
        CostType(0, costTypeName, CostType.fromName(fatherCost).getOrElse(null)).persist
        Redirect(routes.Application.listCostTypes())
      }
    )}
  }
  
}