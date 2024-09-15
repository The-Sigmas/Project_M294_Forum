import { Link } from 'react-router-dom';

function Category(props) {
  return (
    <Link className={`${props.s}`} to={`/${props.name.toLowerCase()}`}>
      {props.name}
    </Link>
  );
}
