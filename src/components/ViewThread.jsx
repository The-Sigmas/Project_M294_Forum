import { useState, useEffect } from 'react';
import '../App.css';
import { Link } from 'react-router-dom'
//Imports
import Category from "./Category.jsx";
import CommentThread from "./CommentThread.jsx";

export default function ViewThread( props ) {
  const [content, setContent] = useState({}); // Initialize with an empty object
  const [responseMessage, setResponseMessage] = useState('');

  // Use useEffect to set content only when props.data changes
  useEffect(() => {
    if (props.data) {
      setContent(props.data);
    }
  }, [props.data]); // Dependency array ensures this runs only when props.data changes

  const handleDelete = async () => {
    try {
      const response = await fetch(`http://localhost:8080/threads/documents/${props.id}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        setResponseMessage('Thread deleted successfully!');
        window.location.reload()
        // Optionally, redirect or remove the thread from the UI
      } else {
        setResponseMessage('Failed to delete the thread.');
      }
    } catch (error) {
      console.error(error);
      setResponseMessage('An error occurred.');
    }
  };

  return (
    <>
      {content ? (
        <>
          <div className="bg-gray-800 p-6 m-2">
            <h1>{content.title}</h1>
            <h2>{content.content}</h2>
          </div>
          <hr />
          <Link
            to="/"
            onClick={handleDelete}
            className="px-4 py-2 bg-red-600 text-white rounded-lg shadow hover:bg-red-700"
          >
            Delete Thread
          </Link>
          {responseMessage && <p>{responseMessage}</p>}
          <hr />
          <CommentThread threadId={props.id} />
        </>
      ) : (
        <p>Error getting content</p>
      )}
    </>
  );
}
