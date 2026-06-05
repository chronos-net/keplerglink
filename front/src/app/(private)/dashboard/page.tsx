'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuthContext } from '@/context/AuthContext';
import styles from './page.module.css';

export default function DashboardPage() {
  const { loading } = useAuthContext();
  const router = useRouter();

  useEffect(() => {
    if (loading) return;
    router.replace('/dashboard/historico');
  }, [loading, router]);

  // Skeleton mientras redirige
  return (
    <main className={styles.main}>
      <div className={styles.card}>
        <div className={styles.skeletonTitle} />
        <div className={styles.skeletonLine} />
      </div>
    </main>
  );
}
