import styles from './CommentsErrorState.module.css';

type CommentsErrorStateProps = {
    error: string;
};

export default function CommentsErrorState({ error }: CommentsErrorStateProps) {
    return (
        <div className={styles.error}>
            <strong>No fue posible cargar los comentarios.</strong>
            <span>{error}</span>
        </div>
    );
}