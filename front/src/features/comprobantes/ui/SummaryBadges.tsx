'use client';

export default function SummaryBadges({
  totalPercepciones, totalDeducciones, neto, recibo, banco
}: {
  totalPercepciones: number; totalDeducciones: number; neto: number;
  recibo?: string; banco?: string;
}) {
  const fmt = (n:number)=>'$'+n.toLocaleString('es-MX',{minimumFractionDigits:2, maximumFractionDigits:2});
  return (
    <div className="rb-badges">
      <div className="rb-badge">
        <span>Total de percepciones:</span>
        <strong>{fmt(totalPercepciones)}</strong>
      </div>
      <div className="rb-badge">
        <span>Total de deducciones:</span>
        <strong>{fmt(totalDeducciones)}</strong>
      </div>
      <div className="rb-badge ok">
        <span>Neto:</span>
        <strong>{fmt(neto)}</strong>
      </div>
      {recibo && (
        <div className="rb-badge">
          <span>Recibo:</span>
          <strong>{fmt(Number(recibo))}</strong>
        </div>
      )}
      {banco && (
        <div className="rb-badge">
          <span>Bancomer:</span>
          <strong className="mono">{banco}</strong>
        </div>
      )}
    </div>
  );
}
