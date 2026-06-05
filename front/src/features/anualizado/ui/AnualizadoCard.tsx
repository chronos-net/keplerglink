'use client';


import { headerRows, money } from '../const/anualizadoGrid';
import { useAnualizadoGridModel } from '../hooks/useAnualizadoGridModels';
import { AnualizadoResponse } from '../types/anualizado.dto';
import s from './anualizado-grid.module.css';

type Props = { data: AnualizadoResponse };

export default function AnualizadoGridPro({ data }: Props) {
  const m = useAnualizadoGridModel(data);

  return (
    <div className={s.wrapper}>
      <div className={s.tableScroller}>
        <table className={s.table}>
          <thead>
            <tr className={s.topRow}>
              <th className={`${s.sticky} ${s.code}`} />
              <th className={`${s.sticky} ${s.desc}`} />
              {m.bands.map((b) => (
                <th key={`band-${b.idx}`} className={s.bandHead} colSpan={b.size}>
                  {b.label}
                </th>
              ))}
            </tr>

            <tr>
              <th className={`${s.sticky} ${s.code}`}>Clave</th>
              <th className={`${s.sticky} ${s.desc}`}>Concepto</th>
              {m.periods.map((p) => (
                <th key={`h-${p}`} className={s.colHead}>
                  {p}
                </th>
              ))}
            </tr>
          </thead>

          <tbody>
            {headerRows.map((hr) => (
              <tr key={`info-${hr.key}`} className={s.infoRow}>
                <td className={`${s.sticky} ${s.code} ${s.infoHead}`}>{hr.label}</td>
                <td className={`${s.sticky} ${s.desc}`} />
                {m.periods.map((p) => (
                  <td key={`info-${hr.key}-${p}`} className={`${s.mono} ${s.cell}`}>
                    {hr.get(m.plazaByPeriod.get(p)) ?? '—'}
                  </td>
                ))}
              </tr>
            ))}

            <SectionHead title="Percepciones" cols={m.periods.length} />

            {m.percKeys.map((codigo) => (
              <tr key={`perc-${codigo}`}>
                <td className={`${s.sticky} ${s.code} ${s.mono}`}>{codigo}</td>
                <td className={`${s.sticky} ${s.desc}`} title={m.catalogos.percepcionesByCodigo[codigo] ?? codigo}>
                  {m.catalogos.percepcionesByCodigo[codigo] ?? codigo}
                </td>

                {m.periods.map((p) => {
                  const v = m.percByPeriodo.get(p)?.get(codigo);
                  return (
                    <td key={`ded-${codigo}-${p}`} className={`${s.num} ${v === 0 ? s.muted : ''}`}>
                      {v ? money(v) : ''}
                    </td>
                  );
                })}
              </tr>
            ))}

            <TotalRow
              label="Total de Percepciones"
              periods={m.periods}
              getValue={(p) => m.resumenByPeriodo.get(p)?.percepciones ?? 0}
            />

            <SectionHead title="Deducciones" cols={m.periods.length} />

            {m.dedKeys.map((codigo) => (
              <tr key={`ded-${codigo}`}>
                <td className={`${s.sticky} ${s.code} ${s.mono}`}>{codigo}</td>
                <td className={`${s.sticky} ${s.desc}`} title={m.catalogos.deduccionesByCodigo[codigo] ?? codigo}>
                  {m.catalogos.deduccionesByCodigo[codigo] ?? codigo}
                </td>

                {m.periods.map((p) => {
                  const v = m.dedByPeriodo.get(p)?.get(codigo);
                  return (
                    <td key={`ded-${codigo}-${p}`} className={`${s.num} ${v === 0 ? s.muted : ''}`}>
                      {v ? money(v) : ''}
                    </td>
                  );
                })}
              </tr>
            ))}

            <TotalRow
              label="Total de Deducciones"
              periods={m.periods}
              getValue={(p) => m.resumenByPeriodo.get(p)?.deducciones ?? 0}
            />

            <TotalRow
              label="Neto por período"
              periods={m.periods}
              getValue={(p) => m.resumenByPeriodo.get(p)?.neto ?? 0}
              strong
            />
          </tbody>
        </table>
      </div>
    </div>
  );
}

function SectionHead({ title, cols }: { title: string; cols: number }) {
  return (
    <tr className={s.sectionRow}>
      <td className={`${s.sticky} ${s.code} ${s.sectionHead}`} colSpan={2}>
        {title}
      </td>
      <td className={s.sectionFill} colSpan={cols} />
    </tr>
  );
}

function TotalRow({
  label,
  periods,
  getValue,
  strong = false,
}: {
  label: string;
  periods: string[];
  getValue: (periodo: string) => number;
  strong?: boolean;
}) {
  return (
    <tr className={s.totalRow}>
      <td className={`${s.sticky} ${s.code} ${s.totalHead}`} colSpan={2}>
        {label}
      </td>
      {periods.map((p) => {
        const v = getValue(p);
        const isZero = v === 0;
        return (
          <td key={`${label}-${p}`} className={`${s.num} ${isZero ? s.mutedStrong : strong ? s.bold : ''}`}>
            {money(v)}
          </td>
        );
      })}
    </tr>
  );
}