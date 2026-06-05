package com.edomex.kiliantRSP.models.Base;

import java.math.BigDecimal;

public interface CantidadBase {
    Integer getCve_principal();
    String getNeyemp();
    String getPeriodo();
    String getCheque();
    BigDecimal getPercep();
    BigDecimal getDed();
    String getBanco();
    String getNum_cuenta();
    String getNum_recibo();
    String getLug_pago();
    String getAnio();
}
