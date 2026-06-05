'use client';

import { useState } from 'react';
import { Eye, EyeOff, LockKeyhole } from 'lucide-react';
import InputIcon from '../InputIcon/InputIcon';
import s from './PasswordField.module.css';

type Props = {
  value: string;
  onChange: (v: string) => void;
  label?: string;
  hint?: string;
  error?: string | null;
  placeholder?: string;
  disabled?: boolean; 
};

export default function PasswordField({
  value,
  onChange,
  label = 'Contraseña',
  hint,
  error,
  placeholder = '••••••••',
  disabled = false, 
}: Props) {
  const [show, setShow] = useState(false);

  return (
    <InputIcon
      label={label}
      hint={hint}
      error={error}
      leftIcon={LockKeyhole}
      type={show ? 'text' : 'password'}
      value={value}
      onChange={(e) => onChange(e.currentTarget.value)}
      placeholder={placeholder}
      autoComplete="current-password"
      disabled={disabled} 
      rightSlot={
        <button
          type="button"
          onClick={() => setShow((v) => !v)}
          className={s.eyeBtn}
          aria-label={show ? 'Ocultar contraseña' : 'Ver contraseña'}
          title={show ? 'Ocultar contraseña' : 'Ver contraseña'}
          disabled={disabled} 
        >
          {show ? <EyeOff /> : <Eye />}
        </button>
      }
    />
  );
}
