import { Link } from 'react-router-dom';

export default function Category(props) {
  if ( props.name.toLowerCase() == "home" ) {
    return (
      <Link className={`${props.s}`} to={`/`}>
        {props.name}
      </Link>
    );
  } else {
    return (
      <Link className={`${props.s}`} to={`/${props.name.toLowerCase()}`}>
        {props.name}
      </Link>
    );
  }
}
