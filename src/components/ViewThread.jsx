import { useState, useEffect } from 'react'
import '../App.css'
//Imports
import Category from "./Category.jsx"

export default function ViewThread( props ) {
  const [content, setContent] = useState([])

  setContent( props.data )

  return(<>
    <h1> content.title </h1>
    <h2> content.content </h2>
  </>)
}
