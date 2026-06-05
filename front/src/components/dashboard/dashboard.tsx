'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { ChevronLeft, ChevronRight, LogOut } from 'lucide-react';

import { useAuthContext } from '@/context/AuthContext';
import { useSidebar } from '@/context/SidebarContext';

import type { RoleCode } from '@/features/navigation/menu.config';
import { MENU_SECTIONS } from '@/features/navigation/menu.config';

import s from './sidebar.module.css';
import Image from 'next/image';

/* ──────────────────────────────────────────────
  Roles (API -> UI)
────────────────────────────────────────────── */
function roleCodeFromUser(user: { roles?: Array<{ id: number }> } | null): RoleCode {
  const roleId = user?.roles?.[0]?.id;
  return roleId === 1 ? 'ADMIN' : 'USER';
}

export default function Sidebar() {
  const pathname = usePathname();

  const { user, logout, loading } = useAuthContext();
  const { open, toggle } = useSidebar();

  const [hydrated, setHydrated] = useState(false);
  useEffect(() => setHydrated(true), []);

  const uiOpen = hydrated ? open : false;

  /* (Opcional) menú flotante / gear — lo dejamos listo */
  const [showMenu, setShowMenu] = useState(false);
  const [, setMenuPos] = useState<{ top: number; left: number }>({ top: 0, left: 0 });
  const gearRef = useRef<HTMLButtonElement>(null);

  const placeMenu = useCallback(() => {
    if (!gearRef.current) return;
    const r = gearRef.current.getBoundingClientRect();
    const top = Math.min(Math.max(12, r.top), window.innerHeight - 160);
    const left = r.right + 12;
    setMenuPos({ top, left });
  }, []);

  useEffect(() => {
    if (!showMenu) return;

    placeMenu();

    const onDoc = (e: MouseEvent) => {
      if (!gearRef.current || !gearRef.current.contains(e.target as Node)) setShowMenu(false);
    };
    const onMove = () => placeMenu();

    document.addEventListener('click', onDoc);
    window.addEventListener('resize', onMove);
    window.addEventListener('scroll', onMove, true);

    return () => {
      document.removeEventListener('click', onDoc);
      window.removeEventListener('resize', onMove);
      window.removeEventListener('scroll', onMove, true);
    };
  }, [showMenu, placeMenu]);

  /* ──────────────────────────────────────────────
    Menu filtrado por rol (+ puedes filtrar por feature si quieres)
  ─────────────────────────────────────────────── */
  const roleSet = useMemo(() => {
    const code = roleCodeFromUser(user);
    return new Set<RoleCode>([code]);
  }, [user]);

  const sections = useMemo(() => {
    if (loading) return [];

    return MENU_SECTIONS
      .map((sec) => {
        const items = sec.items.filter((i) => i.roles.some((r) => roleSet.has(r)));
        return { ...sec, items };
      })
      .filter((sec) => sec.items.length > 0);
  }, [loading, roleSet]);

  const isActive = useCallback(
    (href: string) => pathname === href || pathname.startsWith(href + '/'),
    [pathname]
  );

  /* Tooltip positioning */
  const handleTipPos = useCallback((el: HTMLElement) => {
    const r = el.getBoundingClientRect();
    el.style.setProperty('--tip-top', `${Math.round(r.top + r.height / 2)}px`);
    el.style.setProperty('--tip-left', `${Math.round(r.right + 12)}px`);
  }, []);

  const asideClass = `${s.aside} ${hydrated ? (uiOpen ? s.open : s.closed) : ''}`;

  return (
    <aside className={asideClass}>
      {/* Header */}
      <div className={s.header}>
        <button className={s.toggle} onClick={toggle} aria-label={uiOpen ? 'Contraer' : 'Expandir'}>
          {uiOpen ? <ChevronLeft /> : <ChevronRight />}
        </button>

        <div className={s.brand}>
          {/* ✅ el logo NO se oculta en collapsed (solo el texto) */}
          {uiOpen && <span className={s.brandName}>GLINK - KEPLER</span>}
        </div>
      </div>

      {/* Nav */}
      <nav className={s.nav} aria-label="Navegación principal">
        {sections.map((sec) => (
          <div key={sec.id} className={s.section}>
            {/* etiqueta de sección (solo visible cuando está abierto) */}
            {uiOpen && <div className={s.sectionLabel}>{sec.label}</div>}

            <div className={s.sectionItems}>
              {sec.items.map(({ id, href, label, icon: Icon }) => {
                const active = isActive(href);

                return (
                  <Link
                    key={id}
                    href={href}
                    className={`${s.item} ${active ? s.active : ''}`}
                    aria-label={label}
                    onMouseEnter={(e) => handleTipPos(e.currentTarget)}
                  >
                    <span className={s.rail} aria-hidden />
                    <Icon className={s.icon} />
                    {uiOpen && <span className={s.label}>{label}</span>}
                    {!uiOpen && <span className={s.tooltip}>{label}</span>}
                  </Link>
                );
              })}
            </div>

            {/* separador entre secciones */}
            <div className={s.sectionDivider} aria-hidden />
          </div>
        ))}
      </nav>

      {/* Controles inferiores */}
      <div className={s.controls}>
        <button
          className={`${s.item} ${s.itemDanger}`}
          onClick={logout}
          aria-label="Cerrar sesión"
          onMouseEnter={(e) => handleTipPos(e.currentTarget)}
        >
          <span className={s.rail} aria-hidden />
          <LogOut className={`${s.icon} ${s.iconDanger}`} />
          {uiOpen && <span className={s.label}>Cerrar sesión</span>}
          {!uiOpen && <span className={s.tooltip}>Cerrar sesión</span>}
        </button>
      </div>

      {/* Perfil */}
      <div className={s.profile}>
        <Image className={s.avatar} src="/img/user.png" alt="Avatar" width={40} height={40} />
        {uiOpen && (
          <div className={s.userMeta}>
            {/* ✅ esto evita que “LEMUS” brinque o rompa layout */}
            <div className={s.userName}>{user?.name ?? 'Usuario'}</div>
            <div className={s.userEmail}>{user?.email ?? 'correo@dominio'}</div>
          </div>
        )}
      </div>

      {/* {showMenu && (
        <div className={s.menuPopup} style={{ top: menuPos.top, left: menuPos.left }}>
          ...
        </div>
      )} */}
    </aside>
  );
}
