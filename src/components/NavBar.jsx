import { Outlet } from "react-router-dom";
import Category from './Category.jsx'
import '../App.css'

export default function NavBar(){
  const options = ["Home", "Create Forum", "About"]
  const option_buttons = options.map(a =>
    <Category s="m-8 p-4 font-mono text-6xl bg-teal-950" name={a} />
  )

  return(<>
    <div className="answer-buttons m-1 absolute bg-gradient-to-tr from-red-400 to-red-500 top-0 left-1/2 -translate-x-1/2 w-11/12">
      {option_buttons}
    </div>
 
    <Outlet />
  </>)
}
