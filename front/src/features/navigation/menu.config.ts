// src/features/navigation/menu.config.ts
import type { ComponentType } from 'react';
import type { LucideProps } from 'lucide-react';

import {
  BarChart3,
  ReceiptText,
  History,
  HandCoins,
  Landmark,
  LayoutList,
  BadgeInfo,
  MessageSquare 
} from 'lucide-react';

/**
 * Roles
 
 * Hoy el backend solo maneja 2 tipos reales:
 * - cveTipoUsuario = 1 => ADMIN
 * - cveTipoUsuario = 2 => USER
 * 
 */
export type RoleCode = 'ADMIN' | 'USER';

export type MenuSectionId = 'KEPLER' | 'GLINK' | 'PRESTACIONES';

export type MenuItem = {
  id: string;
  label: string;
  href: string;
  icon: ComponentType<LucideProps>;
  roles: RoleCode[];
  feature?: string;
};

export type MenuSection = {
  id: MenuSectionId;
  label: string;
  items: MenuItem[];
};

export const MENU_SECTIONS: MenuSection[] = [
  /* ===================== KEPLER ===================== */
  {
    id: 'KEPLER',
    label: 'KEPLER',
    items: [
    
      {
        id: 'comprobantes',
        label: 'Comprobantes',
        href: '/dashboard/comprobantes',
        icon: ReceiptText,
        roles: ['ADMIN', 'USER'],
      },
      {
        id: 'anualizado',
        label: 'Anualizados',
        href: '/dashboard/anualizado',
        icon: BarChart3,
        roles: ['ADMIN', 'USER'],
      },
       {
        id: 'desc-plazas',
        label: 'Desgloses de plazas',
        href: '/dashboard/desc-plazas',
        icon: LayoutList,
        roles: ['ADMIN', 'USER'],
      },
       {
        id: 'comentarios',
        label: 'Comentarios',
        href: '/dashboard/comentarios',
        icon: MessageSquare ,
        roles: ['ADMIN', 'USER'],
      },
    ],
  },

  /* ===================== GLINK ===================== */
  {
    id: 'GLINK',
    label: 'GLINK',
    items: [
      {
        id: 'prestamos-glink',
        label: 'Préstamos',
        href: '/dashboard/prestamo',
        icon: HandCoins,
        roles: ['ADMIN', 'USER'],
      },
      {
        id: 'desc-prestamo',
        label: 'Pensiones Alimenticias',
        href: '/dashboard/pensiones_alim',
        icon: Landmark,
        roles: ['ADMIN', 'USER'],
      },
        {
        id: 'historico-laboral',
        label: 'Histórico laboral',
        href: '/dashboard/historico',
        icon: History,
        roles: ['ADMIN', 'USER'],
      },
     
    ],
  },

  /* ===================== DAVS (placeholder) ===================== */
  {
    id: 'PRESTACIONES',
    label: 'PRESTACIONES',
    items: [
      {
        id: 'davs-historico',
        label: 'Histórico',
        href: '/dashboard/historico_davs', 
        icon: BadgeInfo,
        roles: ['ADMIN', 'USER'],
        feature: 'Prestaciones', 
      },
    ],
  },
];


