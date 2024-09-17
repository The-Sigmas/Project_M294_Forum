import { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import '../App.css'

export default function ShowThreads( props ) {
  const [data, setData] = useState([])

  // Use useEffect to set data only when props.data changes
  useEffect(() => {
    if (props.data) {
      setData(props.data)
    }
  }, [props.data]) // Dependency array ensures this only runs when props.data changes

  return (
    <>
      {data.length > 0 ? (
        data.map((doc) => (
          <Link key={doc.content.id} to={`/threads/${ doc.id }`}>
            <p>{doc.content.title}</p>
          </Link>
        ))
      ) : (
        <p>Error getting content or mapping data</p>
      )}
    </>
  )
}
