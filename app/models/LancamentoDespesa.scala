package models

import org.joda.time.LocalDateTime
import java.math.{RoundingMode, MathContext}

/**
 * Uma despesa gera o lançamento de 1 ou mais parcelas em um ou mais centro de custo.<br/>
 * Esta classe representa o lançamento de uma parte de uma despesa e um centro de custo
 */
case class LancamentoDespesa(valorParcela: BigDecimal, dataLancamento: LocalDateTime, centroCusto: CostCenter)

object LancamentoDespesa{
  def parcelaDespesa(valor: BigDecimal, dataInicial: LocalDateTime, numeroVezes: Int, centroCusto: CostCenter): List[LancamentoDespesa] = {
    require(numeroVezes > 0)
    require(valor > 0)
    val range =  0 until numeroVezes
    val parcela = (valor / numeroVezes).round(new MathContext(2, RoundingMode.DOWN))
    (for (
      i <- range
    ) yield LancamentoDespesa(parcela, dataInicial.plusMonths(i), centroCusto)).toList
  }


}
