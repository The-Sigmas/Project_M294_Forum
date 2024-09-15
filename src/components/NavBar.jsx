import { Outlet } from "react-router-dom";
import Category from './Category.jsx'
import '../App.css'

export default function NavBar(){
  const options = ["autos", "cpu", "gpu"]
  const option_buttons = options.map(a =>
    <Category s="m-8 p-4 font-mono text-6xl bg-teal-950" name={a} />
  )

  return(<>
    <div className="answer-buttons m-1 absolute bg-teal-600 top-4 -translate-y-1/2 -translate-x-1/2 left-4">
      {option_buttons}
    </div>
 
    <Outlet />
  </>)
}
