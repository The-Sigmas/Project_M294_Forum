import Category from './Category.jsx'

export default function ErrorCode( props ) {
  return(<>
    <h1>Following error occured: { props.code }</h1>
    <h2>Click "here" to return back to home page</h2>
    <Category s="" name="Home" />
  </>)
}
