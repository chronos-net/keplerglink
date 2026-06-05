import { MessageSquareText } from 'lucide-react';

import styles from './CommentsEmptyState.module.css';

type CommentsEmptyStateProps = {
    title?: string;
    description?: string;
};

export default function CommentsEmptyState({
    title = 'Sin comentarios',
    description,
}: CommentsEmptyStateProps) {
    return (
        <div className={styles.empty}>
            <MessageSquareText size={24} />
            <p>{title}</p>

            {description && (
                <span>{description}</span>
            )}
        </div>
    );
}