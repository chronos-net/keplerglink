import styles from './CommentsLoadingState.module.css';

export default function CommentsLoadingState() {
  return (
    <div className={styles.state}>
      <div className={styles.skelTitle} />
      <div className={styles.skelRow} />
      <div className={styles.skelRow} />
      <div className={styles.skelCard} />
      <div className={styles.skelCard} />
    </div>
  );
}