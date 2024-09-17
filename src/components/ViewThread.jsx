import { useState, useEffect } from 'react'
import '../App.css'
//Imports
import Category from "./Category.jsx"

export default function ViewThread( props ) {
  const [content, setContent] = useState({}) // Initialize with an empty object instead of an empty array

  // Use useEffect to set content only when props.data changes
  useEffect(() => {
    if (props.data) {
      setContent(props.data)
    }
  }, [props.data]) // Dependency array ensures this runs only when props.data changes

  return (
    <>
      {content ? (
        <>
          <h1>{content.title}</h1>
          <h2>{content.content}</h2>
        </>
      ) : (
        <p>Error getting content</p>
      )}
    </>
  )
}
