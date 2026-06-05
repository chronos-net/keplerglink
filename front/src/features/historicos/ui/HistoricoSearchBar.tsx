// src/features/historicos/ui/HistoricoSearchBar.tsx
'use client';

import { FormEvent } from 'react';
import { Search } from 'lucide-react';
import s from './HistoricoSearchBar.module.css';

type Props = {
  query: string;
  loading: boolean;
  onQueryChange(value: string): void;
  onSubmit(): void;
};

export default function HistoricoSearchBar({
  query,
  loading,
  onQueryChange,
  onSubmit,
}: Props) {
  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    onSubmit();
  };

  return (
    <form className={s.form} onSubmit={handleSubmit}>
      <div className={s.inputWrapper}>
        <Search className={s.icon} size={16} />
        <input
          className={s.input}
          value={query}
          onChange={(e) => onQueryChange(e.target.value)}
          placeholder="Número de servidor, CURP o RFC"
        />
      </div>

      <button
        type="submit"
        className={`${s.button} ${loading ? s.buttonDisabled : ''}`}
        disabled={loading}
      >
        {loading ? 'Buscando…' : 'Filtrar'}
      </button>
    </form>
  );
}
