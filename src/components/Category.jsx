import { Link } from 'react-router-dom';

export default function Category(props) {
  return (
    <Link className={`${props.s}`} to={`/${props.name.toLowerCase()}`}>
      {props.name}
    </Link>
  );
}
