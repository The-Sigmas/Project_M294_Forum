import { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import '../App.css'

export default function ShowThreads( props ) {
  const [data, setData] = useState([])

  setData( props.data )

  return(<>
    {data.length > 0 ? (
      data.map((doc) => (
        <Link to={`/threads/${ doc.content.id }`}>
          <p> { doc.content.title } </p>
        </Link>
      ))
    ) : (
      <p>Error getting content or mapping data</p>
    )}
  </>)
}
