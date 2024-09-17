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
            <div className="m-4 bg-gray-800 rounded">
              <p className="p-4 text-xl text-white w-1/2">{doc.content.title}</p>
            </div>
          </Link>
        ))
      ) : (
        <p>Error getting content or mapping data</p>
      )}
    </>
  )
}
