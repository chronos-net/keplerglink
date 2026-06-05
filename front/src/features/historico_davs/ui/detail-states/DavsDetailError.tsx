import s from './DavsDetailStates.module.css';

type Props = {
  message: string;
};

export default function DavsDetailError({ message }: Props) {
  return <div className={s.error}>{message}</div>;
}