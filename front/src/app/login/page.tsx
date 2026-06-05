'use client';
import { Suspense } from 'react';
import LoginForm from '@/features/auth/components/LoginForm';
import Image from 'next/image';
import s from './login.module.css';

export default function LoginPage() {
  return (
    <div className={s.page}>
      <div className={s.container}>
        <section className={s.leftPanel}>
          <div className={s.leftContent}>
            <h1> Histórico · G-Link / Kepler</h1>
            <p>
              Consulta y gestiona el historial: administra registros, emite validaciones y mantén control con una
              experiencia rápida y clara.
            </p>
          </div>
        </section>

        <section className={s.rightPanel}>
          <div className={s.rightScroll}>
            <div className={s.formWrap}>
              <div className={s.logoBox}>
                <Image src="/img/logo.png" alt="Logo institucional" width={140} height={60} />
              </div>
              <Suspense fallback={<div>Cargando...</div>}>
                <LoginForm />
              </Suspense>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
}
