import { Outlet } from "react-router-dom";
import Category from './Category.jsx'
import '../App.css'

export default function NavBar(){
  const options = ["Home", "Create Forum", "About"]
  const option_buttons = options.map(a =>
    <Category s="m-8 text-gray-50 font-mono text-xl hover:text-gray-100 hover:backdrop-blur-sm hover:bg-white/30 rounded p-1" name={a} />
  )

  return(<>
    <div className="answer-buttons m-1 absolute bg-gradient-to-r from-red-500 via-red-700 to-red-900 top-0 left-1/2 -translate-x-1/2 w-11/12 rounded">
      {option_buttons}
    </div>
 
    <Outlet />
  </>)
}
