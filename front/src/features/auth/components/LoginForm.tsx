'use client';

import { User2 } from 'lucide-react';
import { useRouter, useSearchParams } from 'next/navigation';
import { toast } from 'sonner';

import { useLoginForm } from '@/features/auth/hooks/useLoginForm';

import s from './LoginForm.module.css';
import InputIcon from './fields/InputIcon/InputIcon';
import PasswordField from './fields/PasswordField/PasswordField';

export default function LoginForm() {
  const router = useRouter();
  const sp = useSearchParams();
  const from = sp.get('from') || '/dashboard';

  const {
    username,
    password,
    loading,
    error,
    valid,
    setUsername,
    setPassword,
    submit,
  } = useLoginForm({
    onSuccess: () => {
      toast.success('Acceso exitoso. ¡Bienvenido de nuevo!');
      router.replace(from);
      router.refresh();
    },
  });

  return (
    <form
      className={s.form}
      onSubmit={(e) => {
        e.preventDefault();
        if (!loading) submit();
      }}
      aria-describedby="login-help"
    >
      <div className={s.header}>
        <h2 className={s.title}>Iniciar sesión</h2>
        <p className={s.subtitle}>
          Ingresa tus credenciales para acceder al Sistema Histórico y continuar con tu gestión.
        </p>
      </div>

      {error && (
        <div className={s.error} role="alert" aria-live="polite">
          {error}
        </div>
      )}

      <InputIcon
        label="Usuario"
        hint="Tu usuario institucional"
        leftIcon={User2}
        type="text"
        name="username"
        value={username}
        onChange={(e) => setUsername(e.currentTarget.value)}
        placeholder="tu.usuario"
        autoComplete="username"
        disabled={loading}
      />

      <PasswordField
        value={password}
        onChange={setPassword}
        hint="Mínimo 8 caracteres (recomendado)"
        error={null}
        disabled={loading}
      />

      <div id="login-help" className={s.helperList} aria-live="polite">
        <ul>
          <li>Evita iniciar sesión en equipos compartidos</li>
          <li>Si olvidaste tu contraseña, contacta a Soporte</li>
        </ul>
      </div>

      <div className={s.actions}>
        <button
          className={s.primaryBtn}
          type="submit"
          disabled={!valid || loading}
          aria-busy={loading}
        >
          {loading ? 'Ingresando…' : 'Entrar'}
        </button>
      </div>

      <footer className={s.footerNote}>
        <small>
          v1.0 · Desarrollado por <strong>Dirección de Sistemas y Tecnología</strong>
        </small>
      </footer>
    </form>
  );
}
