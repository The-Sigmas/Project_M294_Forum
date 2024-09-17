import Category from './Category.jsx'

export default function ErrorCode( props ) {
  return(<>
    <h1 className="text-white">Following error occured: { props.code }</h1>
    <h2 className="text-white">Click "here" to return back to home page</h2>
    <Category s="text-white" name="Home" />
  </>)
}
