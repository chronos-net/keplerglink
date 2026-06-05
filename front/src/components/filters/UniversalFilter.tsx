'use client';

import { Search, Briefcase, Calendar, Filter } from 'lucide-react';
import s from './css/RecibosFilters.module.css';

import type { ServidorLite } from '@/features/comprobantes/services/payroll.service';
import type { UniversalFiltersProps } from './types/types';

// hooks tuyos

import { useUniverSalFilters } from './hooks/useUniverSalFilters';
import { useServidorAutocomplete } from './hooks/useServidorAutocomplete';

// utils tuyos
import { clampQuincena } from './utils/universalFilters.utils';
import { allowNumericForSource } from '@/utils/autoCompleteSource.utils';
import { useEffect, useState } from 'react';

export default function UniversalFilter({
  busy,
  initialText = '',
  initialAnio,
  initialQuincena,

  hideQuincena = false,
  showYear = true,
  showQuincena = true,

  onFilter,
  autocompleteSource,
  autoSubmitOnSelect = true,

  yearOptions,
  quincenaOptions,
  textPlaceholder,

  requireText,
  requireYear,
  requireQuincena,

  textMode,
  modoEnvioTexto = 'libre',

  showPdfButton = false,
  onGeneratePdf,
  pdfBusy = false,
  pdfLabel = 'Generar PDF',
}: UniversalFiltersProps) {
  // ----------------------------
  // 1) estado + reglas + buildQuery (hook)
  // ----------------------------
  const {
    text,
    setText,
    anioStr,
    setAnioStr,
    qStr,
    setQStr,
    setResolvedNeyemp,

    renderYear,
    renderQuincena,
    canSubmit,

    buildQuery,
    canSubmitWithText,
  } = useUniverSalFilters({
    initialText,
    initialAnio,
    initialQuincena,
    showYear,
    showQuincena,
    hideQuincena,
    requireText,
    requireYear,
    requireQuincena,
    modoEnvioTexto,
  });

  // ----------------------------
  // 2) autocomplete (hook)
  // ----------------------------
  const {
    suggestions,
    showSuggestions,
    searching,
    handleTextChange,
    closeSuggestions,
  } = useServidorAutocomplete({
    enabled: true,
    source: autocompleteSource,   // 👈 se pasa aquí
    setText,
    setResolvedNeyemp,
  });

  const [activeIndex, setActiveIndex] = useState<number>(-1);
  useEffect(() => {
    if (!showSuggestions || suggestions.length === 0) {
      setActiveIndex(-1);
      return;
    }

    setActiveIndex(0);
  }, [showSuggestions, suggestions]);

  // ----------------------------
  // 3) acciones
  // ----------------------------
  const submit = () => {
    if (!canSubmit || !isTextModeValid) return;

    closeSuggestions();
    setActiveIndex(-1);
    onFilter(buildQuery());
  };

  const onKeyDown = (e: React.KeyboardEvent<HTMLInputElement | HTMLSelectElement>) => {
    if (e.key === 'ArrowDown' && showSuggestions && suggestions.length > 0) {
      e.preventDefault();
      setActiveIndex((prev) => (prev < suggestions.length - 1 ? prev + 1 : 0));
      return;
    }

    if (e.key === 'ArrowUp' && showSuggestions && suggestions.length > 0) {
      e.preventDefault();
      setActiveIndex((prev) => (prev > 0 ? prev - 1 : suggestions.length - 1));
      return;
    }

  };

  const handleSelectServidor = (srv: ServidorLite) => {

    const shouldShowKey = allowNumericForSource(autocompleteSource);
    const nextText = shouldShowKey ? srv.neyemp : srv.negnom;

    setText(nextText);
    setResolvedNeyemp(srv.neyemp);
    closeSuggestions();
    setActiveIndex(-1);

    if (!autoSubmitOnSelect) return;

    // auto-submit SOLO si ya cumple reglas (año/quincena según módulo)
    if (!canSubmitWithText(nextText)) return;

    onFilter(
      buildQuery({
        text: nextText,
        resolved: srv.neyemp,
      })
    );
  };

  const wrpClassName = [
    s.wrap,
    !renderQuincena && renderYear ? s.wrapNoQuincena : '',
    !renderYear && !renderQuincena ? s.wrapOnlyText : '',
  ].filter(Boolean).join(' ')


  const uniqueSuggestions = suggestions.filter(
    (item, index, self) =>
      index === self.findIndex((s) => s.neyemp === item.neyemp)
  );

  const onlyNumericText = textMode === 'numericKey9';
  const textMaxLength = textMode === 'numericKey9' ? 9 : undefined;
  const isTextModeValid =
    !onlyNumericText || /^\d{9}$/.test(text.trim());

  // ----------------------------
  // UI
  // ----------------------------
  return (
    <div className={wrpClassName}>
      {/* Nombre o clave */}
      <div className={s.fieldWrap}>
        <div className={s.field}>
          <Search className={s.icon} size={16} />
          <input
            className={s.input}
            placeholder={textPlaceholder ?? 'Nombre o clave'}
            value={text}
            inputMode={onlyNumericText ? 'numeric' : undefined}
            maxLength={textMaxLength}
            onChange={(e) => {
              setActiveIndex(-1);
              setResolvedNeyemp(undefined);

              let nextValue = e.target.value;

              if (onlyNumericText) {
                nextValue = nextValue.replace(/\D/g, '');
              }

              handleTextChange(nextValue);
            }}
            onKeyDown={onKeyDown}
          />
        </div>

        {showSuggestions && (
          <ul className={s.suggestions}>
            {searching && suggestions.length === 0 && (
              <li className={s.suggestionLoading}>Buscando…</li>
            )}

            {uniqueSuggestions.map((srv, index) => (
              <li
                key={srv.neyemp}
                className={`${s.suggestionItem} ${index === activeIndex ? s.suggestionItemActive : ''}`}
                onClick={() => handleSelectServidor(srv)}
              >
                <span className={s.suggestionName}>{srv.negnom}</span>
                <span className={s.suggestionCode}>{srv.neyemp}</span>
              </li>
            ))}

            {!searching && suggestions.length === 0 && (
              <li className={s.suggestionEmpty}>Sin coincidencias</li>
            )}
          </ul>
        )}
      </div>

      {/* Año */}
      {renderYear && (
        <div className={s.field}>
          <Briefcase className={s.icon} size={16} />

          {yearOptions && yearOptions.length > 0 ? (
            <select
              className={s.input}
              value={anioStr}
              onChange={(e) => setAnioStr(e.target.value)}
              onKeyDown={onKeyDown}
            >
              <option value="">Año</option>
              {yearOptions.map((opt) => (
                <option key={String(opt.value)} value={String(opt.value)}>
                  {opt.label}
                </option>
              ))}
            </select>
          ) : (
            <input
              className={s.input}
              inputMode="numeric"
              placeholder="Año"
              value={anioStr}
              onChange={(e) => setAnioStr(e.target.value.replace(/\D/g, '').slice(0, 4))}
              onKeyDown={onKeyDown}
            />
          )}
        </div>
      )}

      {/* Quincena */}
      {renderQuincena && (
        <div className={s.field}>
          <Calendar className={s.icon} size={16} />

          {quincenaOptions && quincenaOptions.length > 0 ? (
            <select
              className={s.input}
              value={qStr}
              onChange={(e) => setQStr(e.target.value)}
              onKeyDown={onKeyDown}
            >
              <option value="">Quincena</option>
              {quincenaOptions.map((opt) => (
                <option key={String(opt.value)} value={String(opt.value)}>
                  {opt.label}
                </option>
              ))}
            </select>
          ) : (
            <input
              className={s.input}
              inputMode="numeric"
              placeholder="01"
              value={qStr}
              onChange={(e) => setQStr(e.target.value.replace(/\D/g, '').slice(0, 2))}
              onBlur={(e) => setQStr(clampQuincena(e.target.value))}
              onKeyDown={onKeyDown}
            />
          )}
        </div>
      )}

      {/* Acciones */}
      <div className={s.actions}>
        <button
          className={s.btn}
          disabled={busy || !canSubmit || !isTextModeValid}
          onClick={submit}
          type="button"
        >
          <Filter size={16} />
          <span>{busy ? 'Filtrando…' : 'Filtrar'}</span>
        </button>

        {showPdfButton && onGeneratePdf && (
          <button
            type="button"
            className={`${s.btn} ${s.btnPdf}`}
            disabled={pdfBusy || busy}
            onClick={onGeneratePdf}
          >
            <span>{pdfBusy ? 'Generando…' : pdfLabel}</span>
          </button>
        )}
      </div>
    </div>
  );
}
